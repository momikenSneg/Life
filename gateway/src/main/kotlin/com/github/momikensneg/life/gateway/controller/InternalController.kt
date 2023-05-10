package com.github.momikensneg.life.gateway.controller

import com.github.momikensneg.life.gateway.config.security.JWTUtil
import com.github.momikensneg.life.gateway.dao.enums.RoleName
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/internal")
class InternalController(
    val jwtUtil: JWTUtil,
    @Value("\${spring-webflux-jjwt.role}")
    private val role: String
) {

    companion object {
        const val AUTH_PREF = "Bearer "
    }

    @GetMapping("/user_details")
    fun getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String): Mono<String> {
        //TODO: add validation
        return Mono.just(jwtUtil.getUsernameFromToken(authHeader.replace(AUTH_PREF, "")))
    }

    @GetMapping("/is_admin")
    fun isAdMin(@RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String): Mono<Boolean> {
        //TODO: add validation
        val roles = jwtUtil.getAllClaimsFromToken(authHeader.replace(AUTH_PREF, ""))[role]
        if (roles !is List<*>) {
            throw RuntimeException()
        }
        return Mono.just(RoleName.ROLE_ADMIN.name == roles[0])
    }
}