package com.github.momikensneg.life.quest.service.user_quest

import com.github.momikensneg.life.quest.dao.user_quest.UserDailyQuest
import com.github.momikensneg.life.quest.dto.user_quest.userdailyquest.UserDailyQuestDto
import com.github.momikensneg.life.quest.dto.user_quest.userdailyquest.UserDailyQuestGetRequest
import com.github.momikensneg.life.quest.dto.user_quest.userdailyquest.UserDailyQuestResponseDto
import com.github.momikensneg.life.quest.dto.user_quest.userdailyquest.UserDailyQuestUpdateRequest
import com.github.momikensneg.life.quest.exception.ForbiddenException
import com.github.momikensneg.life.quest.exception.QuestAlreadyExistException
import com.github.momikensneg.life.quest.exception.QuestNotFoundException
import com.github.momikensneg.life.quest.mapper.user_quest.UserDailyQuestMapper
import com.github.momikensneg.life.quest.repository.user_quest.UserDailyQuestRepository
import com.github.momikensneg.life.quest.service.BaseServiceImpl
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class UserDailyQuestService(
    private val repository: UserDailyQuestRepository,
    private val mapper: UserDailyQuestMapper
) : BaseServiceImpl<UserDailyQuest, UUID, UserDailyQuestDto,
        UserDailyQuestResponseDto, UserDailyQuestUpdateRequest>(repository, mapper) {

    fun getUserQuests(user: String, request: UserDailyQuestGetRequest): Flux<UserDailyQuestDto> =
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

    override fun validateCreation(dto: UserDailyQuestDto): Mono<UserDailyQuest> =
        repository.findUserQuestsFromTo(dto.userName!!, dto.from!!, dto.to!!)
            .hasElements()
            .flatMap<UserDailyQuest?> {
                Mono.error(QuestAlreadyExistException("Quest with such time frame already exist"))
            }
            .switchIfEmpty(Mono.just(mapper.convertDtoToEntity(dto)))

    override fun validateUpdate(id: UUID, quest: UserDailyQuestDto): Mono<UserDailyQuest> =
        repository.findById(id)
            .switchIfEmpty(Mono.error(QuestNotFoundException("Quest with id $id was not found")))
            .filter { foundQuest ->
                foundQuest.userName == quest.userName
            }
            .switchIfEmpty(Mono.error(ForbiddenException("User can't edin this quest")))
}