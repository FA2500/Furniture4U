package my.fa250.furniture4u.model

import kotlinx.serialization.Serializable

@Serializable
data class ColorResponse(
    val hex: Hex,
    val rgb: RGB
)
