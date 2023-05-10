package com.github.momikensneg.life.gateway.controller

import com.github.momikensneg.life.gateway.config.security.JWTUtil
import com.github.momikensneg.life.gateway.config.security.PBKDF2Encoder
import com.github.momikensneg.life.gateway.dto.api.AuthRequest
import com.github.momikensneg.life.gateway.dto.api.AuthResponse
import com.github.momikensneg.life.gateway.mapper.UserMapper
import com.github.momikensneg.life.gateway.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class AuthenticationController(
    private val jwtUtil: JWTUtil,
    private val passwordEncoder: PBKDF2Encoder,
    private val userService: UserService,
    private val userMapper: UserMapper
) {

    @PostMapping("/login")
    fun login(@RequestBody ar: AuthRequest): Mono<ResponseEntity<AuthResponse>> =
        userService.findByUsername(ar.login)
            .filter { user -> passwordEncoder.encode(ar.password) == user.password }
            .map { user -> ResponseEntity.ok(AuthResponse(jwtUtil.generateToken(user.login, user.roleName!!.name))) }
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))


    @PostMapping("/signup")
    fun signup(@RequestBody ar: AuthRequest): Mono<ResponseEntity<AuthResponse>> {
        return userService.create(userMapper.convertAuthToDto(ar))
            .map { user -> ResponseEntity.ok(AuthResponse(jwtUtil.generateToken(user.login, user.roleName!!.name))) }
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
    }

}