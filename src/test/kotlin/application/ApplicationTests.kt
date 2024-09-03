package application

import application.documentation.ApplicationDescription
import application.documentation.ArchitectureDocumentation.createOrAmendConsumedQueue
import application.documentation.ArchitectureDocumentation.createOrReplaceApplication
import application.documentation.ArchitectureDocumentation.createOrReplaceDatabase
import application.documentation.ArchitectureDocumentation.createOrReplaceDependent
import application.documentation.ComponentType
import application.documentation.ComponentType.BACKEND
import application.documentation.ConsumedQueue
import application.documentation.DependentDescription
import application.documentation.Distance
import application.documentation.Distance.OWNED
import application.documentation.collectDatabaseDescription
import application.documentation.loadTableDescriptions
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
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.amqp.core.Binding
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.UUID.randomUUID

@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
class ApplicationTests {

    companion object {

        @JvmStatic
        @Container
        val postgres = PostgreSQLContainer("postgres:16.1-alpine")
            .withDatabaseName("test-${randomUUID()}")
            .withUsername("postgres")
            .withPassword("password")
            .withExposedPorts(5432)

        @JvmStatic
        @DynamicPropertySource
        fun registerDatasourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") {
                "jdbc:postgresql://${postgres.host}:${postgres.firstMappedPort}/${postgres.databaseName}"
            }
            registry.add("spring.datasource.username") { postgres.username }
            registry.add("spring.datasource.password") { postgres.password }
            registry.add("spring.datasource.hikari.schema") { "test" }
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    @Provider("order-service")
    @PactFolder("src/test/pacts")
    @VerificationReports("console")
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    inner class ContractTests {

        @BeforeEach
        fun setTarget(context: PactVerificationContext, @LocalServerPort port: Int) {
            context.target = HttpTestTarget("localhost", port)
        }

        @TestTemplate
        fun `consumer contract tests`(context: PactVerificationContext, request: HttpRequest) {
            createOrReplaceDependent(dependentDescription(context.consumer))
            context.verifyInteraction()
        }

        @State("order with any id exists and can be deleted")
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

    @Nested
    inner class Documentation(
        @Autowired private val jdbcClient: JdbcClient,
        @Autowired private val queueBindings: List<Binding>,
        @Value("\${spring.application.name}") private val applicationName: String,
    ) {

        @Test
        fun `generate application description`() {
            createOrReplaceApplication(
                ApplicationDescription(
                    id = applicationName,
                    type = BACKEND,
                    distanceFromUs = OWNED
                )
            )
        }

        @Test
        fun `generate consumed queues descriptions`() {
            queueBindings
                .map { binding ->
                    ConsumedQueue(binding.destination, ConsumedQueue.Binding(binding.exchange, binding.routingKey))
                }
                .forEach(::createOrAmendConsumedQueue)
        }

        @Test
        fun `generate database description`() {
            createOrReplaceDatabase(
                collectDatabaseDescription(
                    id = "order-service-db",
                    name = "Order Service Database",
                    description = "The database of the Order Service.",
                    tables = loadTableDescriptions("/documentation/order-service-db_table-descriptions.json"),
                    jdbcClient = jdbcClient,
                    schemaName = "test"
                )
            )
        }
    }
}
