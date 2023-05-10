package com.github.momikensneg.life.quest.service

import com.github.momikensneg.life.quest.exception.QuestNotFoundException
import com.github.momikensneg.lifelib.mapper.BaseMapper
import com.github.momikensneg.lifelib.repository.AbstractTemplateRepository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

abstract class BaseServiceImpl<E,I,D,R,U>(
    private val repository: AbstractTemplateRepository<E, I>,
    private val mapper: BaseMapper<E, D, R, U>
): BaseService<D,I> {

    override fun findById(id: I): Mono<D> =
        repository.findById(id)
            .switchIfEmpty(Mono.error(QuestNotFoundException("Quest with id $id was not found")))
            .map(mapper::convertEntityToDto)

    @Transactional
    override fun create(dto: D): Mono<D> =
        validateCreation(dto)
            .switchIfEmpty(
                repository.save(mapper.convertDtoToEntity(dto))
            ).map(mapper::convertEntityToDto)

    @Transactional
    override fun update(id: I, dto: D): Mono<D> =
        validateUpdate(id, dto)
            .flatMap { u ->
                val newDto = mapper.convertToEntityFromDto(dto, u)
                repository.save(newDto)
            }.map(mapper::convertEntityToDto)

    override fun delete(id: I): Mono<Void> =
        repository.deleteById(id)

    protected abstract fun validateCreation(dto: D): Mono<E>

    protected abstract fun validateUpdate(id: I, quest: D): Mono<E>

}