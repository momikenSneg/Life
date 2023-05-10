package com.github.momikensneg.life.quest.dao.defaul_quest

import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.DtoField
import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.ResponseApiField
import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.UpdateApiField
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("default_daily_quest")
data class DefaultDailyQuest(

    @DtoField(true)
    @ResponseApiField(false)
    var id: UUID? = null,

    @DtoField(true)
    @ResponseApiField(false)
    @UpdateApiField(true)
    var title: String,

    @DtoField(true)
    @ResponseApiField(false)
    @UpdateApiField(true)
    var money: Int,

    @DtoField(true)
    @ResponseApiField(false)
    @UpdateApiField(true)
    var description: String
)
