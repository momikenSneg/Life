package com.github.momikensneg.life.gateway.services

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.momikensneg.life.gateway.dto.DamageMessage
import com.github.momikensneg.life.gateway.repository.UserRepository
import org.apache.kafka.common.requests.FetchMetadata.log
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class DamageMessageConsumerService(
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository
) {

    @KafkaListener(id = "damage", topics = ["server.damage"], containerFactory = "singleFactory")
    fun consume(dto: DamageMessage) {
        log.info("=> consumed {}", writeValueAsString(dto))
        userRepository.findByLogin(dto.user)
            .flatMap { user ->
                user.health = user.health - dto.damage
                userRepository.update(user)
            }
            .subscribe()
    }

    private fun writeValueAsString (dto: DamageMessage): String {
        try {
            return objectMapper.writeValueAsString(dto)
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
            throw RuntimeException("Writing value to JSON failed: $dto")
        }
    }
}