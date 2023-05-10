package com.github.momikensneg.life_mic_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication
@EnableEurekaServer
class LifeMicServerApplication

fun main(args: Array<String>) {
    runApplication<LifeMicServerApplication>(*args)
}
