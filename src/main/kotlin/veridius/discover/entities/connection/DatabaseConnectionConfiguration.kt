package veridius.discover.entities.connection

import veridius.discover.entities.common.DatabaseType
import veridius.discover.entities.configuration.TableMonitoringConfigurationEntity
import java.time.ZonedDateTime
import java.util.*

data class DatabaseConnectionConfiguration(
    val id: UUID,
    val instanceId: UUID,
    val connectionName: String,
    val databaseType: DatabaseType,
    var hostName: String,
    var port: String,
    var database: String?,
    var user: String?,
    var password: String?,
    var additionalProperties: ConnectionAdditionalProperties?,
    val isEnabled: Boolean,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val tableConfigurations: List<TableMonitoringConfigurationEntity> = mutableListOf()
) {
    companion object Factory {
        /**
         * Converts a Database Connection Entity fetched from storage to a Database Connection Configuration
         * In the event that the entity is not encrypted, we can safely pass through most of the fields,
         * but would need to object map the JSON Additional properties string.
         *
         * For encrypted entities, they will be left blank for the calling service to decrypt and apply the
         * associated values
         */
        fun fromEntity(
            entity: DatabaseConnectionEntity,
            requireEncryption: Boolean = true
        ): DatabaseConnectionConfiguration {
            if (requireEncryption) {
                return DatabaseConnectionConfiguration(
                    id = entity.id!!,
                    instanceId = entity.instanceId,
                    connectionName = entity.connectionName,
                    databaseType = entity.databaseType,
                    hostName = "",
                    port = "",
                    database = "",
                    user = "",
                    password = "",
                    additionalProperties = null,
                    isEnabled = false,
                    createdAt = entity.createdAt,
                    updatedAt = entity.updatedAt,
                    tableConfigurations = entity.tableMonitoringConfigurations
                )
            }
            val propertyConverter = ConnectionPropertyConverter()

            return DatabaseConnectionConfiguration(
                id = entity.id!!,
                instanceId = entity.instanceId,
                connectionName = entity.connectionName,
                databaseType = entity.databaseType,
                hostName = entity.hostName,
                port = entity.port,
                database = entity.databaseName,
                user = entity.user,
                password = entity.password,
                additionalProperties = propertyConverter.convertToEntityAttribute(entity.additionalPropertiesText),
                isEnabled = entity.isEnabled,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                tableConfigurations = entity.tableMonitoringConfigurations
            )

        }
    }
}