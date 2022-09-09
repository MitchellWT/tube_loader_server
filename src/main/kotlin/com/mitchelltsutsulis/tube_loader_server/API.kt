package com.mitchelltsutsulis.tube_loader_server

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mitchelltsutsulis.tube_loader_server.storage.Video
import com.mitchelltsutsulis.tube_loader_server.storage.VideoRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class API(val videoRepository: VideoRepository, val videoQueue: VideoQueue) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val objectMapper = jacksonObjectMapper()

    @GetMapping("videos", produces = ["application/json"])
    fun showVideos(
        @RequestParam(defaultValue = "10") amount: Int,
        @RequestParam(defaultValue = "0") page: Int
    ): ResponseEntity<String> {
        return try {
            val videos = videoRepository.findAll(PageRequest.of(page, amount, Sort.by("id")))
            val res = objectMapper.writeValueAsString(videos.toList())
            logger.info("showVideos SUCCESS res: $res")
            ResponseEntity(res, HttpStatus.OK)
        } catch (e: Exception) {
            val res = objectMapper.writeValueAsString(mapOf("res" to "fail", "message" to e.message))
            logger.info("showVideos FAIL res: $res")
            ResponseEntity(res, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("video/{id}", produces = ["application/json"])
    fun showVideo(@PathVariable id: Long): ResponseEntity<String> {
        return try {
            val video = videoRepository.findById(id).get()
            val res = objectMapper.writeValueAsString(video)
            logger.info("showVideo SUCCESS res: $res")
            ResponseEntity(res, HttpStatus.OK)
        } catch (e: Exception) {
            val res = objectMapper.writeValueAsString(mapOf("res" to "fail", "message" to e.message))
            logger.info("showVideo FAIL res: $res")
            ResponseEntity(res, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("video", produces = ["application/json"])
    fun storeVideo(@RequestBody videoJSON: String): ResponseEntity<String> {
        return try {
            val video = objectMapper.readValue(videoJSON, Video::class.java)
            val res = objectMapper.writeValueAsString(mapOf("res" to "success"))
            videoRepository.save(video)
            if (video.queued) {
                val downloadThread = Thread { videoQueue.downloadVideo() }
                downloadThread.start()
            }
            logger.info("storeVideo SUCCESS res: $res")
            ResponseEntity(res, HttpStatus.OK)
        } catch (e: Exception) {
            val res = objectMapper.writeValueAsString(mapOf("res" to "fail", "message" to e.message))
            logger.info("storeVideo FAIL res: $res")
            ResponseEntity(res, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("video/{id}/queued", produces = ["application/json"])
    fun toggleQueued(@PathVariable id: Long): ResponseEntity<String> {
        return try {
            val video = videoRepository.findById(id).get()
            val res = objectMapper.writeValueAsString(mapOf("res" to "success"))
            video.queued = !video.queued
            videoRepository.save(video)
            if (video.queued) {
                val downloadThread = Thread { videoQueue.downloadVideo() }
                downloadThread.start()
            }
            logger.info("toggleQueued SUCCESS res: $res")
            ResponseEntity(res, HttpStatus.OK)
        } catch (e: Exception) {
            val res = objectMapper.writeValueAsString(mapOf("res" to "fail", "message" to e.message))
            logger.info("toggleQueued FAIL res: $res")
            ResponseEntity(res, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("video/{id}", produces = ["application/json"])
    fun destroyVideo(@PathVariable id: Long): ResponseEntity<String> {
        return try {
            val res = objectMapper.writeValueAsString(mapOf("res" to "success"))
            videoRepository.deleteById(id)
            logger.info("destroyVideo SUCCESS res: $res")
            ResponseEntity(res, HttpStatus.OK)
        } catch (e: Exception) {
            val res = objectMapper.writeValueAsString(mapOf("res" to "fail", "message" to e.message))
            logger.info("destroyVideo FAIL res: $res")
            ResponseEntity(res, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("queue", produces = ["application/json"])
    fun showQueue(): ResponseEntity<String> {
        return try {
            val res = objectMapper.writeValueAsString(mapOf("active" to videoQueue.getQueue()))
            logger.info("showQueue SUCCESS res: $res")
            ResponseEntity(res, HttpStatus.OK)
        } catch (e: Exception) {
            val res = objectMapper.writeValueAsString(mapOf("res" to "fail", "message" to e.message))
            logger.info("showQueue FAIL res: $res")
            ResponseEntity(res, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("queue", produces = ["application/json"])
    fun toggleQueue(): ResponseEntity<String> {
        return try {
            val res = objectMapper.writeValueAsString(mapOf("active" to videoQueue.toggleQueue()))
            if (videoQueue.getQueue()) {
                val downloadThread = Thread { videoQueue.downloadVideo() }
                downloadThread.start()
            }
            logger.info("toggleQueue SUCCESS res: $res")
            ResponseEntity(res, HttpStatus.OK)
        } catch (e: Exception) {
            val res = objectMapper.writeValueAsString(mapOf("res" to "fail", "message" to e.message))
            logger.info("toggleQueue FAIL res: $res")
            ResponseEntity(res, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
