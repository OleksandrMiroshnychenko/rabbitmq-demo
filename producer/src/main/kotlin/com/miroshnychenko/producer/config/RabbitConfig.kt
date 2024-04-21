package com.miroshnychenko.producer.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.InjectionPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class RabbitConfig {

    @Bean
    @Scope("prototype")
    fun logger(injectionPoint: InjectionPoint): Logger = LoggerFactory
        .getLogger(
            injectionPoint.methodParameter?.containingClass // constructor
                ?: injectionPoint.field?.declaringClass
        )

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val connectionFactory = CachingConnectionFactory()
        connectionFactory.username = "admin"
        connectionFactory.setPassword("admin")
        connectionFactory.setAddresses("127.0.0.1:5672,127.0.0.1:5682,127.0.0.1:5692")
        return connectionFactory
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.exchange = "ex.demo"
        rabbitTemplate.messageConverter = Jackson2JsonMessageConverter()
        return rabbitTemplate
    }
}