package com.github.momikensneg.life.quest.mapper.default_quest

import com.github.momikensneg.life.quest.dao.defaul_quest.DefaultDailyQuest
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultdailyquest.DefaultDailyQuestDto
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultdailyquest.DefaultDailyQuestResponseDto
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultdailyquest.DefaultDailyQuestUpdateRequest
import com.github.momikensneg.lifelib.mapper.BaseMapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface DefaultDailyQuestMapper:
    BaseMapper<DefaultDailyQuest, DefaultDailyQuestDto, DefaultDailyQuestResponseDto, DefaultDailyQuestUpdateRequest> {
}