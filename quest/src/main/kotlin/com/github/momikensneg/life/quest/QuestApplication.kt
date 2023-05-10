package com.github.momikensneg.life.quest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.reactive.config.EnableWebFlux
import reactor.tools.agent.ReactorDebugAgent

@SpringBootApplication
@EnableWebFlux
@EnableR2dbcRepositories
@EnableEurekaClient
@EnableScheduling
class QuestApplication
fun main(args: Array<String>) {
	ReactorDebugAgent.init()
	runApplication<QuestApplication>(*args)
}
