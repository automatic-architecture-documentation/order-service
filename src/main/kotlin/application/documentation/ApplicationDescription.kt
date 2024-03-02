package application.documentation

data class ApplicationDescription(
    val id: String,
    val contextId: String?,
    val systemId: String?,
    val type: ComponentType,
    val dependents: List<ComponentDescription>,
    val dependencies: List<ComponentDescription>,
) {
    val distanceFromUs: Distance = Distance.OWNED
}
