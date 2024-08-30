package application

import application.documentation.ApplicationDescription
import application.documentation.ArchitectureDocumentation.createOrAmendConsumedQueue
import application.documentation.ArchitectureDocumentation.createOrReplaceApplication
import application.documentation.ComponentType.BACKEND
import application.documentation.ConsumedQueue
import application.documentation.Distance.OWNED
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.amqp.core.Binding
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationTests {

    @Nested
    inner class Documentation(
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
    }
}
