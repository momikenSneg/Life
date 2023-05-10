package com.github.momikensneg.life.gateway.config.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.stream.Collectors

@Component
class AuthenticationManager(
    private val jwtUtil: JWTUtil,
    @Value("\${spring-webflux-jjwt.role}")
    private val role: String
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        val authToken = authentication?.credentials.toString()
        val username = jwtUtil.getUsernameFromToken(authToken)
        return Mono.just(jwtUtil.validateToken(authToken))
            .filter { valid -> valid }
            .switchIfEmpty(Mono.empty())
            .map {
                val claims = jwtUtil.getAllClaimsFromToken(authToken)
                val rolesMap: List<String> = claims.get(role, List::class.java) as List<String>
                UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    rolesMap.stream().map(::SimpleGrantedAuthority).collect(Collectors.toList()))
            }
    }
}