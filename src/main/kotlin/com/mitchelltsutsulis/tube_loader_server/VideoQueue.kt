package com.mitchelltsutsulis.tube_loader_server

import com.mitchelltsutsulis.tube_loader_server.storage.VideoRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class VideoQueue(val videoRepository: VideoRepository, val downloadConfig: DownloadConfig) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var queueActive = false

    fun downloadVideo() {
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
    }

    fun getQueue() = queueActive

    fun toggleQueue(): Boolean {
        queueActive = !queueActive
        return queueActive
    }
}