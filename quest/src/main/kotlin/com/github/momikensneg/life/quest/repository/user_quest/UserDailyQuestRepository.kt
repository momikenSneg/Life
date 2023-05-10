package com.github.momikensneg.life.quest.repository.user_quest

import com.github.momikensneg.life.quest.dao.user_quest.UserDailyQuest
import com.github.momikensneg.life.quest.dao.user_quest.UserDailyQuest_
import com.github.momikensneg.lifelib.repository.AbstractTemplateRepository
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalTime
import java.util.*

@Repository
class UserDailyQuestRepository(template: R2dbcEntityTemplate, val databaseClient: DatabaseClient) :
    AbstractTemplateRepository<UserDailyQuest, UUID>(template) {

    override fun getEntityClass(): Class<UserDailyQuest> = UserDailyQuest::class.java

    override fun getIdName(): String = UserDailyQuest_.ID

    fun findUserQuestsFromTo(user: String, from: LocalTime, to: LocalTime): Flux<UserDailyQuest> =
        template.select(UserDailyQuest::class.java)
            .matching(
                Query.query(
                    Criteria.where(UserDailyQuest_.USER_NAME).`is`(user)
                        .and(Criteria.where(UserDailyQuest_.TIME_FROM).between(from, to)
                            .or(Criteria.where(UserDailyQuest_.TIME_TO).between(from, to)))
                )
            ).all()

    fun findActiveUsers(): Flux<String> =
        databaseClient
            .sql(
                """select "${UserDailyQuest_.USER_NAME}" from user_daily_quest 
                    |where "${UserDailyQuest_.TIME_TO}" between '00:00:00.000000' and '23:59:59.000000' 
                    |group by user_name;""".trimMargin()
            )
            .map { row, _ ->
                row.get(UserDailyQuest_.USER_NAME, String::class.java)!!
            }.all()
}