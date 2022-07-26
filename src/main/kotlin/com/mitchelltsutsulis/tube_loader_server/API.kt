package com.mitchelltsutsulis.tube_loader_server

import org.springframework.web.bind.annotation.*

@RestController
class API {
    @GetMapping("videos")
    fun showVideos() {

    }

    @GetMapping("video/{id}")
    fun showVideo() {

    }

    @PostMapping("video")
    fun storeVideo() {

    }

    @PutMapping("video/{id}/queued")
    fun toggleQueued() {

    }

    @DeleteMapping("video/{id}")
    fun destroyVideo() {

    }

    @GetMapping("queue")
    fun showQueue() {

    }

    @PutMapping("queue")
    fun toggleQueue() {

    }
}
