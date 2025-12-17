package com.example.servera.infrastructure.web

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/api/a/air-quality")
interface AirQualityAPI {

    @PostMapping
    fun record(@RequestBody request: RecordRequest): RecordResponse

    data class RecordRequest(
        val longitude: Double,
        val latitude: Double,
        val pollutants: List<PollutantRequest>,
        val deviceId: String
    )

    data class PollutantRequest(
        val type: String,
        val value: Double
    )

    data class RecordResponse(
        val id: String
    )
}
