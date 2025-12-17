package com.example.serverb.infrastructure.web

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono

@RequestMapping("/api/b/air-quality")
interface AirQualityAPI {

    @PostMapping
    fun record(@RequestBody request: RecordRequest): Mono<RecordResponse>

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
