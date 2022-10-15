package dev.jelsik.kotlinrestapi.pub.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.data.mongodb")
data class ExtendedMongoProperties(
    /**
     * Map of additional databases - [name - database name]
     */
    var additionalDatabases: Map<String, String> = emptyMap()
)
