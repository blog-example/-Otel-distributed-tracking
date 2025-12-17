package com.example.serverbb.infrastructure.kafka

import com.example.serverbb.infrastructure.kafka.message.AirQualityPayload
import com.example.serverbb.infrastructure.kafka.message.Message
import org.apache.kafka.clients.consumer.ConsumerRecord

interface AirQualityListener {
    fun consume(record: ConsumerRecord<String, Message<AirQualityPayload>>)
}
