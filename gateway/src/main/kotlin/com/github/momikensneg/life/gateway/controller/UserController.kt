package com.github.momikensneg.life.gateway.controller

import com.github.momikensneg.life.gateway.dto.user.UserDto
import com.github.momikensneg.life.gateway.dto.user.UserResponseDto
import com.github.momikensneg.life.gateway.dto.user.UserUpdateRequest
import com.github.momikensneg.life.gateway.mapper.UserMapper
import com.github.momikensneg.life.gateway.services.UserService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@Tag(name = "Users")
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val mapper: UserMapper
) : SecuredController {

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: UUID): Mono<ResponseEntity<UserResponseDto>> =
        userService.findById(id)
            .map(mapper::convertDtoToResponse)
            .map { u -> ResponseEntity.ok(u) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @GetMapping
    fun listUsers(@RequestParam(name = "name") name: String): Flux<UserResponseDto> =
        userService.listUsers(name)
            .map(mapper::convertDtoToResponse)

    @PostMapping
    fun addNewUser(@RequestBody user: UserDto): Mono<ResponseEntity<UserResponseDto>> =
        userService.create(user)
            .map(mapper::convertDtoToResponse)
            .map { u -> ResponseEntity.ok(u) }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: UUID, @RequestBody user: UserUpdateRequest): Mono<ResponseEntity<UserResponseDto>> =
        userService.update(id, mapper.convertUpdateToDto(user))
            .map(mapper::convertDtoToResponse)
            .map { u -> ResponseEntity.ok(u) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: UUID): Mono<ResponseEntity<Void>> =
        userService.findById(id)
            .flatMap { u -> userService.delete(u).then(Mono.just(ResponseEntity<Void>(HttpStatus.OK))) }
            .defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))

}

