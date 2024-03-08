package application.external

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.net.URL

class BackendService2Client(
    private val client: RestClient
) {

    fun getBookingsOfUser(userId: String): String? =
        client.get()
            .uri { it.path("/bookings/{userId}").build(mapOf("userId" to userId)) }
            .retrieve()
            .body<String>()
}

@Configuration
@EnableConfigurationProperties(BackendService2Properties::class)
class BackendService2Configuration(
    private val properties: BackendService2Properties
) {

    @Bean
    fun backendService2Client(): BackendService2Client =
        BackendService2Client(
            client = RestClient.builder()
                .baseUrl(properties.baseUrl.toString())
                .build()
        )
}

@ConfigurationProperties("application.external.backends.backend-service-2")
data class BackendService2Properties(
    val baseUrl: URL
)
