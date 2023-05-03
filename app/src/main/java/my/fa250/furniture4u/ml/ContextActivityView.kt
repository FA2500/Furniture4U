package my.fa250.furniture4u.ml

import android.opengl.GLSurfaceView
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import my.fa250.furniture4u.R
import my.fa250.furniture4u.ar.helper.SnackbarHelper
import my.fa250.furniture4u.ar.render.SampleRender

class ContextActivityView(val activity: ContextActivity, renderer: AppRenderer) :
    DefaultLifecycleObserver {
    val root = View.inflate(activity, R.layout.activity_context, null)
    val surfaceView = root.findViewById<GLSurfaceView>(R.id.surfaceview).apply {
        SampleRender(this, renderer, activity.assets)
    }
    val useCloudMlSwitch = root.findViewById<SwitchCompat>(R.id.useCloudMlSwitch)
    val scanButton = root.findViewById<AppCompatButton>(R.id.scanButton)
    val resetButton = root.findViewById<AppCompatButton>(R.id.clearButton)
    val snackbarHelper = SnackbarHelper().apply {
        setParentView(root.findViewById(R.id.coordinatorLayout))
        setMaxLines(6)
    }

    override fun onResume(owner: LifecycleOwner) {
        surfaceView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        surfaceView.onPause()
    }

    fun post(action: Runnable) = root.post(action)

    /**
     * Toggles the scan button depending on if scanning is in progress.
     */
    fun setScanningActive(active: Boolean) = when(active) {
        true -> {
            scanButton.isEnabled = false
            scanButton.setText(activity.getString(R.string.scan_busy))
        }
        false -> {
            scanButton.isEnabled = true
            scanButton.setText(activity.getString(R.string.scan_available))
        }
    }
}