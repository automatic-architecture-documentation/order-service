package application.external

import application.business.Order
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.net.URL

class NotificationClient(
    private val client: RestClient
) {

    fun confirmOrder(order: Order): Unit {
        client.post()
            .uri {
                it.path("/notifications/customer/{customerId}/order-confirmation")
                    .build(mapOf("customerId" to order.customerId))
            }
            .body(order)
            .retrieve()
            .body<JsonNode>()
    }
}

@Configuration
@EnableConfigurationProperties(NotificationServiceProperties::class)
class NotificationClientConfiguration(
    private val properties: NotificationServiceProperties
) {

    @Bean
    fun notificationClient(): NotificationClient =
        NotificationClient(
            client = RestClient.builder()
                .baseUrl(properties.baseUrl.toString())
                .build()
        )
}

@ConfigurationProperties("application.external.backends.notification-service")
data class NotificationServiceProperties(
    val baseUrl: URL
)
