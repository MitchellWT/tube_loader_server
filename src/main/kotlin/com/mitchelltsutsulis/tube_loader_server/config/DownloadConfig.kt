package com.mitchelltsutsulis.tube_loader_server

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "download")
data class DownloadConfig(
    var videoStorage: String = "/opt/tube_loader/videos/",
    var downloadCron: String =  "30 * * * * *",
    var downloaderConfig: Array<String> = arrayOf()
)
