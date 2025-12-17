package com.example.serverbb.infrastructure.kafka

import com.example.serverbb.application.service.airquality.AirQualityService
import com.example.serverbb.infrastructure.kafka.message.AirQualityPayload
import com.example.serverbb.infrastructure.kafka.message.Message
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.context.Context
import io.opentelemetry.context.propagation.TextMapGetter
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.header.Headers
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class AirQualityListenerImpl(
    private val airQualityService: AirQualityService,
    private val openTelemetry: OpenTelemetry
) : AirQualityListener {

    private val log = LoggerFactory.getLogger(AirQualityListenerImpl::class.java)

    @KafkaListener(
        topics = ["\${kafka.topic.air-quality}"],
        containerFactory = "kafkaListenerContainerFactory"
    )
    override fun consume(record: ConsumerRecord<String, Message<AirQualityPayload>>) {
        // 받은 헤더 확인
        val headers = record.headers().map { "${it.key()}=${String(it.value())}" }
        log.info("Received headers: $headers")

        // Kafka 헤더에서 trace context 추출
        val propagator = openTelemetry.propagators.textMapPropagator
        val extractedContext = propagator.extract(Context.current(), record.headers(), HeadersGetter)
        log.info("Extracted context: $extractedContext")

        // 추출한 context를 현재 스레드에 적용하고 처리
        extractedContext.makeCurrent().use {
            processMessage(record.value())
        }
    }

    private object HeadersGetter : TextMapGetter<Headers> {
        override fun keys(carrier: Headers): Iterable<String> =
            carrier.map { it.key() }

        override fun get(carrier: Headers?, key: String): String? =
            carrier?.lastHeader(key)?.value()?.let { String(it) }
    }

    private fun processMessage(message: Message<AirQualityPayload>) {
        log.info("Processing air quality message: messageId=${message.messageId}, deviceId=${message.payload.deviceId}")

        val payload = message.payload
        val recordPayload = AirQualityService.RecordPayload(
            longitude = payload.longitude,
            latitude = payload.latitude,
            pollutants = payload.pollutants.map {
                AirQualityService.PollutantPayload(it.type, it.value)
            },
            deviceId = payload.deviceId
        )

        val result = airQualityService.record(recordPayload)
        log.info("Saved air quality log: id=${result.id}, correlationId=${message.correlationId}")
    }
}
