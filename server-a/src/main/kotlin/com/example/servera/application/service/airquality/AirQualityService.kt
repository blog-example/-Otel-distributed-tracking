package com.example.servera.application.service.airquality

interface AirQualityService {

    fun record(payload: RecordPayload): RecordResult

    data class RecordPayload(
        val longitude: Double,
        val latitude: Double,
        val pollutants: List<PollutantPayload>,
        val deviceId: String
    )

    data class PollutantPayload(
        val type: String,
        val value: Double
    )

    data class RecordResult(
        val id: String
    )
}
