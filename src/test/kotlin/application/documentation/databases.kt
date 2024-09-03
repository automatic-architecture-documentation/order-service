package application.documentation

import application.documentation.DatabaseDescription.Column
import application.documentation.DatabaseDescription.Table
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.simple.JdbcClient

private val objectMapper = jacksonObjectMapper()
    .disable(FAIL_ON_UNKNOWN_PROPERTIES)

fun loadTableDescriptions(fileOnClassPath: String): List<TableDescription> {
    val resource = ClassPathResource(fileOnClassPath)
    return objectMapper.readValue(resource.contentAsByteArray)
}

data class TableDescription(
    val tableName: String,
    val description: String,
    val columns: List<ColumnDescription>,
)

data class ColumnDescription(
    val columnName: String,
    val description: String,
)

fun collectDatabaseDescription(
    id: String,
    name: String,
    description: String,
    tables: List<TableDescription>,
    jdbcClient: JdbcClient,
    schemaName: String,
) = DatabaseDescription(
    id = id,
    name = name,
    type = "PostgreSQL",
    description = description,
    tables = getTables(tables, jdbcClient, schemaName)
)

private fun getTables(descriptions: List<TableDescription>, jdbcClient: JdbcClient, schemaName: String): List<Table> =
    getColumnData(jdbcClient, schemaName)
        .groupBy(ColumnData::tableName)
        .map { (tableName, columns) ->
            val primaryKeyColumns = getPrimaryKeyColumns(jdbcClient, schemaName, tableName)
            val td = descriptions.firstOrNull() { it.tableName == tableName }
            Table(
                name = tableName,
                description = td?.description,
                columns = columns
                    .sortedBy(ColumnData::columnPosition)
                    .map { column ->
                        val cd = td?.columns?.firstOrNull { it.columnName == column.columnName }
                        Column(
                            name = column.columnName,
                            dataType = column.dataType,
                            defaultValue = column.defaultValue,
                            nullable = column.nullable,
                            description = cd?.description,
                            partOfPrimaryKey = column.columnName in primaryKeyColumns
                        )
                    }
            )
        }
        .sortedBy(Table::name)

private fun getColumnData(jdbcClient: JdbcClient, schemaName: String) =
    jdbcClient
        .sql(
            """
            SELECT * FROM information_schema.columns
            WHERE table_schema = :schemaName
            AND   table_name NOT LIKE 'flyway_%'
            ORDER BY ordinal_position
            """
        )
        .param("schemaName", schemaName)
        .query { rs, _ ->
            ColumnData(
                tableName = rs.getString("table_name"),
                columnPosition = rs.getInt("ordinal_position"),
                columnName = rs.getString("column_name"),
                dataType = buildString {
                    append(rs.getString("data_type"))
                    val characterLength = rs.getString("character_maximum_length")
                    if (characterLength != null) {
                        append(" ($characterLength)")
                    }
                },
                defaultValue = rs.getString("column_default"),
                nullable = rs.getBoolean("is_nullable")
            )
        }
        .list()

private fun getPrimaryKeyColumns(jdbcClient: JdbcClient, schemaName: String, tableName: String) =
    jdbcClient
        .sql(
            """
            -- based on https://wiki.postgresql.org/wiki/Retrieve_primary_key_columns
            SELECT a.attname AS column_name
            FROM pg_index i
            JOIN pg_attribute a ON a.attrelid = i.indrelid AND a.attnum = ANY (i.indkey)
            WHERE i.indrelid = '$schemaName.$tableName'::regclass AND i.indisprimary
            """.trimIndent()
        )
        .query { rs, _ -> rs.getString("column_name") }
        .set()

private data class ColumnData(
    val tableName: String,
    val columnPosition: Int,
    val columnName: String,
    val dataType: String,
    val defaultValue: String?,
    val nullable: Boolean
)
