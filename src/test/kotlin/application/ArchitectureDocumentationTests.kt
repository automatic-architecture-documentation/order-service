package application

import application.documentation.ApplicationDescription
import application.documentation.ArchitectureDocumentation.createOrReplaceApplication
import application.documentation.ComponentType.BACKEND
import application.documentation.Distance.OWNED
import org.junit.jupiter.api.Test


class ArchitectureDocumentationTests {

    @Test
    fun `generate application description`() {
        createOrReplaceApplication(
            ApplicationDescription(
                id = "backend-service-1",
                groupId = "application",
                systemId = "platform",
                type = BACKEND,
                distanceFromUs = OWNED
            )
        )
    }
}
