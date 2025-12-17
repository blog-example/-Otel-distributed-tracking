package com.example.serverbb.infrastructure.kafka.config

import com.example.serverbb.infrastructure.kafka.message.AirQualityPayload
import com.example.serverbb.infrastructure.kafka.message.Message
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class KafkaDltConsumerConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String,

    @Value("\${spring.kafka.consumer.group-id}")
    private val groupId: String,

    private val objectMapper: ObjectMapper
) {

    @Bean
    fun dltConsumerFactory(): ConsumerFactory<String, Message<AirQualityPayload>> {
        val jsonDeserializer = JsonDeserializer<Message<AirQualityPayload>>(
            objectMapper.typeFactory.constructParametricType(
                Message::class.java,
                AirQualityPayload::class.java
            ),
            objectMapper,
            false
        )

        val props = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG to "$groupId-dlt",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest"
        )
        return DefaultKafkaConsumerFactory(props, StringDeserializer(), jsonDeserializer)
    }

    @Bean
    fun dltKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Message<AirQualityPayload>> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Message<AirQualityPayload>>()
        factory.consumerFactory = dltConsumerFactory()
        return factory
    }
}
