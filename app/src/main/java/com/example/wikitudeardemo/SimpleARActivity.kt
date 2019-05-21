package com.example.wikitudeardemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.wikitude.architect.ArchitectStartupConfiguration
import com.wikitude.architect.ArchitectView
import com.wikitude.common.camera.CameraSettings
import java.io.IOException

//     This Activity is required to use the basic functionality for Object Tracking.
//     It is responsible for loading the AR Experience

class SimpleArActivity : AppCompatActivity() {

    //     The ArchitectureView is the core of the ARFunctionality. It is the main
    //      interface to the Wikitude SDK. The ArchitectView has its own lifecycle which
    //       is very similar to the Activity lifecycle.
    //          To ensure that the ArchitectView is functioning properly the following methods have to be called:
    //           - onCreate(ArchitectStartupConfiguration)
    //           - onPostCreate()
    //           - onResume()
    //           - onPause()
    //           - onDestroy()
    //        Those methods are preferably called in the corresponding Activity lifecycle callbacks.

    var architectView: ArchitectView? = null

    //    The path to the AR-Experience. This variable will be initialized with the path of index.html
    private var arExperience = "Interactivity_Model/index.html"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //        The ArchitectStartupConfiguration is required to call architectView.onCreate().
        //        It controls the startup of the ArchitectView which includes camera settings,
        //        the required device features to run the ArchitectView and the LicenseKey which
        //        has to be set to enable an AR-Experience.

        // Creates a config with its default values.
        val architectureConfiguration = ArchitectStartupConfiguration()

        // Set the wihitude licence key. To get a trial license key visit http://www.wikitude.com/developer/licenses.
        architectureConfiguration.licenseKey = getString(R.string.wikitude_license_key)

        // Set camera position. The default camera is the first camera available for the system.
        architectureConfiguration.cameraPosition = CameraSettings.CameraPosition.BACK

        // Set camera resolution. The default resolution is 640x480.
        architectureConfiguration.cameraResolution = CameraSettings.CameraResolution.HD_1280x720

        // Set camera focus mode. The default focus mode is continuous focusing.
        architectureConfiguration.cameraFocusMode = CameraSettings.CameraFocusMode.CONTINUOUS

        // Enable/Disable camera 2. The camera2 api is disabled by default (old camera api is used).
        architectureConfiguration.isCamera2Enabled = false

        //Instantiate the ArchitectView object with above configurations
        architectView = ArchitectView(this)
        architectView?.onCreate(architectureConfiguration)

        //Set the architectView as content to SimpeArActivity
        setContentView(architectView)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        architectView?.onPostCreate()
        try {
            //    Loads the AR-Experience, it may be a relative path from assets,
            //    an absolute path (file://) or a server url.
            //
            //    To get notified once the AR-Experience is fully loaded,
            //    an ArchitectWorldLoadedListener can be registered.

            architectView?.load(SAMPLES_ROOT + arExperience)
        } catch (e: IOException) {
            Toast.makeText(this, getString(R.string.error_loading_ar_experience), Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Exception while loading arExperience $arExperience.", e)
        }

    }

    override fun onResume() {
        super.onResume()

        // Mandatory ArchitectView lifecycle call
        architectView?.onResume()
    }

    override fun onPause() {
        super.onPause()

        // Mandatory ArchitectView lifecycle call
        architectView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        //      Deletes all cached files of this instance of the ArchitectView.
        //      This guarantees that internal storage for this instance of the ArchitectView
        //      is cleaned and app-memory does not grow each session.
        //
        //      This should be called before architectView.onDestroy
        architectView?.clearCache()

        // Mandatory ArchitectView lifecycle call
        architectView?.onDestroy()
    }

    companion object {

        private val TAG = SimpleArActivity::class.java.simpleName

        //          Root directory of the sample AR-Experiences in the assets dir.
        private const val SAMPLES_ROOT = "samples/"
    }
}
