package com.github.momikensneg.life.quest.dao.user_quest

import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.DtoField
import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.GetApiField
import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.ResponseApiField
import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.UpdateApiField
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalTime
import java.util.*

@Table("user_daily_quest")
data class UserDailyQuest(

    @DtoField(true)
    @ResponseApiField(false)
    var id: UUID? = null,

    @DtoField(true)
    @ResponseApiField(false)
    @Column("user_name")
    var userName: String,

    @DtoField(true)
    @ResponseApiField(false)
    @UpdateApiField(true)
    @GetApiField(true)
    var title: String,

    @DtoField(true)
    @ResponseApiField(false)
    @UpdateApiField(true)
    var money: Int,

    @DtoField(true)
    @ResponseApiField(false)
    @UpdateApiField(true)
    var description: String,

    @DtoField(true)
    @ResponseApiField(false)
    @UpdateApiField(true)
    @GetApiField(false)
    @Column("time_from")
    var from: LocalTime,

    @DtoField(true)
    @ResponseApiField(false)
    @UpdateApiField(true)
    @GetApiField(false)
    @Column("time_to")
    var to: LocalTime
)
