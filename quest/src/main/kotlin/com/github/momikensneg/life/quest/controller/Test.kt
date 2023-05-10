package com.github.momikensneg.life.quest.controller

import com.github.momikensneg.life.quest.client.GatewayClient
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.*

@RestController
class Test(val client: GatewayClient) {

    @GetMapping("/test")
    fun test(@RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String): Mono<ResponseEntity<Boolean>> {
        val name = client.isAdmin(authHeader)
        return name.map { n -> ResponseEntity.ok(n) }
    }
}