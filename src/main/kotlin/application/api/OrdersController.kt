package application.api

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/orders")
class OrdersController {

    @PostMapping
    @ResponseStatus(CREATED)
    fun create(@RequestBody request: CreationRequest): OrderRepresentation {
        TODO("not implemented")
    }

    @GetMapping("/{orderId}")
    fun getById(@PathVariable orderId: UUID): ResponseEntity<OrderRepresentation> {
        TODO("not implemented")
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(NO_CONTENT)
    fun deleteById(@PathVariable orderId: UUID) {
        // does nothing
    }

    data class CreationRequest(
        val customerId: UUID,
        // etc ..
    )

    data class OrderRepresentation(
        val orderId: UUID,
        val customerId: UUID,
        val orderDate: Instant,
        val status: String,
        // etc ..
    )
}
