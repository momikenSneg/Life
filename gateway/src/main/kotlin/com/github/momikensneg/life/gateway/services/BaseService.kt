package com.github.momikensneg.life.gateway.services

import reactor.core.publisher.Mono

interface BaseService<D,I> {

    fun findById(id: I): Mono<D>

    fun create(dto: D): Mono<D>

    fun update(id: I, user: D): Mono<D>

    fun delete(dto: D): Mono<Void>

}