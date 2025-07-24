package org.example.model
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: String,
    val email: String,
    val username: String,
    val active: Boolean
)