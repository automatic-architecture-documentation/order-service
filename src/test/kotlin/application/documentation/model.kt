package application.documentation

data class ApplicationDescription(
    val id: String,
    val groupId: String? = null,
    val systemId: String? = null,
    val type: ComponentType,
    val distanceFromUs: Distance,
)

data class DependentDescription(
    val id: String,
    val groupId: String? = null,
    val systemId: String? = null,
    val type: ComponentType,
    val distanceFromUs: Distance,
)

data class DependencyDescription(
    val id: String,
    val groupId: String? = null,
    val systemId: String? = null,
    val type: ComponentType,
    val distanceFromUs: Distance,
    val credentials: List<Credentials> = emptyList(),
)

enum class ComponentType { BACKEND, FRONTEND, DATABASE }
enum class Distance { OWNED, CLOSE, EXTERNAL }

data class HttpEndpoint(val method: String, val path: String)
enum class Credentials { JWT, BASIC_AUTH }
