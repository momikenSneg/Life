package com.github.momikensneg.life.quest.service.user_quest

import com.github.momikensneg.life.quest.dao.user_quest.UserPlannedQuest
import com.github.momikensneg.life.quest.dto.user_quest.userplannedquest.UserPlannedQuestDto
import com.github.momikensneg.life.quest.dto.user_quest.userplannedquest.UserPlannedQuestGetRequest
import com.github.momikensneg.life.quest.dto.user_quest.userplannedquest.UserPlannedQuestResponseDto
import com.github.momikensneg.life.quest.dto.user_quest.userplannedquest.UserPlannedQuestUpdateRequest
import com.github.momikensneg.life.quest.exception.ForbiddenException
import com.github.momikensneg.life.quest.exception.QuestAlreadyExistException
import com.github.momikensneg.life.quest.exception.QuestNotFoundException
import com.github.momikensneg.life.quest.mapper.user_quest.UserPlannedQuestMapper
import com.github.momikensneg.life.quest.repository.user_quest.UserPlannedQuestRepository
import com.github.momikensneg.life.quest.service.BaseServiceImpl
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class UserPlannedQuestService(
    private val repository: UserPlannedQuestRepository,
    private val mapper: UserPlannedQuestMapper
): BaseServiceImpl<UserPlannedQuest, UUID, UserPlannedQuestDto,
        UserPlannedQuestResponseDto, UserPlannedQuestUpdateRequest>(repository, mapper)  {

    fun getUserQuests(user: String, request: UserPlannedQuestGetRequest): Flux<UserPlannedQuestDto> =
        repository.findUserQuestsFromTo(user, request.from, request.to)
            .filter { quest ->
                val title = request.title
                if (title == null) true else quest.title.lowercase().contains(title.lowercase().toRegex())
            }
            .map(mapper::convertEntityToDto)

    fun deleteUsersQuest(user: String, id: UUID): Mono<Void> =
        repository.findById(id)
            .switchIfEmpty(Mono.error(QuestNotFoundException("Quest with id $id was not found")))
            .filter { foundQuest -> foundQuest.userName == user }
            .switchIfEmpty(Mono.error(ForbiddenException("User can't delete this quest")))
            .flatMap { repository.deleteById(id) }

    override fun validateCreation(dto: UserPlannedQuestDto): Mono<UserPlannedQuest> =
        repository.findUserQuestsFromTo(dto.userName!!, dto.from!!, dto.to!!)
            .hasElements()
            .flatMap<UserPlannedQuest?> {
                Mono.error(QuestAlreadyExistException("Quest with such time frame already exist"))
            }
            .switchIfEmpty(Mono.just(mapper.convertDtoToEntity(dto)))

    override fun validateUpdate(id: UUID, quest: UserPlannedQuestDto): Mono<UserPlannedQuest> =
        repository.findById(id)
            .switchIfEmpty(Mono.error(QuestNotFoundException("Quest with id $id was not found")))
            .filter { foundQuest ->
                foundQuest.userName == quest.userName
            }
            .switchIfEmpty(Mono.error(ForbiddenException("User can't edin this quest")))
}