package application.documentation

data class ComponentDescription(
    val id: String,
    val contextId: String? = null,
    val systemId: String? = null,
    val type: ComponentType? = null,
    val relationship: Relationship? = null,
) {
}
