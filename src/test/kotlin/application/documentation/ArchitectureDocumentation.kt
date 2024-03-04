package application.documentation

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream

object ArchitectureDocumentation {

    private val rootFolder = File("build/architecture-documentation")

    // DO NOT INDENT OUTPUT -> JSON List format is 1 JSON per LINE!
    private val objectMapper = jacksonObjectMapper()
        .setSerializationInclusion(NON_EMPTY)

    private val lock = Any()

    fun createOrReplaceApplication(description: ComponentDescription) {
        val file = File(rootFolder, description.id + ".json")

        createOrReplaceFile(file) {
            write(toJsonString(description))
        }
    }

    fun createOrReplaceDependent(description: ComponentDescription) {
        val folder = File(rootFolder, "dependents")
        val dependencyFile = File(folder, description.id + ".json")

        createOrReplaceFile(dependencyFile) {
            write(toJsonString(description))
        }
    }

    fun createOrReplaceDependency(description: ComponentDescription) {
        val folder = File(rootFolder, "dependencies")
        val file = File(folder, description.id + ".json")

        createOrReplaceFile(file) {
            write(toJsonString(description))
        }
    }

    fun createOrAmendDependencyEndpoints(description: ComponentDescription, endpoints: List<HttpEndpoint>) {
        val folder = File(rootFolder, "dependencies/http-endpoints")
        val file = File(folder, description.id + ".jsonl")

        synchronized(lock) {
            createOrAppendFile(file) {
                endpoints.forEach { endpoint ->
                    write(toJsonString(endpoint))
                    newLine()
                }
            }
        }
    }

    private fun toJsonString(value: Any): String =
        objectMapper.writeValueAsString(value)


    private fun createOrAppendFile(file: File, writer: BufferedWriter.() -> Unit) {
        file.parentFile.mkdirs()
        FileOutputStream(file, true).use { it.bufferedWriter().use(writer) }
    }

    private fun createOrReplaceFile(file: File, writer: BufferedWriter.() -> Unit) {
        file.parentFile.mkdirs()
        FileOutputStream(file, true).use { it.bufferedWriter().use(writer) }
    }
}
