package application.external

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.net.URL

class ExternalService1Client(
    private val client: RestClient
) {

    fun getOpenInvoices(id: String): String? =
        client.get()
            .uri { it.path("/invoices/{userId}/{state}").build(mapOf("userId" to id, "state" to "open")) }
            .retrieve()
            .body<String>()
}

@Configuration
@EnableConfigurationProperties(ExternalService1Properties::class)
class ExternalService1Configuration(
    private val properties: ExternalService1Properties
) {

    @Bean
    fun externalService1Client(): ExternalService1Client =
        ExternalService1Client(
            client = RestClient.builder()
                .baseUrl(properties.baseUrl.toString())
                .build()
        )
}

@ConfigurationProperties("application.external.backends.external-service-1")
data class ExternalService1Properties(
    val baseUrl: URL
)
