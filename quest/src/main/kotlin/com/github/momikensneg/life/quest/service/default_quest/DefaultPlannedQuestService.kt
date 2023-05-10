package com.github.momikensneg.life.quest.service.default_quest

import com.github.momikensneg.life.quest.dao.defaul_quest.DefaultPlannedQuest
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultplannedquest.DefaultPlannedQuestDto
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultplannedquest.DefaultPlannedQuestResponseDto
import com.github.momikensneg.life.quest.dto.defaul_quest.defaultplannedquest.DefaultPlannedQuestUpdateRequest
import com.github.momikensneg.life.quest.exception.QuestAlreadyExistException
import com.github.momikensneg.life.quest.exception.QuestNotFoundException
import com.github.momikensneg.life.quest.mapper.default_quest.DefaultPlannedQuestMapper
import com.github.momikensneg.life.quest.repository.default_quest.DefaultPlannedQuestRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class DefaultPlannedQuestService(
    private val repository: DefaultPlannedQuestRepository,
    private val mapper: DefaultPlannedQuestMapper
) : DefaultQuestService<DefaultPlannedQuest, UUID, DefaultPlannedQuestDto,
        DefaultPlannedQuestResponseDto, DefaultPlannedQuestUpdateRequest>(repository, mapper) {

    override fun validateCreation(dto: DefaultPlannedQuestDto): Mono<DefaultPlannedQuest> =
        repository.findByTitle(dto.title!!)
            .flatMap {
                Mono.error(QuestAlreadyExistException("Quest with name ${dto.title} already exist"))
            }

    override fun validateUpdate(id: UUID, quest: DefaultPlannedQuestDto): Mono<DefaultPlannedQuest> =
        repository.findById(id)
            .switchIfEmpty(Mono.error(QuestNotFoundException("Quest with id $id was not found")))
}