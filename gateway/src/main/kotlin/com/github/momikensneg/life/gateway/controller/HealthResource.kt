package com.github.momikensneg.life.gateway.controller

import com.github.momikensneg.life.gateway.dto.Health
import org.springframework.boot.actuate.health.Status
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthResource {

    @GetMapping("/health")
    fun getHealth(): ResponseEntity<Health> =
        ResponseEntity.ok().body(Health(Status.UP))

}