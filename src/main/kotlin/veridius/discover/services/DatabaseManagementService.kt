package veridius.discover.services

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import mu.KotlinLogging
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import veridius.discover.configuration.properties.DatabaseConfigurationProperties
import veridius.discover.configuration.properties.DatabaseConfigurationProperties.*
import veridius.discover.services.connection.DatabaseConnectionManager



@Service
class DatabaseManagementService(
    private val connectionManager: DatabaseConnectionManager,
    private val configuration: DatabaseConfigurationProperties
) {

    private val logger = KotlinLogging.logger {}

    @Bean
    fun initializeDatabases() = ApplicationRunner {
        configuration.databases.forEach{config ->
            connectionManager.createConnection(config)
        }

//        CoroutineScope(Dispatchers.IO).launch {
//            configurations.forEach { config ->
//                connectionManager.createConnection(config)
//            }
//
//            // Start connection monitoring
//            connectionManager.monitorConnections()
//
//            // Observe connection states
//            connectionManager.observeConnectionStates()
//                .collect { states ->
//                    states.forEach { (id, state) ->
//                        when (state) {
//                            is ConnectionState.Error -> logger.error("Connection $id error: ${state.exception}")
//                            is ConnectionState.Connected -> logger.info("Connection $id established")
//                            is ConnectionState.Disconnected -> logger.info("Connection $id disconnected")
//                            is ConnectionState.Connecting -> logger.info("Connection $id connecting...")
//                        }
//                    }
//                }
//        }
    }

}