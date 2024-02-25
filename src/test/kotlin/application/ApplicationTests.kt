package application

import application.documentation.ComponentDescription
import application.documentation.ComponentType.BACKEND
import application.documentation.ComponentType.FRONTEND
import application.documentation.DocumentedDependency
import application.documentation.Relationship.OWNED
import application.documentation.RootComponentDescription
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.io.FileOutputStream

@SpringBootTest
class ApplicationTests {

    @Test
    fun generateServiceDescription(@Autowired dependencies: List<DocumentedDependency>) {
        val objectMapper = jacksonObjectMapper()
            .enable(INDENT_OUTPUT)
            .setSerializationInclusion(NON_NULL)

        val description = RootComponentDescription(
            id = "backend-service-1",
            contextId = "application",
            systemId = "platform",
            type = BACKEND,
            relationship = OWNED,
            dependents = listOf(
                ComponentDescription(
                    id = "frontend",
                    contextId = "application",
                    systemId = "platform",
                    type = FRONTEND,
                    relationship = OWNED,
                )
            ),
            dependencies = dependencies.map { it.description }
        )

        val json = objectMapper.writeValueAsBytes(description)

        val outputFolder = File("build/documentation/json").also(File::mkdirs)
        val outputFile = File(outputFolder, "backend-service-1.json")

        FileOutputStream(outputFile, false).use { it.write(json) }
    }
}
