package application.external

import application.documentation.*
import application.documentation.ArchitectureDocumentation.createOrAmendDependencyEndpoints
import application.documentation.ArchitectureDocumentation.createOrReplaceDependency
import application.documentation.WireMockSupport.extractEndpointsFromEvents
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import java.net.URL

@WireMockTest
class InventoryClientTests(
    wireMockInfo: WireMockRuntimeInfo
) {

    private val description = DependencyDescription(
        id = "inventory-service",
        type = ComponentType.BACKEND,
        distanceFromUs = Distance.OWNED,
        credentials = listOf(Credentials.JWT),
    )

    private val contextPath = "/bs2"
    private val wireMock = wireMockInfo.wireMock

    private val properties = InventoryServiceProperties(
        baseUrl = URL("http://localhost:${wireMockInfo.httpPort}$contextPath")
    )
    private val cut = InventoryClientConfiguration(properties).inventoryClient()

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
    fun `some test for getting inventory information of a product`() {
        wireMock.register(
            get(urlPathTemplate("$contextPath/inventory/{productId}"))
                .withPathParam("productId", equalTo("4ab8c691-0a49-45e2-8290-e6942bd735da"))
                .willReturn(
                    ok("{}")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                )
        )

        cut.getProductInventory("4ab8c691-0a49-45e2-8290-e6942bd735da")
        // test incomplete ...
    }

    @Test
    fun `some test to reserve an amount of product in the inventory`() {
        wireMock.register(
            post(urlPathTemplate("$contextPath/inventory/{productId}/reserve"))
                .withPathParam("productId", equalTo("cda81952-f3d9-4be1-9f0b-71e2bd34528d"))
                .willReturn(
                    ok("{}")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                )
        )

        cut.reserveProductInventory("cda81952-f3d9-4be1-9f0b-71e2bd34528d", 3)
        // test incomplete ...
    }
}
