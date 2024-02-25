package application.persistence

import application.documentation.ComponentDescription
import application.documentation.Relationship.CLOSE
import application.documentation.ComponentType.DATABASE
import application.documentation.DocumentedDependency
import org.springframework.stereotype.Component

@Component
class SomeStore : DocumentedDependency {

    override val description = ComponentDescription(
        id = "backend-service-1-database",
        contextId = "application",
        systemId = "platform",
        type = DATABASE,
        relationship = CLOSE
    )

    fun save(something: Any) {
        TODO("some kind of implementation")
    }
}
