package com.github.momikensneg.life.quest.mapper.user_quest

import com.github.momikensneg.life.quest.dao.user_quest.UserDailyQuest
import com.github.momikensneg.life.quest.dto.user_quest.userdailyquest.UserDailyQuestDto
import com.github.momikensneg.life.quest.dto.user_quest.userdailyquest.UserDailyQuestResponseDto
import com.github.momikensneg.life.quest.dto.user_quest.userdailyquest.UserDailyQuestUpdateRequest
import com.github.momikensneg.lifelib.mapper.BaseMapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserDailyQuestMapper :
    BaseMapper<UserDailyQuest, UserDailyQuestDto, UserDailyQuestResponseDto, UserDailyQuestUpdateRequest> {
}