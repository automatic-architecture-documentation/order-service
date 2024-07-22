package application.config

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * Produces an [ObjectMapper] intended for usage when (de)serializing events.
 */
fun objectMapperForEvents(): ObjectMapper = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .disable(FAIL_ON_UNKNOWN_PROPERTIES)
    .disable(FAIL_ON_IGNORED_PROPERTIES)
    .disable(WRITE_DATES_AS_TIMESTAMPS)
    .disable(WRITE_DURATIONS_AS_TIMESTAMPS)
    .enable(FAIL_ON_NULL_FOR_PRIMITIVES)
    .setDefaultPropertyInclusion(NON_NULL)
