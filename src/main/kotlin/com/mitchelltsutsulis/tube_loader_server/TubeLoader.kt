package com.mitchelltsutsulis.tube_loader_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableScheduling
@EnableWebSecurity
@SpringBootApplication
class TubeLoader

fun main(args: Array<String>) {
    runApplication<TubeLoader>(*args)
}
