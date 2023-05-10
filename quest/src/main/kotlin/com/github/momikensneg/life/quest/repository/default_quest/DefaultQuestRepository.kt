package com.github.momikensneg.life.quest.repository.default_quest

import com.github.momikensneg.lifelib.repository.AbstractTemplateRepository
import org.springframework.data.domain.Sort
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

abstract class DefaultQuestRepository<E, I>(template: R2dbcEntityTemplate) :
    AbstractTemplateRepository<E, I>(template) {

    companion object {
        const val PAGE_SIZE = 10
    }

    fun findByTitle(title: String): Mono<E> =
        template.selectOne(
            Query.query(Criteria.where(titleFieldName()).`is`(title)),
            getEntityClass())

    fun findLikeTitle(userName: String, page: Int): Flux<E> =
        template.select(getEntityClass())
            .matching(
                Query.query(
                Criteria.where(titleFieldName())
                    .like("%$userName%"))
                .sort(Sort.by(Sort.Order.desc(titleFieldName()))).limit(PAGE_SIZE).offset(PAGE_SIZE*page.toLong()))
            .all()

    abstract fun titleFieldName(): String
}