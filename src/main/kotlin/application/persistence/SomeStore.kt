package application.persistence

import application.documentation.ComponentDescription
import application.documentation.ComponentType.DATABASE
import application.documentation.Distance.CLOSE
import application.documentation.DocumentedDependency
import org.springframework.stereotype.Component

@Component
class SomeStore : DocumentedDependency {

    override val description = ComponentDescription(
        id = "backend-service-1-database",
        contextId = "application",
        systemId = "platform",
        type = DATABASE,
        distanceFromUs = CLOSE
    )

    fun save(something: Any) {
        TODO("some kind of implementation")
    }
}
