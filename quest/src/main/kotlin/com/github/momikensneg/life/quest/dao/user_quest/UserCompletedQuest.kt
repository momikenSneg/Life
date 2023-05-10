package com.github.momikensneg.life.quest.dao.user_quest

import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.DtoField
import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.GetApiField
import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.ResponseApiField
import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.UpdateApiField
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("user_completed_quest")
data class UserCompletedQuest(

    @DtoField(true)
    @ResponseApiField(false)
    var id: UUID? = null,

    @DtoField(true)
    @ResponseApiField(false)
    @Column("old_id")
    var oldId: UUID,

    @DtoField(true)
    @ResponseApiField(false)
    @Column("user_name")
    var userName: String,

    @DtoField(true)
    @ResponseApiField(false)
    @UpdateApiField(true)
    var title: String,

    @DtoField(true)
    @ResponseApiField(false)
    var money: Int,

    @DtoField(true)
    @ResponseApiField(false)
    @UpdateApiField(true)
    var description: String,

    @DtoField(true)
    @ResponseApiField(false)
    @GetApiField(false)
    @Column("time_from")
    var from: LocalDateTime,

    @DtoField(true)
    @ResponseApiField(false)
    @GetApiField(false)
    @Column("time_to")
    var to: LocalDateTime
)
