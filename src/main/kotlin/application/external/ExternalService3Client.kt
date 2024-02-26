package application.external

import application.documentation.ComponentDescription
import application.documentation.ComponentType.BACKEND
import application.documentation.DocumentedDependency
import application.documentation.Relationship.EXTERNAL
import org.springframework.stereotype.Component

@Component
class ExternalService3Client : DocumentedDependency {

    override val description = ComponentDescription(
        id = "external-service-3",
        systemId = "other-project",
        type = BACKEND,
        relationship = EXTERNAL
    )

    fun getSomething(): String {
        TODO("some kind of implementation")
    }
}
