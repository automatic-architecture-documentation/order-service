package application.business

import java.util.UUID

data class Order(
    val orderId: UUID,
    val customerId: UUID,
    val status: OrderStatus,
    // etc ..
)
