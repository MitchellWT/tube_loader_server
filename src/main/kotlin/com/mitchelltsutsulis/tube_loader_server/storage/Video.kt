package com.mitchelltsutsulis.tube_loader_server.storage

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "video")
open class Video(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,
    @Column(nullable = false)
    open var downloaded: Boolean = false,
    @Column(nullable = false)
    open var queued: Boolean = true,
    @JsonProperty("video_id")
    @Column(nullable = false, name = "video_id")
    open var videoId: String,
    @Column(nullable = false)
    open var title: String,
    @Column(nullable = false)
    open var thumbnail: String,
    @Column(name = "downloaded_at")
    open var downloadedAt: Timestamp? = null
)
