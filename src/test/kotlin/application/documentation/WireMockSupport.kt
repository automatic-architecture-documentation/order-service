package application.documentation

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.stubbing.ServeEvent

object WireMockSupport {

    fun extractEndpointsFromEvents(wireMock: WireMock, pathPrefix: String?): List<HttpEndpoint> =
        wireMock.serveEvents
            .filter { it.wasMatched }
            .filter { event -> event.request.url.startsWith(pathPrefix ?: "") }
            .map { event ->
                val method = getMethod(event)
                val path = getPath(event).removePrefix(pathPrefix ?: "")

                HttpEndpoint(method, path)
            }

    private fun getMethod(event: ServeEvent): String =
        event.request.method.value()

    private fun getPath(event: ServeEvent): String =
        with(event.stubMapping.request) {
            when {
                urlPathTemplate != null -> urlPathTemplate
                urlPath != null -> urlPath
                else -> error("Stubs need to be defined using either 'urlPathEqualTo' or 'urlPathTemplate'!")
            }
        }
}
