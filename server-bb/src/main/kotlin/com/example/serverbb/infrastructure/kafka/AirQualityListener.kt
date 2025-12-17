package com.example.serverbb.infrastructure.kafka

import com.example.serverbb.infrastructure.kafka.message.AirQualityPayload
import com.example.serverbb.infrastructure.kafka.message.Message

interface AirQualityListener {
    fun consume(message: Message<AirQualityPayload>)
}
