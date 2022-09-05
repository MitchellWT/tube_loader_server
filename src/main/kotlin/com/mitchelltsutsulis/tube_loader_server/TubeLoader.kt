package com.mitchelltsutsulis.tube_loader_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class TubeLoader

fun main(args: Array<String>) {
    runApplication<TubeLoader>(*args)
}
