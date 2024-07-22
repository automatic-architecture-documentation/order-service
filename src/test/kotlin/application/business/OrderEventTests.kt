package application.business

import application.business.OrderStatus.PLACED
import application.business.OrderStatus.PROCESSING
import application.documentation.ArchitectureDocumentation.createOrReplaceEvent
import application.documentation.EventDescriptionSpec
import application.documentation.eventDescription
import org.junit.jupiter.api.Test
import java.time.Instant.parse
import java.util.UUID.fromString

class OrderEventTests {

    @Test
    fun `OrderPlaced event`() {
        val description = eventDescription(
            example = OrderPlaced(
                id = fromString("3d6fd447-a311-4028-8248-356e3621d450"),
                timestamp = parse("2024-07-22T12:34:56.789Z"),
                order = OrderData(
                    orderId = fromString("a64914f7-7404-4e85-8e1a-778068fae307"),
                    customerId = fromString("ed2a43d7-e49b-408d-8b5f-e2e2305954c2"),
                    status = PLACED
                )
            ),
            description = "Emitted whenever a new order is placed.",
            fields = {
                orderDataFields("order")
            }
        )
        createOrReplaceEvent(description)
    }

    @Test
    fun `OrderCanceled event`() {
        val description = eventDescription(
            example = OrderCanceled(
                id = fromString("4c97c099-0c00-4e56-841d-fbfe81770936"),
                timestamp = parse("2024-07-22T12:34:56.789Z"),
                order = OrderData(
                    orderId = fromString("a64914f7-7404-4e85-8e1a-778068fae307"),
                    customerId = fromString("ed2a43d7-e49b-408d-8b5f-e2e2305954c2"),
                    status = PROCESSING
                )
            ),
            description = "Emitted whenever an order is canceled.",
            fields = {
                orderDataFields("order")
            }
        )
        createOrReplaceEvent(description)
    }

    private fun EventDescriptionSpec.orderDataFields(objectName: String) {
        field(
            property = objectName,
            type = "Object",
        )
        field(
            property = "$objectName.orderId",
            type = "UUID4",
            description = "The ID of the order."
        )
        field(
            property = "$objectName.customerId",
            type = "UUID4",
            description = "The ID of the customer that placed the order."
        )
        field(
            property = "$objectName.status",
            type = "Enumeration",
            description = "The status of the order. Might have one of the following values: "
                    + OrderStatus.entries.joinToString()
        )
    }
}
