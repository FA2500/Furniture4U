package my.fa250.furniture4u.ml.classification.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import java.io.ByteArrayOutputStream

object ImageUtils {
    fun rotateBitmap(bitmap: Bitmap, rotation: Int): Bitmap {
        if (rotation == 0) return bitmap

        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
    }


    /**
     * Converts a [Bitmap] to [ByteArray] using [Bitmap.compress].
     */
    fun Bitmap.toByteArray(): ByteArray = ByteArrayOutputStream().use { stream ->
        this.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.toByteArray()
    }
}