package com.project.oic_android.ui.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.project.oic_android.databinding.FragmentSearchBinding
import org.opencv.android.Utils
import org.opencv.core.Mat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.project.oic_android.R

typealias CornersListener = () -> Unit

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var safeContext: Context
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private lateinit var outputDirectory: File


    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 위험권한(Camera) 권한 승인상태 가져오기
        if (allPermissionsGranted()) { startCamera() }
        // 권한 요청
        else { requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS) }

        // 촬영 버튼 리스너
        binding.captureButton.setOnClickListener { takePhoto() }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)

        cameraProviderFuture.addListener({
            // 카메라 생명주기 오너로 지정
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // 미리보기
            preview = Preview.Builder()
                .build()
                //.also { it.setSurfaceProvider(binding.viewFinder.surfaceProvider) }

            imageCapture = ImageCapture.Builder().build()

            imageAnalyzer = ImageAnalysis.Builder().build().apply {
                setAnalyzer(Executors.newSingleThreadExecutor(), CornerAnalyzer {
                    val bitmap = binding.viewFinder.bitmap
                    val img = Mat()
                    Utils.bitmapToMat(bitmap, img)
                    bitmap?.recycle()
                    // Do image analysis here if you need bitmap
                })
            }
            // 후면 카메라를 기본 카메라로 설정
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalyzer, preview, imageCapture)
                preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(safeContext))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(outputDirectory, SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        // 촬영
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(safeContext), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults){
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(safeContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            })
    }

    override fun onPause() {
        super.onPause()
        isOffline = true
    }

    override fun onResume() {
        super.onResume()
        isOffline = false
    }

    // 권한 처리
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(safeContext, it) == PackageManager.PERMISSION_GRANTED
    }
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) { startCamera() }
            else { Toast.makeText(safeContext, "카메라 권한이 허용되지 않음", Toast.LENGTH_SHORT).show() }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else activity?.filesDir!!
    }

    companion object {
        private const val TAG = "CameraX"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        var isOffline = false // prevent app crash when goes offline
    }

    private class CornerAnalyzer(private val listener: CornersListener) : ImageAnalysis.Analyzer {
        /*
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }
        */
        @SuppressLint("UnsafeExperimentalUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            if (!isOffline) { listener() }
            imageProxy.close()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}