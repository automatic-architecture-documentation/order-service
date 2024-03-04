package application

import application.documentation.ArchitectureDocumentation.createOrReplaceApplication
import application.documentation.ArchitectureDocumentation.createOrReplaceDependent
import application.documentation.ComponentDescription
import application.documentation.ComponentType.BACKEND
import application.documentation.ComponentType.FRONTEND
import application.documentation.Distance.OWNED
import org.junit.jupiter.api.Test


class ArchitectureDocumentationTests {

    @Test
    fun `generate application description`() {
        createOrReplaceApplication(
            ComponentDescription(
                id = "backend-service-1",
                groupId = "application",
                systemId = "platform",
                type = BACKEND,
                distanceFromUs = OWNED
            )
        )
    }

    @Test
    fun `generate dependents description`() {
        createOrReplaceDependent(
            ComponentDescription(
                id = "frontend",
                groupId = "application",
                systemId = "platform",
                type = FRONTEND,
                distanceFromUs = OWNED,
            )
        )
    }
}
