package application.external

import application.documentation.ArchitectureDocumentation.createOrAmendDependencyEndpoints
import application.documentation.ArchitectureDocumentation.createOrReplaceDependency
import application.documentation.ComponentType
import application.documentation.Credentials
import application.documentation.DependencyDescription
import application.documentation.Distance
import application.documentation.WireMockSupport.extractEndpointsFromEvents
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.net.URL

@WireMockTest
class ExternalService1ClientTests(
    wireMockInfo: WireMockRuntimeInfo
) {

    private val description = DependencyDescription(
        id = "external-service-1",
        type = ComponentType.BACKEND,
        distanceFromUs = Distance.CLOSE,
        credentials = listOf(Credentials.JWT),
    )

    private val contextPath = "/es1"
    private val wireMock = wireMockInfo.wireMock

    private val properties = ExternalService1Properties(
        baseUrl = URL("http://localhost:${wireMockInfo.httpPort}$contextPath")
    )
    private val cut = ExternalService1Configuration(properties).externalService1Client()

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
            get(urlPathTemplate("$contextPath/invoices/{userId}/{state}"))
                .withPathParam("userId", equalTo("4ab8c691-0a49-45e2-8290-e6942bd735da"))
                .withPathParam("state", equalTo("open"))
                .willReturn(
                    ok("hello-world!")
                )
        )

        val result = cut.getOpenInvoices("4ab8c691-0a49-45e2-8290-e6942bd735da")

        assertThat(result).isEqualTo("hello-world!")
    }

    @Test
    fun test2() {
        wireMock.register(
            get(urlPathTemplate("$contextPath/invoices/{userId}/{state}"))
                .withPathParam("userId", equalTo("cda81952-f3d9-4be1-9f0b-71e2bd34528d"))
                .withPathParam("state", equalTo("open"))
                .willReturn(
                    ok("hello-world!")
                )
        )

        val result1 = cut.getOpenInvoices("cda81952-f3d9-4be1-9f0b-71e2bd34528d")
        val result2 = cut.getOpenInvoices("cda81952-f3d9-4be1-9f0b-71e2bd34528d")

        assertThat(result1).isEqualTo("hello-world!")
        assertThat(result2).isEqualTo("hello-world!")
    }
}
