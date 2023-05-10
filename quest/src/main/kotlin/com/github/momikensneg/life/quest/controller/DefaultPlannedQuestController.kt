package com.github.momikensneg.life.quest.controller

import com.github.momikensneg.life.quest.client.GatewayClient
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultplannedquest.DefaultPlannedQuestDto
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultplannedquest.DefaultPlannedQuestResponseDto
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultplannedquest.DefaultPlannedQuestUpdateRequest
import com.github.momikensneg.life.quest.mapper.default_quest.DefaultPlannedQuestMapper
import com.github.momikensneg.life.quest.service.default_quest.DefaultPlannedQuestService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/default_planned_quest")
class DefaultPlannedQuestController(
    private val service: DefaultPlannedQuestService,
    private val mapper: DefaultPlannedQuestMapper,
    client: GatewayClient
): SecuredController(client) {

    @GetMapping("/")
    fun getAllQuests(
        @RequestParam(name = "name") name: String,
        @RequestParam(name = "page") page: Int
    ): Flux<DefaultPlannedQuestResponseDto> =
        service.listQuests(name, page)
            .map(mapper::convertDtoToResponse)

    @GetMapping("/{id}")
    fun getQuest(@PathVariable id: UUID): Mono<ResponseEntity<DefaultPlannedQuestResponseDto>> =
        service.findById(id)
            .map(mapper::convertDtoToResponse)
            .map { u -> ResponseEntity.ok(u) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createQuest(@RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
                    @RequestBody quest: DefaultPlannedQuestDto): Mono<ResponseEntity<DefaultPlannedQuestResponseDto>> =
        checkAdmin(authHeader)
            .flatMap { _ -> service.create(quest) }
            .map(mapper::convertDtoToResponse)
            .map { u -> ResponseEntity.ok(u) }

    @PutMapping("/{id}")
    fun updateQuestById(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @PathVariable id: UUID,
        @RequestBody quest: DefaultPlannedQuestUpdateRequest
    ): Mono<ResponseEntity<DefaultPlannedQuestResponseDto>> =
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