package my.fa250.furniture4u.ml

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.ar.core.CameraConfig
import com.google.ar.core.CameraConfigFilter
import com.google.ar.core.Config
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.UnavailableApkTooOldException
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException
import com.google.ar.core.exceptions.UnavailableSdkTooOldException
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import my.fa250.furniture4u.R
import my.fa250.furniture4u.ar.helper.FullScreenHelper
import my.fa250.furniture4u.arsv.ARActivity2
import my.fa250.furniture4u.com.HomePageActivity
import my.fa250.furniture4u.com.NotificationActivity
import my.fa250.furniture4u.com.ProfileActivity

class ContextActivity: AppCompatActivity() {
    val TAG = "ContextActivity"
    lateinit var arCoreSessionHelper: ARCoreSessionLifecycleHelper

    lateinit var renderer: AppRenderer
    lateinit var view: ContextActivityView
    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arCoreSessionHelper = ARCoreSessionLifecycleHelper(this)
        // When session creation or session.resume fails, we display a message and log detailed information.
        arCoreSessionHelper.exceptionCallback = { exception ->
            val message = when (exception) {
                is UnavailableArcoreNotInstalledException,
                is UnavailableUserDeclinedInstallationException -> "Please install ARCore"
                is UnavailableApkTooOldException -> "Please update ARCore"
                is UnavailableSdkTooOldException -> "Please update this app"
                is UnavailableDeviceNotCompatibleException -> "This device does not support AR"
                is CameraNotAvailableException -> "Camera not available. Try restarting the app."
                else -> "Failed to create AR session: $exception"
            }
            Log.e(TAG, message, exception)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        arCoreSessionHelper.beforeSessionResume = { session ->
            session.configure(
                session.config.apply {
                    // To get the best image of the object in question, enable autofocus.
                    focusMode = Config.FocusMode.AUTO
                    if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                        depthMode = Config.DepthMode.AUTOMATIC
                    }
                }
            )

            val filter = CameraConfigFilter(session)
                .setFacingDirection(CameraConfig.FacingDirection.BACK)
            val configs = session.getSupportedCameraConfigs(filter)
            val sort = compareByDescending<CameraConfig> { it.imageSize.width }
                .thenByDescending { it.imageSize.height }
            session.cameraConfig = configs.sortedWith(sort)[0]
        }
        lifecycle.addObserver(arCoreSessionHelper)

        renderer = AppRenderer(this)
        lifecycle.addObserver(renderer)
        view = ContextActivityView(this, renderer)
        setContentView(view.root)
        renderer.bindView(view)
        lifecycle.addObserver(view)

        bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.action_search
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {          // Handle home item click
                    val intent3 = Intent(this@ContextActivity, HomePageActivity::class.java)
                    startActivity(intent3)
                    finish()
                    true
                }
                R.id.action_search ->                     // Handle search item click
                    true

                R.id.action_notifications -> {
                    // Handle notifications item click
                    val intent1 = Intent(this@ContextActivity, NotificationActivity::class.java)
                    startActivity(intent1)
                    finish()
                    true
                }

                R.id.action_profile -> {
                    // Handle profile item click
                    val intent2 = Intent(this@ContextActivity, ProfileActivity::class.java)
                    startActivity(intent2)
                    finish()
                    true
                }

                else -> {
                    false
                }
            }
        }


        view.getrcm.setOnClickListener{
            /*val sceneViewerIntent = Intent(Intent.ACTION_VIEW);
            sceneViewerIntent.setData(Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=https://furniture4u.s3.ap-southeast-1.amazonaws.com/couch/m2/red/couch_m2_red.gltf"));
            sceneViewerIntent.setData(Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Avocado/glTF/Avocado.gltf"));
            val intentUri = Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
                .appendQueryParameter("file", "https://furniture4u.s3.ap-southeast-1.amazonaws.com/couch/m2/red/couch_m2_red.gltf")
                .appendQueryParameter("mode", "ar_preferred")
                .appendQueryParameter("title", "Untitled")
                .build()
            sceneViewerIntent.data = intentUri
            sceneViewerIntent.setPackage("com.google.ar.core");
            startActivity(sceneViewerIntent);*/
            val intent = Intent(this, ARActivity2::class.java)
            intent.putExtra("type",view.getRoomType())
            intent.putExtra("colour",view.getColour())
            intent.putExtra("furn", view.getFurnType())
            startActivity(intent)
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        arCoreSessionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus)
    }
}