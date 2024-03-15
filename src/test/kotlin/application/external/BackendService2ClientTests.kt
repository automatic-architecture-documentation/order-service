package application.external

import application.documentation.*
import application.documentation.ArchitectureDocumentation.createOrAmendDependencyEndpoints
import application.documentation.ArchitectureDocumentation.createOrReplaceDependency
import application.documentation.WireMockSupport.extractEndpointsFromEvents
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.net.URL

@WireMockTest
class BackendService2ClientTests(
    wireMockInfo: WireMockRuntimeInfo
) {

    private val description = DependencyDescription(
        id = "backend-service-2",
        type = ComponentType.BACKEND,
        distanceFromUs = Distance.OWNED,
        credentials = listOf(Credentials.JWT),
    )

    private val contextPath = "/bs2"
    private val wireMock = wireMockInfo.wireMock

    private val properties = BackendService2Properties(
        baseUrl = URL("http://localhost:${wireMockInfo.httpPort}$contextPath")
    )
    private val cut = BackendService2Configuration(properties).backendService2Client()

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
    fun test1() {
        wireMock.register(
            get(urlPathTemplate("$contextPath/bookings/{userId}"))
                .withPathParam("userId", equalTo("4ab8c691-0a49-45e2-8290-e6942bd735da"))
                .willReturn(
                    ok("hello-world!")
                )
        )

        val result = cut.getBookingsOfUser("4ab8c691-0a49-45e2-8290-e6942bd735da")

        assertThat(result).isEqualTo("hello-world!")
    }

    @Test
    fun test2() {
        wireMock.register(
            get(urlPathTemplate("$contextPath/bookings/{userId}"))
                .withPathParam("userId", equalTo("cda81952-f3d9-4be1-9f0b-71e2bd34528d"))
                .willReturn(
                    ok("hello-world!")
                )
        )

        val result = cut.getBookingsOfUser("cda81952-f3d9-4be1-9f0b-71e2bd34528d")
        assertThat(result).isEqualTo("hello-world!")
    }
}
