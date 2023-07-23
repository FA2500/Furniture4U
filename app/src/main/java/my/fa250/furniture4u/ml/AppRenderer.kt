/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package my.fa250.furniture4u.ml

import android.opengl.Matrix
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.ar.core.Anchor
import com.google.ar.core.Coordinates2d
import com.google.ar.core.Frame
import com.google.ar.core.TrackingState
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.NotYetAvailableException
import java.util.Collections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import my.fa250.furniture4u.ar.helper.DisplayRotationHelper
import my.fa250.furniture4u.ar.render.SampleRender
import my.fa250.furniture4u.ar.render.arcore.BackgroundRenderer
import my.fa250.furniture4u.ml.classification.DetectedObjectResult
import my.fa250.furniture4u.ml.classification.GoogleCloudVisionDetector
import my.fa250.furniture4u.ml.classification.GoogleCloudVisionImage
import my.fa250.furniture4u.ml.classification.MLKitObjectDetector
import my.fa250.furniture4u.ml.classification.ObjectDetector
import my.fa250.furniture4u.ml.render.LabelRender
import my.fa250.furniture4u.ml.render.PointCloudRender


/**
 * Renders the HelloAR application into using our example Renderer.
 */
class AppRenderer(val activity: ContextActivity) : DefaultLifecycleObserver, SampleRender.Renderer, CoroutineScope by MainScope() {
  companion object {
    val TAG = "ARRenderer"
  }

  lateinit var view: ContextActivityView

  val displayRotationHelper = DisplayRotationHelper(activity)
  lateinit var backgroundRenderer: BackgroundRenderer
  val pointCloudRender = PointCloudRender()
  val labelRenderer = LabelRender()

  val viewMatrix = FloatArray(16)
  val projectionMatrix = FloatArray(16)
  val viewProjectionMatrix = FloatArray(16)

  //val arLabeledAnchors = Collections.synchronizedList(mutableListOf<ARLabeledAnchor>())
  var scanButtonWasPressed = false

  val gcpAnalyzer = GoogleCloudVisionDetector(activity)
  val imgAnalyzer = GoogleCloudVisionImage(activity)

  var colAnalyzer: ObjectDetector = imgAnalyzer
  var objectAnalyzer: ObjectDetector = gcpAnalyzer

  override fun onResume(owner: LifecycleOwner) {
    displayRotationHelper.onResume()
  }

  override fun onPause(owner: LifecycleOwner) {
    displayRotationHelper.onPause()
  }

  fun bindView(view: ContextActivityView) {
    this.view = view

    view.scanButton.setOnClickListener {
      Log.w("THREAD","BUTTON ON THREAD")
      scanButtonWasPressed = true
      view.setScanningActive(true)
      hideSnackbar()
    }

    colAnalyzer = imgAnalyzer
    objectAnalyzer = gcpAnalyzer

    view.resetButton.setOnClickListener {
      //arLabeledAnchors.clear()
      view.closeRecommendation()
      view.resetButton.isEnabled = false
      hideSnackbar()
    }
  }

  override fun onSurfaceCreated(render: SampleRender) {
    backgroundRenderer = BackgroundRenderer(render).apply {
      setUseDepthVisualization(render, false)
    }
    pointCloudRender.onSurfaceCreated(render)
    labelRenderer.onSurfaceCreated(render)
  }

  override fun onSurfaceChanged(render: SampleRender?, width: Int, height: Int) {
    displayRotationHelper.onSurfaceChanged(width, height)
  }

  var objectResults: List<DetectedObjectResult>? = null
  var objectRes: List<DetectedObjectResult>? = null

  override fun onDrawFrame(render: SampleRender) {
    val session = activity.arCoreSessionHelper.sessionCache ?: return
    session.setCameraTextureNames(intArrayOf(backgroundRenderer.cameraColorTexture.textureId))

    // Notify ARCore session that the view size changed so that the perspective matrix and
    // the video background can be properly adjusted.
    displayRotationHelper.updateSessionIfNeeded(session)

    val frame = try {
      session.update()
    } catch (e: CameraNotAvailableException) {
      Log.e(TAG, "Camera not available during onDrawFrame", e)
      showSnackbar("Camera not available. Try restarting the app.")
      return
    }

    backgroundRenderer.updateDisplayGeometry(frame)
    backgroundRenderer.drawBackground(render)

    // Get camera and projection matrices.
    val camera = frame.camera
    camera.getViewMatrix(viewMatrix, 0)
    camera.getProjectionMatrix(projectionMatrix, 0, 0.01f, 100.0f)
    Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

    // Handle tracking failures.
    if (camera.trackingState != TrackingState.TRACKING) {
      return
    }

    // Draw point cloud.
    frame.acquirePointCloud().use { pointCloud ->
      pointCloudRender.drawPointCloud(render, pointCloud, viewProjectionMatrix)
    }

    // Frame.acquireCameraImage must be used on the GL thread.
    // Check if the button was pressed last frame to start processing the camera image.
    if (scanButtonWasPressed) {
      scanButtonWasPressed = false
      var cameraImage = frame.tryAcquireCameraImage()
      while (cameraImage?.width!! <= 0 && cameraImage?.height!! <= 0)
      {
        cameraImage = frame.tryAcquireCameraImage()
      }

      if (cameraImage != null ) {

        //MAIN
        /*launch(Dispatchers.IO) {
          val cameraId = session.cameraConfig.cameraId
          val imageRotation = displayRotationHelper.getCameraSensorToDisplayRotation(cameraId)
          try {
            objectResults = colAnalyzer.analyze(cameraImage, imageRotation)
            objectRes = objectAnalyzer.analyze(cameraImage, imageRotation)
          }
          catch (e: Exception)
          {
            Log.e("THREAD","NO RESULT",e)
            showSnackbar("Inaccurate Context Data.Please Rescan")
          }
          Log.w("THREAD","CLASSIFICATION END ON THREAD")

          /*val threads = Thread(Runnable {
            launch {
              Log.w("THREAD","CLASSIFICATION RUNNING ON THREAD")
              try {
                objectResults = colAnalyzer.analyze(cameraImage, imageRotation)
                objectRes = objectAnalyzer.analyze(cameraImage, imageRotation)
              }
              catch (e: Exception)
              {
                Log.e("COLOUR","NO RESULT",e)
                showSnackbar("Inaccurate Colour Data.Please Rescan")
              }

            }
          })
          threads.start()
          threads.join()*/

          cameraImage.close()
        }*/

        launch(Dispatchers.IO) {
          Log.w("THREAD", "COLOR CLASSIFICATION START ON THREAD")
          val cameraId = session.cameraConfig.cameraId
          val imageRotation = displayRotationHelper.getCameraSensorToDisplayRotation(cameraId)
          try {
            objectResults = colAnalyzer.analyze(cameraImage, imageRotation)
          } catch (e: Exception) {
            Log.e("THREAD", "Error in colAnalyzer", e)
            showSnackbar("Inaccurate Context Data. Please Rescan")
          }
          Log.w("THREAD", "COLOR CLASSIFICATION END ON THREAD")
          cameraImage.close()
        }

        launch(Dispatchers.IO) {
          Log.w("THREAD", "OBJECT CLASSIFICATION START ON THREAD")
          val cameraId = session.cameraConfig.cameraId
          val imageRotation = displayRotationHelper.getCameraSensorToDisplayRotation(cameraId)
          try {
            objectRes = objectAnalyzer.analyze(cameraImage, imageRotation)
          } catch (e: Exception) {
            Log.e("THREAD", "Error in objectAnalyzer", e)
            showSnackbar("Inaccurate Context Data. Please Rescan")
          }
          Log.w("THREAD", "OBJECT CLASSIFICATION END ON THREAD")
          cameraImage.close()
        }
      }
    }

    /** If results were completed this frame, create [Anchor]s from model results. */
    val objects = objectResults
    val objectsobj = objectRes

    /*if (objects != null) {
      objectResults = null
      Log.i(TAG, "$colAnalyzer got objects: $objects")
      val anchors = objects.mapNotNull { obj ->
        val (atX, atY) = obj.centerCoordinate
        val anchor = createAnchor(atX.toFloat(), atY.toFloat(), frame) ?: return@mapNotNull null
        Log.i(TAG, "Created anchor ${anchor.pose} from hit test")
        ARLabeledAnchor(anchor, obj.label)
      }
      //arLabeledAnchors.addAll(anchors)
      view.post {
        //view.resetButton.isEnabled = arLabeledAnchors.isNotEmpty()
        view.setScanningActive(false)
        when {
          objects.isEmpty() ->
            //showSnackbar("Color model returned no results.")
            Log.w("COLOUR","NO RESULT")
        }
      }

    }
    if (objectsobj != null) {
      objectRes = null
      val anchors = objectsobj.mapNotNull { obj ->
        val (atX, atY) = obj.centerCoordinate
        val anchor = createAnchor(atX.toFloat(), atY.toFloat(), frame) ?: return@mapNotNull null
        Log.i(TAG, "Created anchor ${anchor.pose} from hit test")
        ARLabeledAnchor(anchor, obj.label)
      }
      //arLabeledAnchors.addAll(anchors)
      view.post {
        //view.resetButton.isEnabled = arLabeledAnchors.isNotEmpty()
        view.setScanningActive(false)
        when {
          objectsobj.isEmpty() ->
            Log.w("OBJECT","NO RESULT")

        }
        view.getRecommendation()
      }
    }*/

    //test
    if (objects != null && objectsobj != null) {
      objectResults = null
      objectRes = null
      view.post {
        //view.resetButton.isEnabled = arLabeledAnchors.isNotEmpty()
        view.setScanningActive(false)
        when {
          objects.isEmpty() ->
            //showSnackbar("Color model returned no results.")
            Log.w("COLOUR","NO RESULT")
        }
        when {
          objectsobj.isEmpty() ->
            Log.w("OBJECT","NO RESULT")
        }
        view.getRecommendation()
      }
    }
    //test

    // Draw labels at their anchor position.
    /*for (arDetectedObject in arLabeledAnchors) {
      val anchor = arDetectedObject.anchor
      if (anchor.trackingState != TrackingState.TRACKING) continue
      labelRenderer.draw(
        render,
        viewProjectionMatrix,
        anchor.pose,
        camera.pose,
        arDetectedObject.label
      )
    }*/
  }

  /**
   * Utility method for [Frame.acquireCameraImage] that maps [NotYetAvailableException] to `null`.
   */
  fun Frame.tryAcquireCameraImage() = try {
    acquireCameraImage()
  } catch (e: NotYetAvailableException) {
    null
  } catch (e: Throwable) {
    throw e
  }

  private fun showSnackbar(message: String): Unit =
    activity.view.snackbarHelper.showError(activity, message)

  private fun hideSnackbar() = activity.view.snackbarHelper.hide(activity)

  /**
   * Temporary arrays to prevent allocations in [createAnchor].
   */
  private val convertFloats = FloatArray(4)
  private val convertFloatsOut = FloatArray(4)

  /** Create an anchor using (x, y) coordinates in the [Coordinates2d.IMAGE_PIXELS] coordinate space. */
  fun createAnchor(xImage: Float, yImage: Float, frame: Frame): Anchor? {
    // IMAGE_PIXELS -> VIEW
    convertFloats[0] = xImage
    convertFloats[1] = yImage
    frame.transformCoordinates2d(
      Coordinates2d.IMAGE_PIXELS,
      convertFloats,
      Coordinates2d.VIEW,
      convertFloatsOut
    )

    // Conduct a hit test using the VIEW coordinates
    val hits = frame.hitTest(convertFloatsOut[0], convertFloatsOut[1])
    val result = hits.getOrNull(0) ?: return null
    return result.trackable.createAnchor(result.hitPose)
  }
}

data class ARLabeledAnchor(val anchor: Anchor, val label: String)