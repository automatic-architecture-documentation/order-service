package application.documentation

import application.business.Event
import application.config.objectMapperForEvents
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT

private val eventObjectMapper = objectMapperForEvents()
    .enable(INDENT_OUTPUT) // pretty format the JSON examples

fun eventDescription(example: Event, description: String, fields: EventDescriptionSpec.() -> Unit): EventDescription =
    EventDescriptionSpec(example, description).apply(fields).build()

class EventDescriptionSpec(
    private val example: Event,
    private val description: String
) {
    private val fields = mutableListOf<EventDescription.Field>()

    init {
        field(
            property = "id",
            type = "UUID4",
            nullable = false,
            description = "The unique ID of the event."
        )
        field(
            property = "timestamp",
            type = "ISO-8601 Date+Time (UTC)",
            nullable = false,
            description = "The exact instant the event occurred at its source."
        )
    }

    fun field(
        property: String,
        type: String,
        nullable: Boolean = false,
        description: String? = null
    ) {
        check(fields.none { it.property == property }) { "Field '$property' is already documented." }
        fields.add(
            EventDescription.Field(
                property = property,
                type = type,
                nullable = nullable,
                description = description,
            )
        )
    }

    internal fun build() = EventDescription(
        name = example.getEventName(),
        type = example.getEventType(),
        description = description,
        example = eventObjectMapper.writeValueAsString(example),
        fields = fields
    )
}
