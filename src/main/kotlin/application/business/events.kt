package application.business

import java.time.Instant
import java.util.UUID

interface OrderEvent {
    val id: UUID
    val timestamp: Instant
    val order: OrderData

    fun getEventType(): String
}

data class OrderData(
    val orderId: UUID,
    val customerId: UUID,
    val orderDate: Instant,
    val status: String,
    // etc ..
)

data class OrderPlaced(
    override val id: UUID,
    override val timestamp: Instant,
    override val order: OrderData,
) : OrderEvent {
    override fun getEventType() = "orders.placed"
}

data class OrderCanceled(
    override val id: UUID,
    override val timestamp: Instant,
    override val order: OrderData,
) : OrderEvent {
    override fun getEventType() = "orders.canceled"
}
