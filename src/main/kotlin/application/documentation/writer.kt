package application.documentation

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.io.FileOutputStream

private val objectMapper = jacksonObjectMapper()
    .enable(SerializationFeature.INDENT_OUTPUT)
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)

fun exportApplicationDescription(application: ApplicationDescription) {
    val json = objectMapper.writeValueAsBytes(application)

    val outputFolder = File("build/documentation/json").also(File::mkdirs)
    val outputFile = File(outputFolder, application.id + ".json")

    FileOutputStream(outputFile, false).use { it.write(json) }
}
