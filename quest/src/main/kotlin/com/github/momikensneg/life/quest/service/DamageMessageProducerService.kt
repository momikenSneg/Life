package com.github.momikensneg.life.quest.service

import com.github.momikensneg.life.quest.dto.DamageMessage
import com.github.momikensneg.life.quest.repository.user_quest.UserCompletedQuestRepository
import com.github.momikensneg.life.quest.repository.user_quest.UserDailyQuestRepository
import com.github.momikensneg.life.quest.repository.user_quest.UserPlannedQuestRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Service
class DamageMessageProducerService(
    private val kafkaStarshipTemplate: KafkaTemplate<Long, DamageMessage>,
    private val userCompletedQuestRepository: UserCompletedQuestRepository,
    private val userDailyQuestRepository: UserDailyQuestRepository,
    private val userPlannedQuestRepository: UserPlannedQuestRepository,
    @Value("\${kafka.topic}")
    private val topic: String
) {

    companion object {
        const val FULL_DAILY_DAMAGE = 70.0
    }

    fun produce() {
        userDailyQuestRepository.findActiveUsers()
            .concatWith(userPlannedQuestRepository.findActiveUsers())
            .distinct()
            .flatMap { user ->
                val completedQuests = userCompletedQuestRepository
                    .findUserQuestsFromTo(user, LocalDate.now().atStartOfDay(), LocalDate.now().atTime(LocalTime.MAX))
                    .map { quest -> quest.oldId }
                    .collectList()
                val plannedQuests = userPlannedQuestRepository
                    .findUserQuestsFromTo(user, LocalDate.now().atStartOfDay(), LocalDate.now().atTime(LocalTime.MAX))
                    .map { quest -> quest.id!! }
                    .collectList()
                val dailyQuests = userDailyQuestRepository.findUserQuestsFromTo(user, LocalTime.MIN, LocalTime.MAX)
                    .map { quest -> quest.id!! }
                    .collectList()
                Mono.zip(Mono.just(user), completedQuests, dailyQuests, plannedQuests)
            }
            .map { tuple ->
                UserDayInfo(tuple.t1, tuple.t2, tuple.t3, tuple.t4)
            }
            .map { info ->
                val plannedCount = info.dailyQuests.size + info.plannedQuests.size.toDouble()
                val percent = (plannedCount - info.completedQuests.size) / plannedCount
                DamageMessage(info.user, FULL_DAILY_DAMAGE * percent)
            }
            .subscribe { damage ->
                kafkaStarshipTemplate.send(topic, damage)
            }
    }

    private class UserDayInfo(
        val user: String,
        val completedQuests: List<UUID>,
        val dailyQuests: List<UUID>,
        val plannedQuests: List<UUID>
    )
}