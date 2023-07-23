package my.fa250.furniture4u.ml.classification

import android.app.Activity
import android.graphics.Color
import android.media.Image
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.vision.v1.*
import com.google.protobuf.ByteString
import my.fa250.furniture4u.UserContextInfo
import my.fa250.furniture4u.ar.helper.SnackbarHelper
import my.fa250.furniture4u.ml.ContextActivity
import my.fa250.furniture4u.ml.classification.utils.ImageUtils
import my.fa250.furniture4u.ml.classification.utils.ImageUtils.toByteArray
import com.google.cloud.vision.v1.Image as GCVImage


class GoogleCloudVisionImage(val activity: ContextActivity) : ObjectDetector(activity) {
    companion object {
        val TAG = "GoogleCloudVisionDetector"
    }

    private val messageSnackbarHelper = SnackbarHelper()

     val mColors = hashMapOf(
         "red" to Color.rgb(255, 0, 0),
         "orange" to Color.rgb(255, 104, 31),
         "yellow" to Color.rgb(255, 255, 0),
         "green" to Color.rgb(0, 255, 0),
         "blue" to Color.rgb(0, 0, 255),
         "purple" to Color.rgb(36, 10, 64),
         "black" to Color.rgb(0, 0, 0),
         "white" to Color.rgb(255, 255, 255),
         /*"pink" to Color.rgb(255, 192, 203),
         "gray" to Color.rgb(128, 128, 128),
         "cream" to Color.rgb(255, 253, 208)*/
     )

    val credentials = try {
        // Providing GCP credentials is not mandatory for this app, so the existence of R.raw.credentials
        // is not guaranteed. Instead, use getIdentifier to determine an optional resource.
        val res = activity.resources.getIdentifier("credentials", "raw", activity.packageName)
        if (res == 0) error("Missing GCP credentials in res/raw/credentials.json.")
        GoogleCredentials.fromStream(activity.resources.openRawResource(res))
    } catch (e: Exception) {
        Log.e(TAG, "Unable to create Google credentials from res/raw/credentials.json. Cloud ML will be disabled.", e)
        null
    }

     val settings = ImageAnnotatorSettings.newBuilder().setCredentialsProvider { credentials }.build()
     val vision = ImageAnnotatorClient.create(settings)

     override suspend fun analyze(image: Image, imageRotation: Int): List<DetectedObjectResult> {
         // Convert the image to a byte array.
         val convertYuv = convertYuv(image)
         // The model performs best on upright images, so rotate it.
         val rotatedImage = ImageUtils.rotateBitmap(convertYuv, imageRotation)

         // Perform request on Google Cloud Vision APIs.
         val request = createAnnotateImageRequest(rotatedImage.toByteArray())
         val response = vision.batchAnnotateImages(listOf(request))

         // Process result and map to DetectedObjectResult.
         val imagePropertiesResult = response.responsesList.first().imagePropertiesAnnotation
         val cropPropertiesResult = response.responsesList.first().cropHintsAnnotation

         //Log.d("COLORS 2", "(${s1}, ${r1}, ${g1}, ${b1}, ${p1})")

         //.d("COLORS", getMostCol(imagePropertiesResult.dominantColors.colorsList))
         return imagePropertiesResult.dominantColors.colorsList.map {
             val color = it.color
             val r = color.red.toFloat() / 255
             val g = color.green.toFloat() / 255
             val b = color.blue.toFloat() / 255
             val score = it.score
             val pixelFraction = it.pixelFraction
             val colors = Color.rgb(r,g,b)
             getMostCol(imagePropertiesResult.dominantColors.colorsList)
             DetectedObjectResult(score, "(${r}, ${g}, ${b}, ${pixelFraction})",Pair(0,0) )
         }


     }

     private fun getMostCol(ColorArray: List<ColorInfo>): String {
         var mostCon = 0.0f;
         var mostCounter = 0;
         for((index,col) in ColorArray.withIndex())
         {
             if(col.score > mostCon)
             {
                 mostCon = col.score
                 mostCounter = index
             }
             //Log.d("COLORS 2", getColorNameFromRgb(ColorArray.get(index).color.red,ColorArray.get(index).color.green,ColorArray.get(index).color.blue, ColorArray.get(index).score))
         }
         val r = ColorArray.get(mostCounter).color.red.toFloat() / 255
         val g = ColorArray.get(mostCounter).color.green.toFloat() / 255
         val b = ColorArray.get(mostCounter).color.blue.toFloat() / 255
         val colors = Color.rgb(r,g,b)
         UserContextInfo.setPrimaryColours(getBestMatchingColorName(colors)).toString()
         Log.d("USERCONTEXT","Furniture "+UserContextInfo.getPrimaryColours())
         return ColorArray.get(mostCounter).allFields.toString()

     }

     private fun getBestMatchingColorName(pixelColor: Int): String? {
        // largest difference is 255 for every colour component
         var currentDifference = 3 * 255
        // name of the best matching colour
         var closestColorName: String? = null
        // get int values for all three colour components of the pixel
         val pixelColorR = Color.red(pixelColor)
         val pixelColorG = Color.green(pixelColor)
         val pixelColorB = Color.blue(pixelColor)

         val colorNameIterator = mColors.keys.iterator()
        // continue iterating if the map contains a next colour and the difference is greater than zero.
        // a difference of zero means we've found an exact match, so there's no point in iterating further.
         while (colorNameIterator.hasNext() && currentDifference > 0) {
             // this colour's name
             val currentColorName = colorNameIterator.next()
             // this colour's int value
             val color = mColors[currentColorName]!!
             // get int values for all three colour components of this colour
             val colorR = Color.red(color)
             val colorG = Color.green(color)
             val colorB = Color.blue(color)
             // calculate sum of absolute differences that indicates how good this match is
             val difference = Math.abs(pixelColorR - colorR) + Math.abs(pixelColorG - colorG) + Math.abs(pixelColorB - colorB)
             // a smaller difference means a better match, so keep track of it
             if (currentDifference > difference) {
                 currentDifference = difference
                 closestColorName = currentColorName
             }
         }
         return closestColorName
     }

     private fun createAnnotateImageRequest(imageBytes: ByteArray): AnnotateImageRequest {
         // GCVImage is a typealias for com.google.cloud.vision's Image, needed to differentiate from android.media.Image
         val image = GCVImage.newBuilder().setContent(ByteString.copyFrom(imageBytes))
         val features = Feature.newBuilder().setType(Feature.Type.IMAGE_PROPERTIES)
         return AnnotateImageRequest.newBuilder()
             .setImage(image)
             .addFeatures(features)
             .build()
     }

    private fun showSnackbar(message: String): Unit =
        activity.view.snackbarHelper.showError(activity, message)
 }
