package my.fa250.furniture4u.model

import kotlinx.serialization.Serializable

@Serializable
data class Hex(
    val value: String,
    val clean: String,
    val contrast: String
)
