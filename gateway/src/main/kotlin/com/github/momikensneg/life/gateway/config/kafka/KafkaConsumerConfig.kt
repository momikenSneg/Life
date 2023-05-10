package com.github.momikensneg.life.gateway.config.kafka

import com.github.momikensneg.life.gateway.dto.DamageMessage
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.LongDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter
import org.springframework.kafka.support.converter.StringJsonMessageConverter

@Configuration
class KafkaConsumerConfig(
    @Value("\${kafka.server}")
    private val kafkaServer: String,
    @Value("\${kafka.group.id}")
    private val kafkaGroupId: String
) {

    @Bean
    fun batchFactory(): KafkaListenerContainerFactory<*> {
        val factory = ConcurrentKafkaListenerContainerFactory<Long, DamageMessage>()
        factory.consumerFactory = consumerFactory()
        factory.isBatchListener = true
        factory.setMessageConverter(BatchMessagingMessageConverter(converter()))
        return factory
    }

    @Bean
    fun singleFactory(): KafkaListenerContainerFactory<*> {
        val factory = ConcurrentKafkaListenerContainerFactory<Long, DamageMessage>()
        factory.consumerFactory = consumerFactory()
        factory.isBatchListener = false
        factory.setMessageConverter(StringJsonMessageConverter())
        return factory
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<Long, DamageMessage> =
        DefaultKafkaConsumerFactory(consumerConfigs())

    @Bean
    fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<*>? =
        ConcurrentKafkaListenerContainerFactory<Any, Any>()

    @Bean
    fun consumerConfigs(): Map<String, Any> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaServer
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = LongDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.GROUP_ID_CONFIG] = kafkaGroupId
        props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = true
        return props
    }

    @Bean
    fun converter(): StringJsonMessageConverter =
        StringJsonMessageConverter()

}
