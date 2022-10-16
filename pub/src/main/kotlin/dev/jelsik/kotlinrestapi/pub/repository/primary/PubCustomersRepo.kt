package dev.jelsik.kotlinrestapi.pub.repository.primary

import dev.jelsik.kotlinrestapi.pub.model.primary.PubCustomers
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PubCustomersRepo: MongoRepository<PubCustomers, String>
