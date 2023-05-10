package com.github.momikensneg.life.quest.service

import reactor.core.publisher.Mono

interface BaseService<D,I> {

    fun findById(id: I): Mono<D>

    fun create(dto: D): Mono<D>

    fun update(id: I, dto: D): Mono<D>

    fun delete(id: I): Mono<Void>

}