package com.github.momikensneg.life.quest.repository.user_quest

import com.github.momikensneg.life.quest.dao.user_quest.UserCompletedQuest
import com.github.momikensneg.life.quest.dao.user_quest.UserCompletedQuest_
import com.github.momikensneg.lifelib.repository.AbstractTemplateRepository
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import java.util.*

@Repository
class UserCompletedQuestRepository(template: R2dbcEntityTemplate, val databaseClient: DatabaseClient) :
    AbstractTemplateRepository<UserCompletedQuest, UUID>(template) {

    override fun getEntityClass(): Class<UserCompletedQuest> = UserCompletedQuest::class.java

    override fun getIdName(): String = UserCompletedQuest_.ID

    fun findUserQuestsFromTo(user: String, from: LocalDateTime, to: LocalDateTime): Flux<UserCompletedQuest> =
        template.select(UserCompletedQuest::class.java)
            .matching(
                Query.query(
                    Criteria.where(UserCompletedQuest_.USER_NAME).`is`(user)
                        .and(
                            Criteria.where(UserCompletedQuest_.TIME_FROM).between(from, to)
                                .or(Criteria.where(UserCompletedQuest_.TIME_TO).between(from, to))
                        )
                )
            ).all()

    fun findActiveUsers(): Flux<String> =
        databaseClient
            .sql(
                """select "${UserCompletedQuest_.USER_NAME}" from user_completed_quest 
                |group by "${UserCompletedQuest_.USER_NAME}";""".trimMargin()
            )
            .map { row, _ ->
                row.get(UserCompletedQuest_.USER_NAME, String::class.java)!!
            }.all()

}