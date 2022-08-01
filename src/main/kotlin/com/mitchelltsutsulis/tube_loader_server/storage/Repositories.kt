package com.mitchelltsutsulis.tube_loader_server.storage

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface VideoRepository: PagingAndSortingRepository<Video, Long>
