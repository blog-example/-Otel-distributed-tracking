package com.example.serverb.infrastructure.kafka.producer

import com.example.serverb.infrastructure.kafka.message.AirQualityPayload
import com.example.serverb.infrastructure.kafka.message.Message
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord

@Component
class AirQualityProducerImpl(
    private val kafkaSender: KafkaSender<String, Message<AirQualityPayload>>,

    @Value("\${kafka.topic.air-quality}")
    private val topic: String
) : AirQualityProducer {

    private val log = LoggerFactory.getLogger(AirQualityProducerImpl::class.java)

    override fun send(message: Message<AirQualityPayload>): Mono<Unit> {
        log.info("send start: messageId=${message.messageId}, topic=$topic")

        val producerRecord = ProducerRecord(topic, message.messageId, message)
        val senderRecord = SenderRecord.create(producerRecord, message.messageId)

        return kafkaSender.send(Mono.just(senderRecord))
            .next()
            .doOnSuccess { log.info("send end: messageId=${message.messageId}") }
            .doOnError { e -> log.error("send failed: messageId=${message.messageId}, error=${e.message}") }
            .then(Mono.just(Unit))
    }
}
