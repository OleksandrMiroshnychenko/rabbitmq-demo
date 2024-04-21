package com.miroshnychenko.producer

import com.miroshnychenko.producer.dto.Order
import com.miroshnychenko.producer.dto.OrderType
import org.slf4j.Logger
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
class ProducerApplication(private val log: Logger, private val rabbitTemplate: RabbitTemplate) {

    @PostConstruct
    fun send() {
        for (i in 0..99999) {
            val id: Int = Random().nextInt(100000)
            rabbitTemplate.convertAndSend(Order(id, "TEST$id", OrderType.entries[id % 2]))
        }
        log.info("Sending completed.")
    }
}

fun main(args: Array<String>) {
    runApplication<ProducerApplication>(*args)
}
