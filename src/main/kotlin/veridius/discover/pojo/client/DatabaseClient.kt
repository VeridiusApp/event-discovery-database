package veridius.discover.pojo.client

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import veridius.discover.models.connection.DatabaseConnectionConfiguration
import veridius.discover.pojo.connection.DatabaseConnector
import veridius.discover.pojo.state.ConnectionState
import veridius.discover.util.configuration.TableConfigurationBuilder
import java.util.*

/**
 * Todo: Ensure Configured Database Driver is supported by debezium
 */
abstract class DatabaseClient : DatabaseConnector, TableConfigurationBuilder {
    abstract val id: UUID
    abstract val config: DatabaseConnectionConfiguration

    protected val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    fun updateConnectionState(newState: ConnectionState) {
        _connectionState.value = newState
    }


    final override fun validateConfig() {
        baseConfigValidation()
        clientConfigValidation()
    }

    /**
     * Base configuration validation applicable to all database clients
     */
    protected open fun baseConfigValidation() {
        require(config.hostName.isNotBlank()) { "Hostname cannot be blank" }
        require(config.port.isNotBlank()) { "Port cannot be blank" }
        require(config.user.isNotBlank()) { "Username cannot be blank" }
        require(config.database.isNotBlank()) { "Database name cannot be blank" }
    }

    /**
     * Extendable configuration validation for specific database clients
     */
    protected open fun clientConfigValidation() {
        // No-op
    }
}
