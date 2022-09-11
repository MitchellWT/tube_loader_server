package com.mitchelltsutsulis.tube_loader_server.storage

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface VideoRepository : PagingAndSortingRepository<Video, Long> {
    @Query(
        value = "SELECT * FROM video WHERE queued IS true AND downloaded IS false ORDER BY id LIMIT 1",
        nativeQuery = true
    )
    fun findFirstInQueue(): Optional<Video>
    @Transactional
    @Modifying
    @Query(
        value = "UPDATE video SET downloaded = true, queued = false, downloaded_at = NOW() WHERE id = :id",
        nativeQuery = true
    )
    fun removeFromQueue(@Param("id") id: Long)
    @Query(
        value = "SELECT * FROM video WHERE queued IS false AND downloaded IS true",
        nativeQuery = true
    )
    fun findAllDownloaded(pageable: Pageable): List<Video>
    @Query(
        value = "SELECT * FROM video WHERE downloaded IS false",
        nativeQuery = true
    )
    fun findAllNotDownloaded(pageable: Pageable): List<Video>
}
