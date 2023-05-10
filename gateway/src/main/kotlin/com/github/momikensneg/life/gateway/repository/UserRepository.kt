package com.github.momikensneg.life.gateway.repository

import com.github.momikensneg.life.gateway.dao.User
import com.github.momikensneg.life.gateway.dao.User_
import com.github.momikensneg.lifelib.repository.AbstractTemplateRepository
import org.springframework.data.domain.Sort.Order.desc
import org.springframework.data.domain.Sort.by
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query.query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Repository
class UserRepository(template: R2dbcEntityTemplate): AbstractTemplateRepository<User, UUID>(template) {

    override fun getEntityClass(): Class<User> =User::class.java

    override fun getIdName(): String = User_.ID

    fun findByLogin(login: String): Mono<User> =
        template.selectOne(query(where(User_.LOGIN).`is`(login)),User::class.java)

    fun findLikeUsername(userName: String): Flux<User> =
        template.select(User::class.java)
            .matching(query(where(User_.LOGIN)
                            .like("%$userName%"))
                    .sort(by(desc(User_.LOGIN))))
            .all()
}