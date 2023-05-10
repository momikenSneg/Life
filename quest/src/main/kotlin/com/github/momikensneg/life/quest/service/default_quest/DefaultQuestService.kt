package com.github.momikensneg.life.quest.service.default_quest

import com.github.momikensneg.life.quest.exception.QuestNotFoundException
import com.github.momikensneg.life.quest.repository.default_quest.DefaultQuestRepository
import com.github.momikensneg.life.quest.service.BaseServiceImpl
import com.github.momikensneg.lifelib.mapper.BaseMapper
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

abstract class DefaultQuestService<E, I, D, R, U>(
    private val repository: DefaultQuestRepository<E, I>,
    private val mapper: BaseMapper<E, D, R, U>
): BaseServiceImpl<E, I, D, R, U>(repository, mapper) {

    fun findByName(name: String): Mono<D> =
        repository.findByTitle(name)
            .switchIfEmpty(Mono.error(QuestNotFoundException("Quest with name $name was not found")))
            .map(mapper::convertEntityToDto)

    fun listQuests(name: String, page: Int): Flux<D> =
        repository.findLikeTitle(name, page)
            .map(mapper::convertEntityToDto)
}