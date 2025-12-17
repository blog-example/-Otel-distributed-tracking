package com.example.serverbb.infrastructure.kafka.config

import com.example.serverbb.infrastructure.kafka.message.AirQualityPayload
import com.example.serverbb.infrastructure.kafka.message.Message
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.util.backoff.FixedBackOff

@Configuration
class KafkaConsumerConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String,

    @Value("\${spring.kafka.consumer.group-id}")
    private val groupId: String,

    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(KafkaConsumerConfig::class.java)

    @Bean
    fun consumerFactory(): ConsumerFactory<String, Message<AirQualityPayload>> {
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
            ConsumerConfig.GROUP_ID_CONFIG to groupId,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest"
        )
        return DefaultKafkaConsumerFactory(props, StringDeserializer(), jsonDeserializer)
    }

    @Bean
    fun kafkaErrorHandler(dltKafkaTemplate: KafkaTemplate<String, String>): DefaultErrorHandler {
        val recoverer = DeadLetterPublishingRecoverer(dltKafkaTemplate) { record, exception ->
            log.error("Sending to DLT - topic: ${record.topic()}, value: ${record.value()}, error: ${exception.message}")
            org.apache.kafka.common.TopicPartition("${record.topic()}.DLT", record.partition())
        }
        val errorHandler = DefaultErrorHandler(recoverer, FixedBackOff(1000L, 1))
        return errorHandler
    }

    @Bean
    fun kafkaListenerContainerFactory(
        kafkaErrorHandler: DefaultErrorHandler
    ): ConcurrentKafkaListenerContainerFactory<String, Message<AirQualityPayload>> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Message<AirQualityPayload>>()
        factory.consumerFactory = consumerFactory()
        factory.setCommonErrorHandler(kafkaErrorHandler)
        return factory
    }
}
