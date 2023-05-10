package com.github.momikensneg.life.quest.repository.default_quest

import com.github.momikensneg.life.quest.dao.defaul_quest.DefaultDailyQuest
import com.github.momikensneg.life.quest.dao.defaul_quest.DefaultDailyQuest_
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class DefaultDailyQuestRepository(@Autowired template: R2dbcEntityTemplate) :
    DefaultQuestRepository<DefaultDailyQuest, UUID>(template) {

    override fun titleFieldName(): String = DefaultDailyQuest_.TITLE

    override fun getEntityClass(): Class<DefaultDailyQuest> = DefaultDailyQuest::class.java

    override fun getIdName(): String = DefaultDailyQuest_.ID

}