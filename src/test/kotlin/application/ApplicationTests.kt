package application

import application.documentation.ApplicationDescription
import application.documentation.ComponentDescription
import application.documentation.ComponentType.BACKEND
import application.documentation.ComponentType.FRONTEND
import application.documentation.Distance.OWNED
import application.documentation.DocumentedDependency
import application.documentation.exportApplicationDescription
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationTests {

    @Test
    fun generateServiceDescription(
        @Autowired dependencies: List<DocumentedDependency>,
        @Value("\${spring.application.name}") applicationName: String
    ) {
        exportApplicationDescription(
            ApplicationDescription(
                id = applicationName,
                contextId = "application",
                systemId = "platform",
                type = BACKEND,
                dependents = listOf(
                    ComponentDescription(
                        id = "frontend",
                        contextId = "application",
                        systemId = "platform",
                        type = FRONTEND,
                        distanceFromUs = OWNED,
                    )
                ),
                dependencies = dependencies.map(DocumentedDependency::description)
            )
        )
    }
}
