package application

import application.documentation.ArchitectureDocumentation.createOrReplaceDependent
import application.documentation.ComponentType
import application.documentation.DependentDescription
import application.documentation.Distance
import au.com.dius.pact.provider.IConsumerInfo
import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.State
import au.com.dius.pact.provider.junitsupport.VerificationReports
import au.com.dius.pact.provider.junitsupport.loader.PactFolder
import org.apache.hc.core5.http.HttpRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort

@TestInstance(PER_CLASS)
@Provider("backend-service-1")
@PactFolder("src/test/pacts")
@VerificationReports("console")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(PactVerificationInvocationContextProvider::class)
internal class ContractTests {

    @BeforeEach
    fun setTarget(context: PactVerificationContext, @LocalServerPort port: Int) {
        context.target = HttpTestTarget("localhost", port)
    }

    @TestTemplate
    fun `consumer contract tests`(context: PactVerificationContext, request: HttpRequest) {
        createOrReplaceDependent(dependentDescription(context.consumer))
        context.verifyInteraction()
    }

    @State("search returns empty result")
    fun noOpStates() {
        // nothing to do
    }

    private fun dependentDescription(consumer: IConsumerInfo): DependentDescription =
        when (consumer.name) {
            "frontend" -> DependentDescription(
                id = "frontend",
                type = ComponentType.FRONTEND,
                distanceFromUs = Distance.OWNED
            )

            "external-service-x" -> DependentDescription(
                id = "external-service-x",
                type = ComponentType.BACKEND,
                distanceFromUs = Distance.EXTERNAL
            )

            else -> error("Unmapped dependent [${consumer.name}]! Please add a mapping here.")
        }
}
