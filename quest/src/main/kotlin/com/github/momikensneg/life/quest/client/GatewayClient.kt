package com.github.momikensneg.life.quest.client

import com.github.momikensneg.life.quest.exception.UserNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class GatewayClient(
    @Value("\${client.gateway.url}")
    private val url: String,
) {

    private val client = WebClient.builder()
        .baseUrl(url)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

    fun getUserName(authHeader: String): Mono<String> =
        client.defaultHeader(HttpHeaders.AUTHORIZATION, authHeader).build()
            .get()
            .uri("/internal/user_details")
            .exchangeToMono { response ->
                if (response.statusCode() == HttpStatus.OK) {
                    response.bodyToMono(String::class.java)
                } else {
                    Mono.error(UserNotFoundException("Error response"))
                }
            }

    fun isAdmin(authHeader: String): Mono<Boolean> =
        client.defaultHeader(HttpHeaders.AUTHORIZATION, authHeader).build()
            .get()
            .uri("/internal/is_admin")
            .exchangeToMono { response ->
                if (response.statusCode() == HttpStatus.OK) {
                    response.bodyToMono(Boolean::class.java)
                } else {
                    Mono.error(UserNotFoundException("Error response"))
                }
            }
}