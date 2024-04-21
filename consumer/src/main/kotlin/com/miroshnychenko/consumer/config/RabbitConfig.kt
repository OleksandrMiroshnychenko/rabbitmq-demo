package com.miroshnychenko.consumer.config

import com.miroshnychenko.consumer.dto.Orderr
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.InjectionPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope


@Configuration
@EnableRabbit
class RabbitConfig {

    private var timestamp: Long? = null

    @Bean
    @Scope("prototype")
    fun logger(injectionPoint: InjectionPoint): Logger = LoggerFactory
        .getLogger(
            injectionPoint.methodParameter?.containingClass // constructor
                ?: injectionPoint.field?.declaringClass
        )

    @Bean
    fun jsonConverter(): Jackson2JsonMessageConverter = Jackson2JsonMessageConverter()

    @RabbitListener(queues = ["q.demo"])
    fun onMessage(orderr: Orderr) {
        if (timestamp == null) timestamp = System.currentTimeMillis()
        println((System.currentTimeMillis() - timestamp!!).toString() + " : " + orderr.toString())
    }

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val connectionFactory = CachingConnectionFactory()
        connectionFactory.username = "admin"
        connectionFactory.setPassword("admin")
        connectionFactory.setAddresses("127.0.0.1:5672,127.0.0.1:5682,127.0.0.1:5692")
        connectionFactory.channelCacheSize = 10;
        return connectionFactory
    }

    @Bean
    fun rabbitListenerContainerFactory(jsonConverter: Jackson2JsonMessageConverter): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory())
        factory.setConcurrentConsumers(10)
        factory.setMaxConcurrentConsumers(20)
        factory.setMessageConverter(jsonConverter)
        return factory
    }

    @Bean
    fun queue(): Queue {
        return Queue("q.demo")
    }
}