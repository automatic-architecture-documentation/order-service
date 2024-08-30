package application.business

import application.config.messaging.EXCHANGE_ORDER_EVENTS
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class EventPublisher(
    private val rabbitTemplate: RabbitTemplate
) {
    fun publishEvent(event: Event) {
        rabbitTemplate.convertAndSend(EXCHANGE_ORDER_EVENTS, event.eventType(), event)
    }
}
