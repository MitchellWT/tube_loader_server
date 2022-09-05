package com.mitchelltsutsulis.tube_loader_server

import com.mitchelltsutsulis.tube_loader_server.storage.VideoRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantLock

@Service
class VideoQueue(val videoRepository: VideoRepository, val downloadConfig: DownloadConfig) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val queueActive = AtomicBoolean(false)
    private val downloadLock = ReentrantLock()

    @Scheduled(cron = "#{downloadConfig.downloadCron}")
    fun downloadVideo() {
        if (downloadLock.tryLock()) {
            val videoOpt = videoRepository.findFirstInQueue()
            if (videoOpt.isEmpty) return
            val video = videoOpt.get()
            val processArgs = listOf("yt-dlp", *(downloadConfig.downloaderConfig), video.videoId)
            val downloadPB = ProcessBuilder(processArgs)
            do {
                val downloadProcess = downloadPB.start()
                logger.info("Output from ${video.title}/${video.videoId} download:")
                val exitCode = downloadProcess.waitFor()
                downloadProcess.inputStream.bufferedReader().readLines().forEach { logger.info(it) }
                logger.info("YT-DLP exited with exit code: $exitCode")
            } while (exitCode != 0)
            videoRepository.removeFromQueue(video.id!!)
            downloadLock.unlock()
        }
    }

    fun getQueue() = queueActive.get()

    fun toggleQueue(): Boolean {
        synchronized(queueActive) {
            queueActive.set(!queueActive.get())
            return queueActive.get()
        }
    }
}