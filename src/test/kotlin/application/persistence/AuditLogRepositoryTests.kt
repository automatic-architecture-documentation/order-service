package application.persistence

import application.documentation.ArchitectureDocumentation.createOrReplaceDependency
import application.documentation.ComponentType.DATABASE
import application.documentation.DependencyDescription
import application.documentation.Distance.OWNED
import org.junit.jupiter.api.Test

class AuditLogRepositoryTests {

    private val description = DependencyDescription(
        id = "backend-service-1-database",
        type = DATABASE,
        distanceFromUs = OWNED
    )

    @Test
    fun `document dependency`() {
        createOrReplaceDependency(description)
    }
}
