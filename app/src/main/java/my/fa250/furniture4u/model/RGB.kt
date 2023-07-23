package my.fa250.furniture4u.model

import kotlinx.serialization.Serializable

@Serializable
data class RGB(
    val fraction: Fraction,
    val r: Int,
    val g: Int,
    val b: Int
)
