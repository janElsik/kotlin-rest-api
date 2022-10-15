package dev.jelsik.kotlinrestapi.pub.controller

import dev.jelsik.kotlinrestapi.pub.service.PubHandler
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/app/visit-pub")
class PubRestController(
    private val pubHandler: PubHandler
) {

    @PostMapping("/new-customer")
    fun pubMembershipCreation(
        @RequestParam id: String,
        @RequestParam firstName: String,
        @RequestParam lastName: String,
        @RequestParam age: Int
    ): ResponseEntity<String> {
        return if (pubHandler.checkForExistingCustomer(id)) {
            logger.info { "your credentials are:\n id:[$id] \n first name: [$firstName] \n last name: [$lastName] \n age: [$age]" }
            pubHandler.newCustomer(id, firstName, lastName, age)
            ResponseEntity.ok("new customer created!")
        } else {
            logger.error("customer with id [$id] already exists!")
            ResponseEntity.badRequest().body("customer with id [$id] already exists!")
        }

    }

}
