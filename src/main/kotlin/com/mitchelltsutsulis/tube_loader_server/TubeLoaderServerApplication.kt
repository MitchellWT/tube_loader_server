package com.mitchelltsutsulis.tube_loader_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TubeLoaderServerApplication

fun main(args: Array<String>) {
	runApplication<TubeLoaderServerApplication>(*args)
}
