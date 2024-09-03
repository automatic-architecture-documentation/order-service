package application.documentation

data class ApplicationDescription(
    val id: String,
    val type: ComponentType,
    val distanceFromUs: Distance,
)

data class DependentDescription(
    val id: String,
    val type: ComponentType,
    val distanceFromUs: Distance,
)

data class DependencyDescription(
    val id: String,
    val type: ComponentType,
    val distanceFromUs: Distance,
    val credentials: List<Credentials> = emptyList(),
)

enum class ComponentType { BACKEND, FRONTEND, DATABASE }
enum class Distance { OWNED, CLOSE, EXTERNAL }
enum class Credentials { JWT, BASIC_AUTH }

data class HttpEndpoint(val method: String, val path: String)

data class EventDescription(
    val name: String,
    val type: String,
    val description: String,
    val example: String,
    val fields: List<Field>,
) {
    data class Field(
        val property: String,
        val type: String,
        val nullable: Boolean,
        val description: String?,
    )
}

data class PublishedMessage(
    val exchange: String,
    val routingKeys: List<String>,
) {
    constructor(name: String, routingKey: String) : this(name, listOf(routingKey))
}

data class ConsumedQueue(
    val name: String,
    val bindings: List<Binding>,
) {
    constructor(name: String, binding: Binding) : this(name, listOf(binding))

    data class Binding(
        val exchange: String,
        val routingKeyPattern: String,
    )
}

data class DatabaseDescription(
    val id: String,
    val name: String,
    val type: String,
    val description: String?,
    val tables: List<Table>,
) {
    data class Table(
        val name: String,
        val description: String?,
        val columns: List<Column>,
    )

    data class Column(
        val name: String,
        val dataType: String,
        val defaultValue: String?,
        val nullable: Boolean,
        val description: String?,
        val partOfPrimaryKey: Boolean,
    )
}
