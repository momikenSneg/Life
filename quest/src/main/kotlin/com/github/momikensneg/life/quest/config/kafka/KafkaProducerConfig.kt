package com.github.momikensneg.life.quest.config.kafka

import com.github.momikensneg.life.quest.dto.DamageMessage
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.LongSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.converter.StringJsonMessageConverter
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaProducerConfig(
    @Value("\${kafka.server}")
    private val kafkaServer: String,
    @Value("\${kafka.producer.id}")
    private val kafkaProducerId: String
) {

    @Bean
    fun producerConfigs(): Map<String, Any> {
        val props = HashMap<String, Any>()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaServer
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = LongSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        props[ProducerConfig.CLIENT_ID_CONFIG] = kafkaProducerId
        return props
    }

    @Bean
    fun producerStarshipFactory(): ProducerFactory<Long, DamageMessage> =
        DefaultKafkaProducerFactory(producerConfigs())

    @Bean
    fun kafkaTemplate(): KafkaTemplate<Long, DamageMessage> {
        val template = KafkaTemplate(producerStarshipFactory())
        template.messageConverter = StringJsonMessageConverter()
        return template
    }
}