package dev.jelsik.kotlinrestapi.pub.service

import dev.jelsik.kotlinrestapi.pub.model.primary.PubCustomers
import dev.jelsik.kotlinrestapi.pub.repository.primary.PubCustomersRepo
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}
//handler for the application - is used as a service component and stands between repos and the controller

@Service
class PubHandler(
    private val pubCustomersRepo: PubCustomersRepo
) {
    fun newCustomer(
        id: String,
        firstName: String,
        lastName: String,
        age: Int
    ) {
        kotlin.runCatching {
            val newCustomer = PubCustomers(id, firstName, lastName, age)
            pubCustomersRepo.save(newCustomer)
        }.onFailure {
            logger.error("error occurred during saving new cusomer!", it)
        }.onSuccess {
            logger.info("new customer entered to the system!")
        }

    }

    fun checkForExistingCustomer(id: String): Boolean {
        return pubCustomersRepo.findByIdOrNull(id) == null
    }


}
