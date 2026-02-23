package com.bxr.trainingapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Camera
import androidx.camera.core.AspectRatio
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.bxr.trainingapp.OverlayView
import com.bxr.trainingapp.PoseLandmarkerHelper
import com.bxr.trainingapp.R
import com.bxr.trainingapp.data.AngleType
import com.bxr.trainingapp.data.Angles
import com.bxr.trainingapp.forms.trackJab
import com.bxr.trainingapp.sessions.FormTracker
import com.bxr.trainingapp.sessions.Handedness
import com.bxr.trainingapp.sessions.SessionTracker
import com.google.mediapipe.tasks.vision.core.RunningMode
import java.time.Instant
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class CameraActivity : AppCompatActivity(), PoseLandmarkerHelper.LandmarkerListener {
    private lateinit var poseLandmarkerHelper: PoseLandmarkerHelper

    private var angles: AngleType? = null
    private var moveName: String? = null

    private var model = PoseLandmarkerHelper.Companion.MODEL_POSE_LANDMARKER_FULL
    private var delegate: Int = PoseLandmarkerHelper.Companion.DELEGATE_GPU
    private var minPoseDetectionConfidence: Float =
        PoseLandmarkerHelper.Companion.DEFAULT_POSE_DETECTION_CONFIDENCE
    private var minPoseTrackingConfidence: Float =
        PoseLandmarkerHelper.Companion.DEFAULT_POSE_TRACKING_CONFIDENCE
    private var minPosePresenceConfidence: Float =
        PoseLandmarkerHelper.Companion.DEFAULT_POSE_PRESENCE_CONFIDENCE

    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraFacing = CameraSelector.LENS_FACING_FRONT

    private lateinit var backgroundExecutor: ExecutorService

    private lateinit var viewFinder: PreviewView
    private lateinit var overlay: OverlayView
    private lateinit var tvRepNumber: TextView
    private lateinit var tvErrorMessage: TextView

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(
                    baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                setUpCamera()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // to do other moobs
        moveName = intent.getStringExtra("MOVE_NAME")

        viewFinder = findViewById(R.id.view_finder)
        overlay = findViewById(R.id.overlay)
        tvRepNumber = findViewById(R.id.repCounter)
        tvErrorMessage = findViewById(R.id.errorText)


        viewFinder.scaleType = PreviewView.ScaleType.FILL_CENTER

        backgroundExecutor = Executors.newSingleThreadExecutor()

        backgroundExecutor.execute {
            poseLandmarkerHelper = PoseLandmarkerHelper(
                context = this,
                runningMode = RunningMode.LIVE_STREAM,
                minPoseDetectionConfidence = minPoseDetectionConfidence,
                minPoseTrackingConfidence = minPoseTrackingConfidence,
                minPosePresenceConfidence = minPosePresenceConfidence,
                currentDelegate = delegate,
                poseLandmarkerHelperListener = this,
                currentModel = model
            )
        }

        if (allPermissionsGranted()){
           setUpCamera()
        } else {
            requestPermissions()
        }
    }

    private var currentSession = SessionTracker(
        startTime = Instant.now(),
        endTime = Instant.now(),
        formState = FormTracker(),
        handedness = Handedness.right
    )

    private fun updateState(newAngles: AngleType?){
        if (newAngles == null) return
        angles = newAngles
        currentSession.formState = trackJab(angles!!, currentSession.formState)
        runOnUiThread {
            tvRepNumber.text = (currentSession.formState.reps.first).toString()
            tvErrorMessage.text = (currentSession.formState.errors.last()).toString()
        }
        Log.d("JABSTATE", currentSession.formState.state.toString())
        Log.d("ANGLES", angles.toString())
    }

    private fun requestPermissions(){
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    override fun onResume() {
        super.onResume()
        backgroundExecutor.execute {
            if(this::poseLandmarkerHelper.isInitialized){
                if(poseLandmarkerHelper.isClose()){
                    poseLandmarkerHelper.setupPoseLandmarker()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        backgroundExecutor.execute { poseLandmarkerHelper.clearPoseLandmarker() }
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundExecutor.shutdown()
        backgroundExecutor.awaitTermination(
            Long.MAX_VALUE, TimeUnit.NANOSECONDS
        )
    }

    override fun onError(error: String, errorCode: Int) {
        Log.i("Erroris", error.toString())
    }

    override fun onResults(resultBundle: PoseLandmarkerHelper.ResultBundle) {
        val resultList = resultBundle.results.first()
        val computedAngles = Angles().getAngles(resultList)
        updateState(computedAngles)
        runOnUiThread {
            overlay.setResults(
                resultList,
                resultBundle.inputImageHeight,
                resultBundle.inputImageWidth,
                RunningMode.LIVE_STREAM,
            )
            overlay.invalidate()
        }
    }


    private fun setUpCamera() {
        val cameraProvideFuture =
            ProcessCameraProvider.getInstance(this)
        cameraProvideFuture.addListener(
            {
                cameraProvider = cameraProvideFuture.get()

                bindCameraUseCases()
            }, ContextCompat.getMainExecutor(this)
        )
    }

    private fun bindCameraUseCases() {
        val aspectRatioStrategy = AspectRatioStrategy(
            AspectRatio.RATIO_16_9, AspectRatioStrategy.FALLBACK_RULE_NONE
        )
        val resolutionSelector = ResolutionSelector.Builder()
            .setAspectRatioStrategy(aspectRatioStrategy)
            .build()

        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(cameraFacing).build()

        preview = Preview.Builder().setResolutionSelector(resolutionSelector)
            .setTargetRotation(viewFinder.display.rotation)
            .build()

        imageAnalyzer =
            ImageAnalysis.Builder().setResolutionSelector(resolutionSelector)
                .setTargetRotation(viewFinder.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                .also {
                    it.setAnalyzer(backgroundExecutor) { image ->
                        detectPose(image)
                    }
                }

        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageAnalyzer
            )

            preview?.surfaceProvider = viewFinder.surfaceProvider
        } catch (exc: Exception) {
            Log.e("TAG", "Use case binding failed", exc)
        }
    }

    private fun detectPose(imageProxy: ImageProxy) {
        if(this::poseLandmarkerHelper.isInitialized) {
            val rotation = imageProxy.imageInfo.rotationDegrees
            // overlay.rotationDegrees = imageProxy.imageInfo.rotationDegrees
            overlay.isFrontCamera =
                cameraFacing == CameraSelector.LENS_FACING_FRONT
            poseLandmarkerHelper.detectLiveStream(
                imageProxy = imageProxy,
                isFrontCamera = cameraFacing == CameraSelector.LENS_FACING_FRONT
            )
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageAnalyzer?.targetRotation =
            viewFinder.display.rotation
    }
}