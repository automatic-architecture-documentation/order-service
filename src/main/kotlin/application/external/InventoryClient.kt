package application.external

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.net.URL

class InventoryClient(
    private val client: RestClient
) {

    fun getProductInventory(productId: String): Any? =
        client.get()
            .uri { it.path("/inventory/{productId}").build(mapOf("productId" to productId)) }
            .retrieve()
            .body<JsonNode>()

    fun reserveProductInventory(productId: String, amount: Int): Any? =
        client.post()
            .uri { it.path("/inventory/{productId}/reserve").build(mapOf("productId" to productId)) }
            .body(mapOf("amount" to amount))
            .retrieve()
            .body<JsonNode>()
}

@Configuration
@EnableConfigurationProperties(InventoryServiceProperties::class)
class InventoryClientConfiguration(
    private val properties: InventoryServiceProperties
) {

    @Bean
    fun inventoryClient(): InventoryClient =
        InventoryClient(
            client = RestClient.builder()
                .baseUrl(properties.baseUrl.toString())
                .build()
        )
}

@ConfigurationProperties("application.external.backends.inventory-service")
data class InventoryServiceProperties(
    val baseUrl: URL
)
