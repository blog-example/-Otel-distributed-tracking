package com.example.servera.application.service.etc

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class HelloService {
    private val log = LoggerFactory.getLogger(HelloService::class.java)

    fun getHello(): Map<String, String> {
        log.info("HelloService.getHello() started")
        val result = mapOf(
            "message" to "Hello from Server A!",
            "server" to "server-a",
            "port" to "8081"
        )
        log.info("HelloService.getHello() finished")
        return result
    }
}
