package application.external

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.net.URL

class PaymentClient(
    private val client: RestClient
) {

    fun processCreditCardPayment(cardNumber: String, expiry: String, cardCVC: String): String? =
        client.post()
            .uri { it.path("/payments").build() }
            .body(
                mapOf(
                    "method" to "CREDIT_CARD",
                    "cardNumber" to cardNumber,
                    "cardExpiry" to expiry,
                    "cardCVC" to cardCVC,
                )
            )
            .retrieve()
            .body<String>()

    fun processBankTransferPayment(iban: String): String? =
        client.post()
            .uri { it.path("/payments").build() }
            .body(
                mapOf(
                    "method" to "BANK_TRANSFER",
                    "iban" to iban,
                )
            )
            .retrieve()
            .body<String>()
}

@Configuration
@EnableConfigurationProperties(PaymentServiceProperties::class)
class PaymentClientConfiguration(
    private val properties: PaymentServiceProperties
) {

    @Bean
    fun paymentClient(): PaymentClient =
        PaymentClient(
            client = RestClient.builder()
                .baseUrl(properties.baseUrl.toString())
                .build()
        )
}

@ConfigurationProperties("application.external.backends.payment-service")
data class PaymentServiceProperties(
    val baseUrl: URL
)
