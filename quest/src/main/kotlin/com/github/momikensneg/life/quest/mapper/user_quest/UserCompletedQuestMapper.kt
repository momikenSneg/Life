package com.github.momikensneg.life.quest.mapper.user_quest

import com.github.momikensneg.life.quest.dao.user_quest.UserCompletedQuest
import com.github.momikensneg.life.quest.dto.user_quest.usercompletedquest.UserCompletedQuestDto
import com.github.momikensneg.life.quest.dto.user_quest.usercompletedquest.UserCompletedQuestResponseDto
import com.github.momikensneg.life.quest.dto.user_quest.usercompletedquest.UserCompletedQuestUpdateRequest
import com.github.momikensneg.lifelib.mapper.BaseMapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserCompletedQuestMapper :
    BaseMapper<UserCompletedQuest, UserCompletedQuestDto,
            UserCompletedQuestResponseDto, UserCompletedQuestUpdateRequest> {
}