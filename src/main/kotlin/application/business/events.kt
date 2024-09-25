package application.business

import java.time.Instant
import java.util.UUID

interface Event {
    val id: UUID
    val timestamp: Instant
    fun eventName(): String
    fun eventType(): String
}

interface OrderEvent : Event {
    val order: Order
}

data class OrderPlaced(
    override val id: UUID,
    override val timestamp: Instant,
    override val order: Order,
) : OrderEvent {
    override fun eventName() = "Order Placed"
    override fun eventType() = "order.placed"
}

data class OrderCanceled(
    override val id: UUID,
    override val timestamp: Instant,
    override val order: Order,
) : OrderEvent {
    override fun eventName() = "Order Canceled"
    override fun eventType() = "order.canceled"
}
