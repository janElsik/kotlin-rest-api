package dev.jelsik.kotlinrestapi.pub.model.primary

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document("pub_customers")
data class PubCustomers(
    @Id
    var id: String,
    var firstName: String,
    var lastName: String,
    var age: Int,
)
