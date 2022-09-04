package com.mitchelltsutsulis.tube_loader_server.storage

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
        value = "SELECT * FROM video WHERE queued IS true AND downloaded_at IS NULL ORDER BY id LIMIT 1",
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
}
