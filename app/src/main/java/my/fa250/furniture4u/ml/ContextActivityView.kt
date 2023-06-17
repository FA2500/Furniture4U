package my.fa250.furniture4u.ml

import android.content.Intent
import android.net.Uri
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.fa250.furniture4u.R
import my.fa250.furniture4u.UserContextInfo
import my.fa250.furniture4u.ar.helper.SnackbarHelper
import my.fa250.furniture4u.ar.render.SampleRender

class ContextActivityView(val activity: ContextActivity, renderer: AppRenderer) :
    DefaultLifecycleObserver {
    val root = View.inflate(activity, R.layout.activity_context, null)
    val surfaceView = root.findViewById<GLSurfaceView>(R.id.surfaceview).apply {
        SampleRender(this, renderer, activity.assets)
    }
    val ll = root.findViewById<LinearLayout>(R.id.llcontext)
    val typeBtn = root.findViewById<AppCompatButton>(R.id.ml_room_type_btn)
    val colBtn = root.findViewById<AppCompatButton>(R.id.ml_room_col_btn)
    val rcmBtn = root.findViewById<LinearLayout>(R.id.llcontext2)

    val scanButton = root.findViewById<AppCompatButton>(R.id.scanButton)
    val resetButton = root.findViewById<AppCompatButton>(R.id.clearButton)
    val getrcm = root.findViewById<AppCompatButton>(R.id.ml_recommendation_btn)
    val snackbarHelper = SnackbarHelper().apply {
        setParentView(root.findViewById(R.id.coordinatorLayout))
        setMaxLines(6)
    }

    var roomConType = ""

    override fun onResume(owner: LifecycleOwner) {
        surfaceView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        surfaceView.onPause()
    }

    fun getRecommendation(){
        ll.visibility = View.VISIBLE
        rcmBtn.visibility = View.VISIBLE
        for(k in UserContextInfo.getObjects())
        {
            Log.d("Furniture",k)
            if(k.equals("Bed") || k.equals("Laptop") || k.equals("Computer") || k.equals("monitor") || k.equals("keyboard"))
            {
                roomConType = "Bedroom"
            }
            else if(k.equals("Sofa") || k.equals("Couch"))
            {
                roomConType = "Living Room"
            }
            else if(k.equals("Cabinet"))
            {
                roomConType = "Kitchen"
            }
            else
            {
                roomConType = "Bedroom"
            }
        }
        typeBtn.text = roomConType
        colBtn.text = UserContextInfo.getPrimaryColours()
    }

    fun closeRecommendation()
    {
        ll.visibility = View.INVISIBLE
        rcmBtn.visibility = View.INVISIBLE
        typeBtn.text = ""
        colBtn.text = ""
    }

    fun post(action: Runnable) = root.post(action)

    /**
     * Toggles the scan button depending on if scanning is in progress.
     */
    fun setScanningActive(active: Boolean) = when(active) {
        true -> {
            surfaceView.onResume()
            surfaceView.onPause()
            surfaceView.onResume()

            scanButton.isEnabled = false
            scanButton.setText(activity.getString(R.string.scan_busy))
        }
        false -> {
            surfaceView.onResume()
            surfaceView.onPause()
            surfaceView.onResume()

            scanButton.isEnabled = true
            scanButton.setText(activity.getString(R.string.scan_available))
        }
    }
}