package application.external

import application.documentation.ComponentDescription
import application.documentation.ComponentType.BACKEND
import application.documentation.Distance.OWNED
import application.documentation.DocumentedDependency
import org.springframework.stereotype.Component

@Component
class BackendService2Client : DocumentedDependency {

    override val description = ComponentDescription(
        id = "backend-service-2",
        contextId = "application",
        systemId = "platform",
        type = BACKEND,
        distanceFromUs = OWNED
    )

    fun getSomething(): String {
        TODO("some kind of implementation")
    }
}
