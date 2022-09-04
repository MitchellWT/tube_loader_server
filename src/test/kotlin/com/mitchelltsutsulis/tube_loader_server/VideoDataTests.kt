package com.mitchelltsutsulis.tube_loader_server

import com.mitchelltsutsulis.tube_loader_server.storage.Video
import com.mitchelltsutsulis.tube_loader_server.storage.VideoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.sql.Timestamp
import java.time.Instant

@SpringBootTest
class VideoDataTests(@Autowired val videoRepository: VideoRepository) {
    @Test
    fun `video create`() {
        val video = Video(
            videoId = "Epic Vid Id",
            title = "Epic Vid",
            thumbnail = "https://epic_thumbnail"
        )
        val videoSaved = videoRepository.save(video)
        val repoVideo = videoRepository.findById(videoSaved.id!!).get()

        assertThat(videoSaved.id).isEqualTo(repoVideo.id)

        assertThat(videoSaved.videoId).isEqualTo(video.videoId)
        assertThat(repoVideo.videoId).isEqualTo(video.videoId)

        assertThat(videoSaved.title).isEqualTo(video.title)
        assertThat(repoVideo.title).isEqualTo(video.title)

        assertThat(videoSaved.thumbnail).isEqualTo(video.thumbnail)
        assertThat(repoVideo.thumbnail).isEqualTo(video.thumbnail)
    }

    @Test
    fun `video update`() {
        val video = Video(
            videoId = "Funky Vid Id",
            title = "Funky Vid",
            thumbnail = "https://funky_thumbnail"
        )
        val videoSaved = videoRepository.save(video)
        val repoVideo = videoRepository.findById(videoSaved.id!!).get()
        val updatedTitle = "Very Funky Vid"
        val downloadTime = Timestamp.from(Instant.now())
        repoVideo.title = updatedTitle
        repoVideo.queued = false
        repoVideo.downloaded = true
        repoVideo.downloadedAt = downloadTime
        videoRepository.save(repoVideo)
        val updatedVideo = videoRepository.findById(videoSaved.id!!).get()

        assertThat(updatedVideo.title).isEqualTo(updatedTitle)
        assertThat(updatedVideo.queued).isEqualTo(false)
        assertThat(updatedVideo.downloaded).isEqualTo(true)
        assertThat(updatedVideo.downloadedAt!!.time).isEqualTo(downloadTime.time)
    }

    @Test
    fun `video delete`() {
        val video = Video(
            videoId = "Shitty Vid Id",
            title = "Shitty Vid",
            thumbnail = "https://shitty_thumbnail"
        )
        val videoSaved = videoRepository.save(video)

        assertThat(videoRepository.findById(videoSaved.id!!).isPresent).isEqualTo(true)
        videoRepository.deleteById(videoSaved.id!!)
        assertThat(videoRepository.findById(videoSaved.id!!).isPresent).isEqualTo(false)
    }

    @Test
    fun `test pulling the first video in the queue`() {
        val video = Video(
            videoId = "Cool Vid Id",
            title = "Cool Vid",
            thumbnail = "https://cool_thumbnail",
            queued = false
        )
        videoRepository.save(video)
        val videoQueued = Video(
            videoId = "Very Cool Vid Id",
            title = "Very Cool Vid",
            thumbnail = "https://very_cool_thumbnail"
        )
        val videoQueuedSaved = videoRepository.save(videoQueued)
        val videoInQueue = videoRepository.findFirstInQueue().get()

        assertThat(videoInQueue.videoId).isEqualTo(videoQueuedSaved.videoId)
    }

    @Test
    fun `test video queue removal`() {
        val video = Video(
            videoId = "Epic Vid Id",
            title = "Epic Vid",
            thumbnail = "https://epic_thumbnail"
        )
        videoRepository.save(video)
        val videoInQueue = videoRepository.findFirstInQueue().get()

        assertThat(videoInQueue.videoId).isEqualTo(video.videoId)

        videoRepository.removeFromQueue(videoInQueue.id!!)
        val noQueueVideo = videoRepository.findFirstInQueue()

        assertThat(noQueueVideo.isEmpty).isEqualTo(true)
    }
}
