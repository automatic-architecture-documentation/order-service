package application.documentation

data class ComponentDescription(
    val id: String,
    val groupId: String? = null,
    val systemId: String? = null,
    val type: ComponentType,
    val distanceFromUs: Distance,
)

enum class ComponentType { BACKEND, FRONTEND, DATABASE }
enum class Distance { OWNED, CLOSE, EXTERNAL }

data class HttpEndpoint(val method: String, val path: String)
