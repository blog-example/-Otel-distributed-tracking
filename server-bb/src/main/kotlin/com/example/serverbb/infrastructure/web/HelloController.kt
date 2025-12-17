package com.example.serverbb.infrastructure.web

import com.example.serverbb.application.service.etc.HelloService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController(
    private val helloService: HelloService
) {
    private val log = LoggerFactory.getLogger(HelloController::class.java)

    @GetMapping("/api/bb/hello")
    fun hello(): Map<String, String> {
        log.info("HelloController.hello() started")
        val result = helloService.getHello()
        log.info("HelloController.hello() finished")
        return result
    }
}
