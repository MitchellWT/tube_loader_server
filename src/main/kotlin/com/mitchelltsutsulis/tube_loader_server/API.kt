package com.mitchelltsutsulis.tube_loader_server

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mitchelltsutsulis.tube_loader_server.storage.Video
import com.mitchelltsutsulis.tube_loader_server.storage.VideoRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class API(val videoRepository: VideoRepository) {
    val objectMapper = jacksonObjectMapper()

    @GetMapping("videos", produces=["application/json"])
    fun showVideos(@RequestParam(defaultValue="10") amount: Int, @RequestParam(defaultValue="0") page: Int): ResponseEntity<String> {
        return try {
            val videos = videoRepository.findAll(PageRequest.of(page, amount, Sort.by("id")))
            val res = objectMapper.writeValueAsString(videos.toList())
            ResponseEntity(res, HttpStatus.OK)
        } catch (e: Exception) {
            val res = objectMapper.writeValueAsString(mapOf("res" to "fail", "message" to e.message))
            ResponseEntity(res, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("video/{id}", produces=["application/json"])
    fun showVideo(@PathVariable id: Long): ResponseEntity<String> {
        return try {
            val video = videoRepository.findById(id).get()
            val res = objectMapper.writeValueAsString(video)
            ResponseEntity(res, HttpStatus.OK)
        } catch (e: Exception) {
            val res = objectMapper.writeValueAsString(mapOf("res" to "fail", "message" to e.message))
            ResponseEntity(res, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("video", produces=["application/json"])
    fun storeVideo(@RequestBody videoJSON: String): ResponseEntity<String> {
        return try {
            val video = objectMapper.readValue(videoJSON, Video::class.java)
            val res = objectMapper.writeValueAsString(mapOf("res" to "success"))
            videoRepository.save(video)
            ResponseEntity(res, HttpStatus.OK)
        } catch (e: Exception) {
            val res = objectMapper.writeValueAsString(mapOf("res" to "fail", "message" to e.message))
            ResponseEntity(res, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("video/{id}/queued", produces=["application/json"])
    fun toggleQueued(@PathVariable id: Long): ResponseEntity<String> {
        return try {
            val video = videoRepository.findById(id).get()
            val res = objectMapper.writeValueAsString(mapOf("res" to "success"))
            video.queued = !video.queued
            videoRepository.save(video)
            ResponseEntity(res, HttpStatus.OK)
        } catch (e: Exception) {
            val res = objectMapper.writeValueAsString(mapOf("res" to "fail", "message" to e.message))
            ResponseEntity(res, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("video/{id}", produces=["application/json"])
    fun destroyVideo(@PathVariable id: Long): ResponseEntity<String> {
        return try {
            val res = objectMapper.writeValueAsString(mapOf("res" to "success"))
            videoRepository.deleteById(id)
            ResponseEntity(res, HttpStatus.OK)
        } catch (e: Exception) {
            val res = objectMapper.writeValueAsString(mapOf("res" to "fail", "message" to e.message))
            ResponseEntity(res, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("queue")
    fun showQueue(): Map<String, String> {
        return mapOf()
    }

    @PutMapping("queue")
    fun toggleQueue(): Map<String, String> {
        return mapOf()
    }
}
