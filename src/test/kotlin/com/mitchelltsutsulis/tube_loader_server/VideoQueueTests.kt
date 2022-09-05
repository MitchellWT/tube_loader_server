package com.mitchelltsutsulis.tube_loader_server

import com.mitchelltsutsulis.tube_loader_server.storage.Video
import com.mitchelltsutsulis.tube_loader_server.storage.VideoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

@SpringBootTest
class VideoQueueTests(
    @Autowired val videoQueue: VideoQueue,
    @Autowired val videoRepository: VideoRepository
) {
    @Test
    fun `test video downloading`() {
        val video = Video(
            videoId = "Yg57AvUBsWI",
            title = "Face",
            thumbnail = "https://i.ytimg.com/vi/Yg57AvUBsWI/default.jpg"
        )
        val videoSaved = videoRepository.save(video)
        if (!videoQueue.getQueue()) videoQueue.toggleQueue()
        videoQueue.downloadVideo()
        val noQueueVideo = videoRepository.findFirstInQueue()

        assertThat(noQueueVideo.isEmpty).isEqualTo(true)

        val thumbnail = File("Face [Yg57AvUBsWI].jpg")
        val videoFile = File("Face [Yg57AvUBsWI].mp4")

        assertThat(thumbnail.exists()).isEqualTo(true)
        assertThat(videoFile.exists()).isEqualTo(true)

        thumbnail.delete()
        videoFile.delete()
        videoRepository.deleteById(videoSaved.id!!)
    }
}