package com.github.momikensneg.life.gateway.controller

import com.github.momikensneg.life.gateway.dto.ErrorDto
import com.github.momikensneg.life.gateway.exceptions.ForbiddenException
import com.github.momikensneg.life.gateway.exceptions.UserAlreadyExistsException
import com.github.momikensneg.life.gateway.exceptions.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono


@RestControllerAdvice
class GlobalExceptionController {

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleUserNotFoundException(ex: Exception): Mono<ErrorDto> {
        return Mono.just(ErrorDto(ex.message))
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    fun handleUserAlreadyExistsException(ex: java.lang.Exception): Mono<*>? {
        return Mono.just(ErrorDto(ex.message))
    }

    @ExceptionHandler(ForbiddenException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun forbiddenExistsException(ex: java.lang.Exception): Mono<*>? {
        return Mono.just(ErrorDto(ex.message))
    }
}