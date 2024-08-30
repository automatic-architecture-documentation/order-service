package application.config.messaging

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Binding.DestinationType.QUEUE
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.core.TopicExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

const val EXCHANGE_ORDER_EVENTS = "order-events"
const val EXCHANGE_INVENTORY_EVENTS = "inventory-events"
const val QUEUE_PRODUCT_SOLD_OUT = "inventory-events.order-service.product-sold-out"

@Configuration
class MessagingConfiguration {

    @Bean
    fun orderEventsExchange(): Exchange = TopicExchange(EXCHANGE_ORDER_EVENTS)

    @Bean
    fun inventoryEventsExchange(): Exchange = TopicExchange(EXCHANGE_INVENTORY_EVENTS)

    @Bean
    fun inventoryProductSoldOutQueue(): Queue = QueueBuilder.durable(QUEUE_PRODUCT_SOLD_OUT).build()

    @Bean
    fun inventoryProductSoldOutQueueBinding(): Binding =
        Binding(QUEUE_PRODUCT_SOLD_OUT, QUEUE, EXCHANGE_INVENTORY_EVENTS, "product.sold-out", emptyMap())
}
