package application.external

import application.business.Order
import application.business.OrderStatus
import application.documentation.ArchitectureDocumentation.createOrAmendDependencyEndpoints
import application.documentation.ArchitectureDocumentation.createOrReplaceDependency
import application.documentation.ComponentType
import application.documentation.Credentials
import application.documentation.DependencyDescription
import application.documentation.Distance
import application.documentation.WireMockSupport.extractEndpointsFromEvents
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
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
import java.util.UUID

@WireMockTest
class NotificationClientTests(
    wireMockInfo: WireMockRuntimeInfo
) {

    private val description = DependencyDescription(
        id = "notification-service",
        type = ComponentType.BACKEND,
        distanceFromUs = Distance.CLOSE,
        credentials = listOf(Credentials.JWT),
    )

    private val contextPath = "/bs3"
    private val wireMock = wireMockInfo.wireMock

    private val properties = NotificationServiceProperties(
        baseUrl = URL("http://localhost:${wireMockInfo.httpPort}$contextPath")
    )
    private val cut = NotificationClientConfiguration(properties).notificationClient()

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
    fun `some test to trigger a order confirmation notification for the customer`() {
        wireMock.register(
            post(urlPathTemplate("$contextPath/notifications/customer/{customerId}/order-confirmation"))
                .withPathParam("customerId", equalTo("bdb446f8-766a-407f-87d3-9a4fe735798a"))
                .willReturn(
                    ok("{}")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                )
        )

        val order = Order(
            orderId = UUID.fromString("680f28bf-45c1-4dd5-878b-3af30fbe131e"),
            customerId = UUID.fromString("bdb446f8-766a-407f-87d3-9a4fe735798a"),
            status = OrderStatus.PLACED
        )

        cut.confirmOrder(order)
        // test incomplete ...
    }
}
