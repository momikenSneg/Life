package com.github.momikensneg.life.quest.controller

import com.github.momikensneg.life.quest.dto.api.ErrorDto
import com.github.momikensneg.life.quest.exception.ForbiddenException
import com.github.momikensneg.life.quest.exception.QuestAlreadyExistException
import com.github.momikensneg.life.quest.exception.QuestNotFoundException
import com.github.momikensneg.life.quest.exception.UserNotFoundException
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

    @ExceptionHandler(QuestNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleQuestNotFoundException(ex: Exception): Mono<ErrorDto> {
        return Mono.just(ErrorDto(ex.message))
    }

    @ExceptionHandler(QuestAlreadyExistException::class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    fun handleQuestAlreadyExistsException(ex: java.lang.Exception): Mono<*>? {
        return Mono.just(ErrorDto(ex.message))
    }

    @ExceptionHandler(ForbiddenException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun forbiddenExistsException(ex: java.lang.Exception): Mono<*>? {
        return Mono.just(ErrorDto(ex.message))
    }

}