package com.github.momikensneg.life.quest.repository.default_quest

import com.github.momikensneg.life.quest.dao.defaul_quest.DefaultPlannedQuest
import com.github.momikensneg.life.quest.dao.defaul_quest.DefaultPlannedQuest_
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class DefaultPlannedQuestRepository(template: R2dbcEntityTemplate):
    DefaultQuestRepository<DefaultPlannedQuest, UUID>(template) {

    override fun titleFieldName(): String = DefaultPlannedQuest_.TITLE

    override fun getEntityClass(): Class<DefaultPlannedQuest> = DefaultPlannedQuest::class.java

    override fun getIdName(): String = DefaultPlannedQuest_.ID
}