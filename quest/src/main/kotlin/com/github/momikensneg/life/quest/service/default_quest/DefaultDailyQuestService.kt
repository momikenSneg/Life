package com.github.momikensneg.life.quest.service.default_quest

import com.github.momikensneg.life.quest.dao.defaul_quest.DefaultDailyQuest
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultdailyquest.DefaultDailyQuestDto
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultdailyquest.DefaultDailyQuestResponseDto
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultdailyquest.DefaultDailyQuestUpdateRequest
import com.github.momikensneg.life.quest.exception.QuestAlreadyExistException
import com.github.momikensneg.life.quest.exception.QuestNotFoundException
import com.github.momikensneg.life.quest.mapper.default_quest.DefaultDailyQuestMapper
import com.github.momikensneg.life.quest.repository.default_quest.DefaultDailyQuestRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class DefaultDailyQuestService(
    private val repository: DefaultDailyQuestRepository,
    private val mapper: DefaultDailyQuestMapper
) : DefaultQuestService<DefaultDailyQuest, UUID, DefaultDailyQuestDto,
        DefaultDailyQuestResponseDto, DefaultDailyQuestUpdateRequest>(repository, mapper) {

    override fun validateCreation(dto: DefaultDailyQuestDto): Mono<DefaultDailyQuest> =
        repository.findByTitle(dto.title!!)
            .flatMap {
                Mono.error(QuestAlreadyExistException("Quest with name ${dto.title} already exist"))
            }

    override fun validateUpdate(id: UUID, quest: DefaultDailyQuestDto): Mono<DefaultDailyQuest> =
        repository.findById(id)
            .switchIfEmpty(Mono.error(QuestNotFoundException("Quest with id $id was not found")))
}