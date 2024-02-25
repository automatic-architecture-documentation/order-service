package application.external

import application.documentation.ComponentDescription
import application.documentation.Relationship.CLOSE
import application.documentation.ComponentType.BACKEND
import application.documentation.DocumentedDependency
import org.springframework.stereotype.Component

@Component
class ExternalService1Client : DocumentedDependency {

    override val description = ComponentDescription(
        id = "external-service-1",
        systemId = "platform",
        type = BACKEND,
        relationship = CLOSE
    )

    fun getSomething(): String {
        TODO("some kind of implementation")
    }
}
