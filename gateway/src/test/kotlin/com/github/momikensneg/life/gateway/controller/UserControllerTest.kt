package com.github.momikensneg.life.gateway.controller

import com.github.momikensneg.life.gateway.PostgresTestContainer
import com.github.momikensneg.life.gateway.dao.User
import com.github.momikensneg.life.gateway.dao.enums.PetState
import com.github.momikensneg.life.gateway.dao.enums.RoleName
import org.junit.ClassRule
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.shaded.com.google.common.net.HttpHeaders
import reactor.core.publisher.Mono

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
internal class UserControllerTest(@Autowired val webClient: WebTestClient) {

    @ClassRule
    private val postgreSQLContainer: PostgresTestContainer? = PostgresTestContainer.getInstance()

    @Test
    @WithMockUser(roles = ["USER"])
    fun testGetUsers() {
        webClient.get().uri("/users")
            .header(HttpHeaders.ACCEPT, "application/json")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(User::class.java)
    }

    @Test
    @WithMockUser(username = "admin", password = "adminpwd", roles = ["ADMIN"])
    fun testAddNewUser() {
        val newUser = User(
            login = "login",
            password = "pwd",
            health = .0,
            money = 1,
            roleName = RoleName.ROLE_USER,
            state = PetState.NORMAL
        )

        webClient
            .mutateWith(csrf())
            .post()
            .uri("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(newUser), User::class.java)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").isNotEmpty
            .jsonPath("$.login").isEqualTo(newUser.login    )
    }
}