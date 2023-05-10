package com.github.momikensneg.life.quest.controller

import com.github.momikensneg.life.quest.client.GatewayClient
import com.github.momikensneg.life.quest.dto.user_quest.userplannedquest.UserPlannedQuestDto
import com.github.momikensneg.life.quest.dto.user_quest.userplannedquest.UserPlannedQuestGetRequest
import com.github.momikensneg.life.quest.dto.user_quest.userplannedquest.UserPlannedQuestResponseDto
import com.github.momikensneg.life.quest.dto.user_quest.userplannedquest.UserPlannedQuestUpdateRequest
import com.github.momikensneg.life.quest.exception.ForbiddenException
import com.github.momikensneg.life.quest.mapper.user_quest.UserPlannedQuestMapper
import com.github.momikensneg.life.quest.service.user_quest.UserPlannedQuestService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/user_planned_quest")
class UserPlannedQuestController(
    private val service: UserPlannedQuestService,
    private val mapper: UserPlannedQuestMapper,
    client: GatewayClient
): SecuredController(client) {

    @GetMapping("/")
    fun getAllQuests(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @RequestBody request: UserPlannedQuestGetRequest
    ): Flux<UserPlannedQuestResponseDto> =
        getUsername(authHeader)
            .flatMapMany { user -> service.getUserQuests(user, request) }
            .map(mapper::convertDtoToResponse)

    @GetMapping("/{id}")
    fun getQuest(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @PathVariable id: UUID
    ): Mono<ResponseEntity<UserPlannedQuestResponseDto>> =
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
        @RequestBody quest: UserPlannedQuestDto
    ): Mono<ResponseEntity<UserPlannedQuestResponseDto>> =
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
        @RequestBody quest: UserPlannedQuestUpdateRequest
    ): Mono<ResponseEntity<UserPlannedQuestResponseDto>> =
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