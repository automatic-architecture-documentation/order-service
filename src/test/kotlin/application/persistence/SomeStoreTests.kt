package application.persistence

import application.documentation.ArchitectureDocumentation.createOrReplaceDependency
import application.documentation.ComponentDescription
import application.documentation.ComponentType.DATABASE
import application.documentation.Distance.OWNED
import org.junit.jupiter.api.Test

class SomeStoreTests {

    private val description = ComponentDescription(
        id = "backend-service-1-database",
        groupId = "application",
        systemId = "platform",
        type = DATABASE,
        distanceFromUs = OWNED
    )

    @Test
    fun `document dependency`() {
        createOrReplaceDependency(description)
    }
}
