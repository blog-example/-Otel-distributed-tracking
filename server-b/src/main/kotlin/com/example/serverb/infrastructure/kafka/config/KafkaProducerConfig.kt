package com.example.serverb.infrastructure.kafka.config

import com.example.serverb.infrastructure.kafka.message.AirQualityPayload
import com.example.serverb.infrastructure.kafka.message.Message
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.serializer.JsonSerializer
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions

@Configuration
class KafkaProducerConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String,

    private val objectMapper: ObjectMapper
) {

    @Bean
    fun kafkaSender(): KafkaSender<String, Message<AirQualityPayload>> {
        val props = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
            ProducerConfig.RETRIES_CONFIG to 1,
            ProducerConfig.RETRY_BACKOFF_MS_CONFIG to 1000
        )

        val senderOptions = SenderOptions.create<String, Message<AirQualityPayload>>(props)
            .withValueSerializer(JsonSerializer(objectMapper))

        return KafkaSender.create(senderOptions)
    }
}
