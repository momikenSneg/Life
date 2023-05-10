package com.github.momikensneg.life.quest.mapper.user_quest

import com.github.momikensneg.life.quest.dao.user_quest.UserPlannedQuest
import com.github.momikensneg.life.quest.dto.user_quest.userplannedquest.UserPlannedQuestDto
import com.github.momikensneg.life.quest.dto.user_quest.userplannedquest.UserPlannedQuestResponseDto
import com.github.momikensneg.life.quest.dto.user_quest.userplannedquest.UserPlannedQuestUpdateRequest
import com.github.momikensneg.lifelib.mapper.BaseMapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserPlannedQuestMapper :
    BaseMapper<UserPlannedQuest, UserPlannedQuestDto, UserPlannedQuestResponseDto, UserPlannedQuestUpdateRequest> {
}