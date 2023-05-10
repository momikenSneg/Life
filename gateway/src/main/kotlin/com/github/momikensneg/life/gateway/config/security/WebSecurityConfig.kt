package com.github.momikensneg.life.gateway.config.security

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono


@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig(
    val authenticationManager: AuthenticationManager,
    val securityContextRepository: SecurityContextRepository
) {

    @Bean
    fun securityWebFilterChain(httpSecurity: ServerHttpSecurity): SecurityWebFilterChain =
        httpSecurity
            .exceptionHandling()
            .authenticationEntryPoint { swe, _ ->
                Mono.fromRunnable { swe.response.statusCode = HttpStatus.UNAUTHORIZED }
            }.accessDeniedHandler { swe, _ ->
                Mono.fromRunnable { swe.response.statusCode = HttpStatus.FORBIDDEN }
            }.and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .authenticationManager(authenticationManager)
            .securityContextRepository(securityContextRepository)
            .authorizeExchange()
            .pathMatchers(HttpMethod.OPTIONS).permitAll()
            //.pathMatchers("/users").hasRole(RoleName.ROLE_ADMIN.role)
            .pathMatchers("/login").permitAll()
            .pathMatchers("/signup").permitAll()
            //.pathMatchers("/quest/**").permitAll()
            .pathMatchers("/actuator/**").permitAll()
            .pathMatchers("/swagger.html").permitAll()
            .pathMatchers("/api-docs").permitAll()
            .pathMatchers("/swagger-resources/**").permitAll()
            .pathMatchers("/webjars/**").permitAll()
            .pathMatchers("/api-docs/**").permitAll()
            .pathMatchers("/configuration/ui").permitAll()
            .pathMatchers("/configuration/security").permitAll()
            .anyExchange().authenticated()
            .and().build()

}