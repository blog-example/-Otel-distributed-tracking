package com.example.gateway.infrastructure.filter

import io.opentelemetry.api.trace.Span
import io.opentelemetry.context.Context
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class LoggingFilter : GlobalFilter, Ordered {

    private val log = LoggerFactory.getLogger(LoggingFilter::class.java)

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val request = exchange.request
        val method = request.method
        val path = request.uri.path

        val span = Span.fromContext(Context.current())
        val spanContext = span.spanContext
        val traceparent = "00-${spanContext.traceId}-${spanContext.spanId}-${spanContext.traceFlags.asHex()}"

        val mutatedRequest = request.mutate()
            .header("traceparent", traceparent)
            .build()
        val mutatedExchange = exchange.mutate().request(mutatedRequest).build()

        return chain.filter(mutatedExchange).then(Mono.fromRunnable {
            val route = mutatedExchange.getAttribute<Route>(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR)
            val routeId = route?.id ?: "unknown"
            val routeUri = route?.uri?.toString() ?: "unknown"
            val statusCode = mutatedExchange.response.statusCode?.value() ?: 0

            log.info("Route: {} {} -> [{}] {} (status: {})", method, path, routeId, routeUri, statusCode)
        })
    }

    override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE
}
