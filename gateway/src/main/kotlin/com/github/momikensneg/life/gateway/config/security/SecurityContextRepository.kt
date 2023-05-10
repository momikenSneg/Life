package com.github.momikensneg.life.gateway.config.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository(
    val authenticationManager: AuthenticationManager,
    @Value("\${spring-webflux-jjwt.auth-prefix}")
    private val authPrefix: String
) : ServerSecurityContextRepository {

    override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        return Mono.justOrEmpty(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
            .filter { authHeader -> authHeader.startsWith("$authPrefix ") }
            .flatMap { authHeader ->
                val authToken = authHeader.substring(7)
                val auth: Authentication = UsernamePasswordAuthenticationToken(authToken, authToken)
                this.authenticationManager.authenticate(auth).map(::SecurityContextImpl)
            }
    }
}