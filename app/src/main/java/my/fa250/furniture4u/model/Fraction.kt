package my.fa250.furniture4u.model

import kotlinx.serialization.Serializable

@Serializable
data class Fraction(
    val r: Float,
    val g: Float,
    val b: Float
)
