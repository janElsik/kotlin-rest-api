package dev.jelsik.kotlinrestapi.pub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class PubApplication

fun main(args: Array<String>) {
    runApplication<PubApplication>(*args)
}
