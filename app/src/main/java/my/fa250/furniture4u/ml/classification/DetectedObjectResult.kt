package my.fa250.furniture4u.ml.classification

data class DetectedObjectResult(
    val confidence: Float,
    val label: String,
    val centerCoordinate: Pair<Int, Int>
)