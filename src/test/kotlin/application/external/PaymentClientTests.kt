package application.external

import application.documentation.ArchitectureDocumentation.createOrAmendDependencyEndpoints
import application.documentation.ArchitectureDocumentation.createOrReplaceDependency
import application.documentation.ComponentType
import application.documentation.Credentials
import application.documentation.DependencyDescription
import application.documentation.Distance
import application.documentation.WireMockSupport.extractEndpointsFromEvents
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import java.net.URL

@WireMockTest
class PaymentClientTests(
    wireMockInfo: WireMockRuntimeInfo
) {

    private val description = DependencyDescription(
        id = "payment-service",
        type = ComponentType.BACKEND,
        distanceFromUs = Distance.CLOSE,
        credentials = listOf(Credentials.JWT),
    )

    private val contextPath = "/es1"
    private val wireMock = wireMockInfo.wireMock

    private val properties = PaymentServiceProperties(
        baseUrl = URL("http://localhost:${wireMockInfo.httpPort}$contextPath")
    )
    private val cut = PaymentClientConfiguration(properties).paymentClient()

    @AfterEach
    fun documentCalledEndpoints() {
        val endpoints = extractEndpointsFromEvents(wireMock, contextPath)
        createOrAmendDependencyEndpoints(description, endpoints)
    }

    @Test
    fun `document dependency`() {
        createOrReplaceDependency(description)
    }

    @Test
    fun `some test for paying with a credit card`() {
        wireMock.register(
            post(urlPathTemplate("$contextPath/payments"))
                .willReturn(
                    ok("{}")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                )
        )

        cut.processCreditCardPayment("4111111111111111", "12/24", "123")
        // test incomplete ...
    }

    @Test
    fun `some test for paying via bank transfer`() {
        wireMock.register(
            post(urlPathTemplate("$contextPath/payments"))
                .willReturn(
                    ok("{}")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                )
        )

        cut.processBankTransferPayment("DE99370400440532013000")
        // test incomplete ...
    }
}
