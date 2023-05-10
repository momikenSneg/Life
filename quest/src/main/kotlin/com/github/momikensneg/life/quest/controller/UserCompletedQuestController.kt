package com.github.momikensneg.life.quest.controller

import com.github.momikensneg.life.quest.client.GatewayClient
import com.github.momikensneg.life.quest.dto.user_quest.usercompletedquest.UserCompletedQuestDto
import com.github.momikensneg.life.quest.dto.user_quest.usercompletedquest.UserCompletedQuestGetRequest
import com.github.momikensneg.life.quest.dto.user_quest.usercompletedquest.UserCompletedQuestResponseDto
import com.github.momikensneg.life.quest.exception.ForbiddenException
import com.github.momikensneg.life.quest.mapper.user_quest.UserCompletedQuestMapper
import com.github.momikensneg.life.quest.service.user_quest.UserCompletedQuestService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/user_completed_quest")
class UserCompletedQuestController(
    private val service: UserCompletedQuestService,
    private val mapper: UserCompletedQuestMapper,
    client: GatewayClient
): SecuredController(client)  {

    @GetMapping("/")
    fun getAllQuests(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @RequestBody request: UserCompletedQuestGetRequest
    ): Flux<UserCompletedQuestResponseDto> =
        getUsername(authHeader)
            .flatMapMany { user -> service.getUserQuests(user, request) }
            .map(mapper::convertDtoToResponse)

    @GetMapping("/{id}")
    fun getQuest(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @PathVariable id: UUID
    ): Mono<ResponseEntity<UserCompletedQuestResponseDto>> =
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
        @RequestBody quest: UserCompletedQuestDto
    ): Mono<ResponseEntity<UserCompletedQuestResponseDto>> =
        getUsername(authHeader)
            .flatMap { user ->
                quest.userName = user
                service.create(quest)
            }
            .map(mapper::convertDtoToResponse)
            .map { u -> ResponseEntity.ok(u) }
}