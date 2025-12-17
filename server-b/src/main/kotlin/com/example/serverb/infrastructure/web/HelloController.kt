package com.example.serverb.infrastructure.web

import com.example.serverb.application.service.etc.HelloService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class HelloController(
    private val helloService: HelloService
) {
    private val log = LoggerFactory.getLogger(HelloController::class.java)

    @GetMapping("/api/b/hello")
    fun hello(): Mono<Map<String, String>> {
        log.info("hello start")
        return Mono.fromCallable { helloService.getHello() }
            .doOnSuccess { log.info("hello end") }
    }
}
