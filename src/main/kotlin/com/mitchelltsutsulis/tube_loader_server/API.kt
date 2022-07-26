package com.mitchelltsutsulis.tube_loader_server

import org.springframework.web.bind.annotation.*

@RestController
class API {
    @GetMapping("videos")
    fun showVideos(@RequestParam amount: Int, @RequestParam offset: Int): Map<String, String> {
        return mapOf(Pair("amount", amount.toString()), Pair("offset", offset.toString()))
    }

    @GetMapping("video/{id}")
    fun showVideo(@PathVariable id: Int): Map<String, String> {
        return mapOf(Pair("id", id.toString()))
    }

    @PostMapping("video")
    fun storeVideo(@RequestBody videoJSON: String): Map<String, String> {
        return mapOf(Pair("videoJSON", videoJSON))
    }

    @PutMapping("video/{id}/queued")
    fun toggleQueued(@PathVariable id: Int): Map<String, String> {
        return mapOf(Pair("id", id.toString()))
    }

    @DeleteMapping("video/{id}")
    fun destroyVideo(@PathVariable id: Int): Map<String, String> {
        return mapOf(Pair("id", id.toString()))
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
