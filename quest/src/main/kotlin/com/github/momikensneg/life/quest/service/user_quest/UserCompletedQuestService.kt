package com.github.momikensneg.life.quest.service.user_quest

import com.github.momikensneg.life.quest.dao.user_quest.UserCompletedQuest
import com.github.momikensneg.life.quest.dto.user_quest.usercompletedquest.UserCompletedQuestDto
import com.github.momikensneg.life.quest.dto.user_quest.usercompletedquest.UserCompletedQuestGetRequest
import com.github.momikensneg.life.quest.dto.user_quest.usercompletedquest.UserCompletedQuestResponseDto
import com.github.momikensneg.life.quest.dto.user_quest.usercompletedquest.UserCompletedQuestUpdateRequest
import com.github.momikensneg.life.quest.exception.QuestNotFoundException
import com.github.momikensneg.life.quest.mapper.user_quest.UserCompletedQuestMapper
import com.github.momikensneg.life.quest.repository.user_quest.UserCompletedQuestRepository
import com.github.momikensneg.life.quest.service.BaseServiceImpl
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class UserCompletedQuestService(
    private val repository: UserCompletedQuestRepository,
    private val mapper: UserCompletedQuestMapper
): BaseServiceImpl<UserCompletedQuest, UUID, UserCompletedQuestDto,
        UserCompletedQuestResponseDto, UserCompletedQuestUpdateRequest>(repository, mapper) {

    fun getUserQuests(user: String, request: UserCompletedQuestGetRequest): Flux<UserCompletedQuestDto> =
        repository.findUserQuestsFromTo(user, request.from, request.to)
            .map(mapper::convertEntityToDto)

    override fun validateCreation(dto: UserCompletedQuestDto): Mono<UserCompletedQuest> =
        Mono.just(mapper.convertDtoToEntity(dto))

    override fun validateUpdate(id: UUID, quest: UserCompletedQuestDto): Mono<UserCompletedQuest> =
        repository.findById(id)
            .switchIfEmpty(Mono.error(QuestNotFoundException("Quest with id $id was not found")))
}