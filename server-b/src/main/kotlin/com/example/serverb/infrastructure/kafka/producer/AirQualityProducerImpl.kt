package com.example.serverb.infrastructure.kafka.producer

import com.example.serverb.infrastructure.kafka.message.AirQualityPayload
import com.example.serverb.infrastructure.kafka.message.Message
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.context.Context
import io.opentelemetry.context.propagation.TextMapSetter
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Headers
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord

@Component
class AirQualityProducerImpl(
    private val kafkaSender: KafkaSender<String, Message<AirQualityPayload>>,
    private val openTelemetry: OpenTelemetry,

    @Value("\${kafka.topic.air-quality}")
    private val topic: String
) : AirQualityProducer {

    private val log = LoggerFactory.getLogger(AirQualityProducerImpl::class.java)

    override fun send(message: Message<AirQualityPayload>): Mono<Unit> {
        log.info("send start: messageId=${message.messageId}, topic=$topic")

        val senderRecord = createSenderRecord(message)

        return kafkaSender.send(Mono.just(senderRecord))
            .next()
            .doOnSuccess { log.info("send end: messageId=${message.messageId}") }
            .doOnError { e -> log.error("send failed: messageId=${message.messageId}, error=${e.message}") }
            .then(Mono.just(Unit))
    }

    private fun createSenderRecord(message: Message<AirQualityPayload>): SenderRecord<String, Message<AirQualityPayload>, String> {
        val producerRecord = ProducerRecord(topic, message.messageId, message)

        // Reactor Kafka는 자동 계측이 안되므로 수동으로 trace context 주입
        val context = Context.current()
        log.info("current context: $context")

        val propagator = openTelemetry.propagators.textMapPropagator
        log.info("Propagator: $propagator, fields: ${propagator.fields()}")
        propagator.inject(context, producerRecord.headers(), HeadersSetter)

        val injectedHeaders = producerRecord.headers().map { "${it.key()}=${String(it.value())}" }
        log.info("Injected headers: $injectedHeaders")

        return SenderRecord.create(producerRecord, message.messageId)
    }

    private object HeadersSetter : TextMapSetter<Headers> {
        override fun set(carrier: Headers?, key: String, value: String) {
            carrier?.add(key, value.toByteArray())
        }
    }
}
