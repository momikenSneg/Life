package com.github.momikensneg.life.quest.repository.user_quest

import com.github.momikensneg.life.quest.dao.user_quest.UserPlannedQuest
import com.github.momikensneg.life.quest.dao.user_quest.UserPlannedQuest_
import com.github.momikensneg.lifelib.repository.AbstractTemplateRepository
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@Repository
class UserPlannedQuestRepository(template: R2dbcEntityTemplate, val databaseClient: DatabaseClient) :
    AbstractTemplateRepository<UserPlannedQuest, UUID>(template) {

    override fun getEntityClass(): Class<UserPlannedQuest> = UserPlannedQuest::class.java

    override fun getIdName(): String = UserPlannedQuest_.ID

    fun findUserQuestsFromTo(user: String, from: LocalDateTime, to: LocalDateTime): Flux<UserPlannedQuest> =
        template.select(UserPlannedQuest::class.java)
            .matching(
                Query.query(
                    Criteria.where(UserPlannedQuest_.USER_NAME).`is`(user)
                        .and(
                            Criteria.where(UserPlannedQuest_.TIME_FROM).between(from, to)
                                .or(Criteria.where(UserPlannedQuest_.TIME_TO).between(from, to))
                        )
                )
            ).all()

    fun findActiveUsers(): Flux<String> {
        return databaseClient
            .sql(
                """select "${UserPlannedQuest_.USER_NAME}" from user_planned_quest 
                    |where "${UserPlannedQuest_.TIME_TO}" between '${LocalDate.now().atStartOfDay()}' 
                    |and '${LocalDate.now().atTime(LocalTime.MAX)}' 
                    |group by user_name;""".trimMargin()
            )
            .map { row, _ ->
                row.get(UserPlannedQuest_.USER_NAME, String::class.java)!!
            }.all()
    }
}