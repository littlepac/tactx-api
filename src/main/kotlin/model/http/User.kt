package org.example.model.http

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: String,
    val email: String,
    val userName: String,
    val active: Boolean
)