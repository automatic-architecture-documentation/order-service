package application.documentation

data class RootComponentDescription(
    val id: String,
    val contextId: String?,
    val systemId: String?,
    val type: ComponentType?,
    val relationship: Relationship?,
    val dependents: List<ComponentDescription>,
    val dependencies: List<ComponentDescription>,
)
