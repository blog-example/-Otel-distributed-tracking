package com.example.serverbb.infrastructure.kafka.message

data class AirQualityPayload(
    val longitude: Double,
    val latitude: Double,
    val pollutants: List<PollutantPayload>,
    val deviceId: String
)

data class PollutantPayload(
    val type: String,
    val value: Double
)
