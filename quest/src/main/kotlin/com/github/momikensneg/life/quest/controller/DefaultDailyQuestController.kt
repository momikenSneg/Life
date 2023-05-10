package com.github.momikensneg.life.quest.controller

import com.github.momikensneg.life.quest.client.GatewayClient
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultdailyquest.DefaultDailyQuestDto
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultdailyquest.DefaultDailyQuestResponseDto
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultdailyquest.DefaultDailyQuestUpdateRequest
import com.github.momikensneg.life.quest.mapper.default_quest.DefaultDailyQuestMapper
import com.github.momikensneg.life.quest.service.default_quest.DefaultDailyQuestService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/default_daily_quest")
class DefaultDailyQuestController(
    private val service: DefaultDailyQuestService,
    private val mapper: DefaultDailyQuestMapper,
    client: GatewayClient
): SecuredController(client) {

    @GetMapping("/")
    fun getAllQuests(
        @RequestParam(name = "name") name: String,
        @RequestParam(name = "page") page: Int
    ): Flux<DefaultDailyQuestResponseDto> =
        service.listQuests(name, page)
            .map(mapper::convertDtoToResponse)

    @GetMapping("/{id}")
    fun getQuest(@PathVariable id: UUID): Mono<ResponseEntity<DefaultDailyQuestResponseDto>> =
        service.findById(id)
            .map(mapper::convertDtoToResponse)
            .map { u -> ResponseEntity.ok(u) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createQuest(@RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
                    @RequestBody quest: DefaultDailyQuestDto): Mono<ResponseEntity<DefaultDailyQuestResponseDto>> =
        checkAdmin(authHeader)
            .flatMap { _ -> service.create(quest) }
            .map(mapper::convertDtoToResponse)
            .map { u -> ResponseEntity.ok(u) }

    @PutMapping("/{id}")
    fun updateQuestById(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @PathVariable id: UUID,
        @RequestBody quest: DefaultDailyQuestUpdateRequest
    ): Mono<ResponseEntity<DefaultDailyQuestResponseDto>> =
        checkAdmin(authHeader)
            .flatMap { _ -> service.update(id, mapper.convertUpdateToDto(quest)) }
            .map(mapper::convertDtoToResponse)
            .map { u -> ResponseEntity.ok(u) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @DeleteMapping("/{id}")
    fun deleteById(@RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
                   @PathVariable id: UUID): Mono<Void> =
        checkAdmin(authHeader)
            .flatMap { _ -> service.delete(id)}

}