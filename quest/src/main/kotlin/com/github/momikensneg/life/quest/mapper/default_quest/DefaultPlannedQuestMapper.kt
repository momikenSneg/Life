package com.github.momikensneg.life.quest.mapper.default_quest

import com.github.momikensneg.life.quest.dao.defaul_quest.DefaultPlannedQuest
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultplannedquest.DefaultPlannedQuestDto
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultplannedquest.DefaultPlannedQuestResponseDto
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultplannedquest.DefaultPlannedQuestUpdateRequest
import com.github.momikensneg.lifelib.mapper.BaseMapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface DefaultPlannedQuestMapper :
    BaseMapper<DefaultPlannedQuest, DefaultPlannedQuestDto,
            DefaultPlannedQuestResponseDto, DefaultPlannedQuestUpdateRequest> {
}