package application.external

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import kotlin.jvm.optionals.getOrNull

@ExtendWith(TestExtension::class)
class JunitExperimentTests {

    @Test
    fun level0_1() {
        println("level 0.1")
    }

    @Test
    fun level0_2() {
        println("level 0.2")
    }

    @Nested
    inner class Nested1 {

        @Test
        fun level1_1() {
            println("level 1.1")
        }

        @Test
        fun level1_2() {
            println("level 1.2")
        }
    }
}

class TestExtension : BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback {

    override fun beforeAll(context: ExtensionContext) {
        if(context.parent.getOrNull() == context.root) {
            println("TestExtension: beforeAll - only one")
        }

        val parentContext = context.parent.getOrNull()
        val rootContext = context.root
        println("TestExtension: beforeAll - parent: $parentContext; root: $rootContext")
    }

    override fun beforeEach(context: ExtensionContext) {
        val parentContext = context.parent.getOrNull()
        val rootContext = context.root
        println("TestExtension: beforeEach - parent: $parentContext; root: $rootContext")
    }

    override fun afterEach(context: ExtensionContext) {
        val parentContext = context.parent.getOrNull()
        val rootContext = context.root
        println("TestExtension: afterEach - parent: $parentContext; root: $rootContext")
    }

    override fun afterAll(context: ExtensionContext) {
        if(context.parent.getOrNull() == context.root) {
            println("TestExtension: afterAll - only one")
        }
        val parentContext = context.parent.getOrNull()
        val rootContext = context.root
        println("TestExtension: afterAll - parent: $parentContext; root: $rootContext")
    }
}
