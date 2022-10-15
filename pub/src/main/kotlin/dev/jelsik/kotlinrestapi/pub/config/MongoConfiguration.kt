package dev.jelsik.kotlinrestapi.pub.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories



@Configuration
class MongoConfiguration(
    @Value("\${spring.data.mongodb.uri}")
    val uri: String,
    @Value("\${spring.data.mongodb.database}")
    val dbName: String,
    private val extendedMongoProperties: ExtendedMongoProperties


) : AbstractMongoClientConfiguration() {


    override fun getDatabaseName(): String = dbName

    /**
     * Configures primary MongoTemplate (based on spring.data.mongodb.database)
     */
    @Primary
    @Bean
    override fun mongoTemplate(databaseFactory: MongoDatabaseFactory, converter: MappingMongoConverter): MongoTemplate =
        super.mongoTemplate(databaseFactory, converter)

    /**
     * Configures additional MongoTemplate for [SECOND_DB] (based on spring.data.mongodb.additional-databases)
     */
    @Bean(SECOND_DB)
    fun mongoTemplateSecondaryCollection(mongoClient: MongoClient, converter: MappingMongoConverter): MongoTemplate {
        return createAdditionalMongoTemplate(SECOND_DB, mongoClient, converter)
    }

    private fun createAdditionalMongoTemplate(
        additionalDatabaseConfigKey: String,
        mongoClient: MongoClient,
        converter: MappingMongoConverter
    ): MongoTemplate {
        val databaseName = extendedMongoProperties.additionalDatabases
            .getOrElse(additionalDatabaseConfigKey) { throw IllegalStateException("Additional databaseName[$$additionalDatabaseConfigKey] is not configured!") }
        converter.setTypeMapper(DefaultMongoTypeMapper(null))
        val factory = SimpleMongoClientDatabaseFactory(mongoClient, databaseName)
        return MongoTemplate(factory, converter)
    }

    /**
     * Instantiate singleton MongoClient
     */
    @Bean
    override fun mongoClient(): MongoClient = super.mongoClient()

    /**
     * Configures MongoClient settings
     */
    override fun configureClientSettings(builder: MongoClientSettings.Builder) {

        builder.applyConnectionString(ConnectionString(uri))


    }


    companion object {
        const val SECOND_DB = "second_db"
    }
}

/**
 * Primary configuration for Mongo
 */
@Configuration
@AutoConfigureAfter(MongoConfiguration::class)
@EnableMongoRepositories(
    basePackages = ["dev.jelsik.kotlinrestapi.pub.repository.primary"],
    mongoTemplateRef = "mongoTemplate"
)
class PrimaryRepositoryConfiguration

/**
 * [MongoConfiguration.SECOND_DB] configuration
 */
@Configuration
@AutoConfigureAfter(MongoConfiguration::class)
@EnableMongoRepositories(
    basePackages = ["dev.jelsik.kotlinrestapi.pub.repository.secondary"],
    mongoTemplateRef = MongoConfiguration.SECOND_DB
)
class SecondaryMongoRepositoryConfiguration
