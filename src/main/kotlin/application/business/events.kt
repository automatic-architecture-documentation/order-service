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
    val order: OrderData
}

data class OrderData(
    val orderId: UUID,
    val customerId: UUID,
    val status: OrderStatus,
    // etc ..
)

data class OrderPlaced(
    override val id: UUID,
    override val timestamp: Instant,
    override val order: OrderData,
) : OrderEvent {
    override fun eventName() = "Order Placed"
    override fun eventType() = "orders.placed"
}

data class OrderCanceled(
    override val id: UUID,
    override val timestamp: Instant,
    override val order: OrderData,
) : OrderEvent {
    override fun eventName() = "Order Canceled"
    override fun eventType() = "orders.canceled"
}
