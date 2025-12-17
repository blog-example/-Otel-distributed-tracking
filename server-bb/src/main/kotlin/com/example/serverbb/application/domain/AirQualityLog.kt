package com.example.serverbb.application.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "air_quality_logs")
data class AirQualityLog(
    @Id
    val id: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val location: GeoJsonPoint,
    val pollutants: List<Pollutant>,
    val deviceId: String
)
