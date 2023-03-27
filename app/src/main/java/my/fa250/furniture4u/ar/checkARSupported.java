package my.fa250.furniture4u.ar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;

import my.fa250.furniture4u.ar.helper.CameraPermissionHelper;
import my.fa250.furniture4u.test;

public class checkARSupported extends AppCompatActivity {

    private boolean mUserRequestedInstall = true;
    private Session mSession;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        maybeEnableArButton();
        isARCoreSupportedAndUpToDate();
    }

    public void maybeEnableArButton() {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        if (availability.isTransient()) {
            // Continue to query availability at 5Hz while compatibility is checked in the background.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    maybeEnableArButton();
                }
            }, 200);
        }
        if (availability.isSupported()) {
            Toast.makeText(this, "AR IS SUPPORTED", Toast.LENGTH_SHORT).show();
        } else { // The device is unsupported or unknown.
            Toast.makeText(this, "AR IS NOT SUPPORTED", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isARCoreSupportedAndUpToDate() {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        switch (availability) {
            case SUPPORTED_INSTALLED:
                Toast.makeText(this, "ARCORE INSTALLED AND SUPPORTED", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(checkARSupported.this , testAR.class);
                startActivity(intent);
                return true;

            case SUPPORTED_APK_TOO_OLD:
                Toast.makeText(this, "ARCORE TOO OLD", Toast.LENGTH_SHORT).show();
            case SUPPORTED_NOT_INSTALLED:
                try {
                    Toast.makeText(this, "ARCORE NOT INSTALL", Toast.LENGTH_SHORT).show();
                    // Request ARCore installation or update if needed.
                    ArCoreApk.InstallStatus installStatus = ArCoreApk.getInstance().requestInstall(this, true);
                    switch (installStatus) {
                        case INSTALL_REQUESTED:
                            Toast.makeText(this, "REQUESTING ARCORE INSTALLATION", Toast.LENGTH_SHORT).show();
                            return false;
                        case INSTALLED:
                            return true;
                    }
                } catch (UnavailableException e) {
                    Toast.makeText(this, "ARCORE UNABLE TO BE INSTALL", Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "ARCore not installed", e);
                }
                return false;

            case UNSUPPORTED_DEVICE_NOT_CAPABLE:
                Toast.makeText(this, "ARCORE NOT SUPPORTED", Toast.LENGTH_SHORT).show();
                // This device is not supported for AR.
                return false;

            case UNKNOWN_CHECKING:
                Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                // ARCore is checking the availability with a remote query.
                // This function should be called again after waiting 200 ms to determine the query result.
            case UNKNOWN_ERROR:
                Toast.makeText(this, "ARCORE UNKNOWN ERROR", Toast.LENGTH_SHORT).show();
            case UNKNOWN_TIMED_OUT:
                Toast.makeText(this, "ARCORE TIMED OUT", Toast.LENGTH_SHORT).show();
                // There was an error checking for AR availability. This may be due to the device being offline.
                // Handle the error appropriately.
        }
        return false;
    }

    public void createSession() {
        // Create a new ARCore session.
        try {
            mSession = new Session(this);
        } catch (UnavailableArcoreNotInstalledException e) {
            e.printStackTrace();
        } catch (UnavailableApkTooOldException e) {
            e.printStackTrace();
        } catch (UnavailableSdkTooOldException e) {
            e.printStackTrace();
        } catch (UnavailableDeviceNotCompatibleException e) {
            e.printStackTrace();
        }

        // Create a session config.
        Config config = new Config(mSession);

        // Do feature-specific operations here, such as enabling depth or turning on
        // support for Augmented Faces.

        // Configure the session.
        mSession.configure(config);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check camera permission.
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this, "Requesting Camera Permission", Toast.LENGTH_SHORT).show();
            CameraPermissionHelper.requestCameraPermission(this);
            return;
        }
        else
        {
            Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
        }

        // Ensure that Google Play Services for AR and ARCore device profile data are
        // installed and up to date.
        try {

            if (mSession == null) {
                switch (ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)) {
                    case INSTALLED:
                        // Success: Safe to create the AR session.
                        Toast.makeText(this, "Creating AR Session", Toast.LENGTH_SHORT).show();
                        mSession = new Session(this);
                        break;
                    case INSTALL_REQUESTED:
                        // When this method returns `INSTALL_REQUESTED`:
                        // 1. ARCore pauses this activity.
                        // 2. ARCore prompts the user to install or update Google Play
                        //    Services for AR (market://details?id=com.google.ar.core).
                        // 3. ARCore downloads the latest device profile data.
                        // 4. ARCore resumes this activity. The next invocation of
                        //    requestInstall() will either return `INSTALLED` or throw an
                        //    exception if the installation or update did not succeed.
                        mUserRequestedInstall = false;
                        Toast.makeText(this, "INSTALLING ARCORE", Toast.LENGTH_SHORT).show();
                        return;
                }
            }
            else
            {
                Toast.makeText(this, "AR Session Ongoing", Toast.LENGTH_SHORT).show();
            }
        } catch (UnavailableUserDeclinedInstallationException e) {
            // Display an appropriate message to the user and return gracefully.
            Toast.makeText(this, "TODO: handle exception " + e, Toast.LENGTH_LONG)
                    .show();
            return;
        } catch (Exception e) {
            return;  // mSession remains null, since session creation has failed.
        }
    }

    @Override
    protected void onDestroy()
    {
        mSession.close();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                    .show();
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this);
            }
            finish();
        }
    }
}