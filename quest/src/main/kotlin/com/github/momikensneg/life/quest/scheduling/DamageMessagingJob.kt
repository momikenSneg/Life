package com.github.momikensneg.life.quest.scheduling

import com.github.momikensneg.life.quest.service.DamageMessageProducerService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class DamageMessagingJob(
    val service: DamageMessageProducerService
) {

    @Scheduled(fixedDelay = Long.MAX_VALUE)
    //@Scheduled(cron = "\${job.sync.cron}")
    fun sendMessages() {
        service.produce()
    }
}