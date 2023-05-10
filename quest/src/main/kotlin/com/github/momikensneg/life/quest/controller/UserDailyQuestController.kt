package com.github.momikensneg.life.quest.controller

import com.github.momikensneg.life.quest.client.GatewayClient
import com.github.momikensneg.life.quest.dto.user_quest.userdailyquest.UserDailyQuestDto
import com.github.momikensneg.life.quest.dto.user_quest.userdailyquest.UserDailyQuestGetRequest
import com.github.momikensneg.life.quest.dto.user_quest.userdailyquest.UserDailyQuestResponseDto
import com.github.momikensneg.life.quest.dto.user_quest.userdailyquest.UserDailyQuestUpdateRequest
import com.github.momikensneg.life.quest.exception.ForbiddenException
import com.github.momikensneg.life.quest.mapper.user_quest.UserDailyQuestMapper
import com.github.momikensneg.life.quest.service.user_quest.UserDailyQuestService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/user_daily_quest")
class UserDailyQuestController(
    private val service: UserDailyQuestService,
    private val mapper: UserDailyQuestMapper,
    client: GatewayClient
) : SecuredController(client) {

    @GetMapping("/")
    fun getAllQuests(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @RequestBody request: UserDailyQuestGetRequest
    ): Flux<UserDailyQuestResponseDto> =
        getUsername(authHeader)
            .flatMapMany { user -> service.getUserQuests(user, request) }
            .map(mapper::convertDtoToResponse)

    @GetMapping("/{id}")
    fun getQuest(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @PathVariable id: UUID
    ): Mono<ResponseEntity<UserDailyQuestResponseDto>> =
        Mono.zip(getUsername(authHeader), service.findById(id))
            .filter { result ->
                result.t2.userName == result.t1
            }
            .switchIfEmpty(Mono.error(ForbiddenException("Another user's quest requested")))
            .map { result -> mapper.convertDtoToResponse(result.t2) }
            .map { u -> ResponseEntity.ok(u) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createQuest(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @RequestBody quest: UserDailyQuestDto
    ): Mono<ResponseEntity<UserDailyQuestResponseDto>> =
        getUsername(authHeader)
            .flatMap { user ->
                quest.userName = user
                service.create(quest)
            }
            .map(mapper::convertDtoToResponse)
            .map { u -> ResponseEntity.ok(u) }

    @PutMapping("/{id}")
    fun updateQuestById(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @PathVariable id: UUID,
        @RequestBody quest: UserDailyQuestUpdateRequest
    ): Mono<ResponseEntity<UserDailyQuestResponseDto>> =
        getUsername(authHeader)
            .flatMap { user ->
                val questDto = mapper.convertUpdateToDto(quest)
                questDto.userName = user
                service.update(id, questDto)
            }
            .map(mapper::convertDtoToResponse)
            .map { u -> ResponseEntity.ok(u) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @DeleteMapping("/{id}")
    fun deleteById(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @PathVariable id: UUID
    ): Mono<Void> =
        getUsername(authHeader)
            .flatMap { user -> service.deleteUsersQuest(user, id) }
}