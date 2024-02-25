package application.external

import application.documentation.ComponentDescription
import application.documentation.Relationship.OWNED
import application.documentation.ComponentType.BACKEND
import application.documentation.DocumentedDependency
import org.springframework.stereotype.Component

@Component
class BackendService2Client : DocumentedDependency {

    override val description = ComponentDescription(
        id = "backend-service-2",
        contextId = "application",
        systemId = "platform",
        type = BACKEND,
        relationship = OWNED
    )

    fun getSomething(): String {
        TODO("some kind of implementation")
    }
}
