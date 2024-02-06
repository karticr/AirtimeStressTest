package com.example.airtimestresstest

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Camera
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.nfc.Tag
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.airtimestresstest.ui.theme.AirtimeStressTestTheme

class MainActivity : ComponentActivity() {
    lateinit var capReq: CaptureRequest.Builder
    lateinit var handler: Handler
    lateinit var handlerThread: HandlerThread
    lateinit var  cameraManager: CameraManager
    lateinit var textureView: TextureView
    lateinit var cameraCaptureSession: CameraCaptureSession
    lateinit var cameraDevice: CameraDevice
    lateinit var captureRequest: CaptureRequest


    lateinit var capReq1: CaptureRequest.Builder
    lateinit var handler1: Handler
    lateinit var handlerThread1: HandlerThread
    lateinit var  cameraManager1: CameraManager

    lateinit var textureView1: TextureView


    lateinit var textureView2: TextureView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)

//        windowInsetsController.systemBarsBehavior =
//            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
//            if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
//                || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())) {
//                    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
//            }
//            view.onApplyWindowInsets(windowInsets)
//        }
        get_permissions()
//        open_camera()


        textureView = findViewById(R.id.textureView)
        textureView1 = findViewById(R.id.textureView1)
        textureView2 = findViewById(R.id.textureView3)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)


        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                open_camera(cameraManager.cameraIdList[0], textureView)
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

            }

        }

        textureView1 = findViewById(R.id.textureView1)
        cameraManager1 = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread1 = HandlerThread("videoThread")
        handlerThread1.start()
        handler1 = Handler(handlerThread1.looper)

        textureView1.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                open_camera(cameraManager.cameraIdList[1], textureView1)
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

            }

        }

        textureView2.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                open_camera(cameraManager.cameraIdList[2], textureView2)
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

            }

        }




    }


    @SuppressLint("MissingPermission")
    fun open_camera(cameraId: String, textureView: TextureView) {
        cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                val captureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                val surface = Surface(textureView.surfaceTexture)
                captureRequestBuilder.addTarget(surface)

                camera.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        session.setRepeatingRequest(captureRequestBuilder.build(), null, handler)
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        Log.e("Camera", "Configuration failed for camera $cameraId")
                    }
                }, handler)
            }

            override fun onDisconnected(camera: CameraDevice) {
                // Handle camera disconnection
            }

            override fun onError(camera: CameraDevice, error: Int) {
                // Handle camera error
            }
        }, handler)
    }


//    @SuppressLint("MissingPermission")
//    fun open_camera(){
//        cameraManager.openCamera(cameraManager.cameraIdList[0], object :CameraDevice.StateCallback(){
//            override fun onOpened(p0: CameraDevice) {
//                cameraDevice = p0
//                capReq       = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
//                var surfacee  = Surface(textureView.surfaceTexture)
////                val surface2 = Surface(textureView1.surfaceTexture)
//                capReq.addTarget(surfacee)
//
//                cameraDevice.createCaptureSession(listOf(surfacee), object :CameraCaptureSession.StateCallback(){
//                    override fun onConfigured(p0: CameraCaptureSession) {
//                        cameraCaptureSession = p0
//                        cameraCaptureSession.setRepeatingRequest(capReq.build(), null, null)
//
//                    }
//
//                    override fun onConfigureFailed(p0: CameraCaptureSession) {
//
//                    }
//                }, handler)
//
//            }
//
//            override fun onDisconnected(camera: CameraDevice) {
//
//            }
//
//            override fun onError(camera: CameraDevice, error: Int) {
//
//            }
//
//        }, handler)
//    }
//    @SuppressLint("MissingPermission")
//    fun open_camera1(){
//        cameraManager1.openCamera(cameraManager1.cameraIdList[1], object :CameraDevice.StateCallback(){
//            override fun onOpened(p0: CameraDevice) {
//                cameraDevice1 = p0
//                capReq1       = cameraDevice1.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
//                var surfaceee  = Surface(textureView.surfaceTexture)
////                val surface2 = Surface(textureView1.surfaceTexture)
//                capReq1.addTarget(surfaceee)
//
//                cameraDevice1.createCaptureSession(listOf(surfaceee), object :CameraCaptureSession.StateCallback(){
//                    override fun onConfigured(p0: CameraCaptureSession) {
//                        cameraCaptureSession1 = p0
//                        cameraCaptureSession1.setRepeatingRequest(capReq1.build(), null, null)
//
//                    }
//
//                    override fun onConfigureFailed(p0: CameraCaptureSession) {
//
//                    }
//                }, handler1)
//
//            }
//
//            override fun onDisconnected(camera: CameraDevice) {
//
//            }
//
//            override fun onError(camera: CameraDevice, error: Int) {
//
//            }
//
//        }, handler1)
//    }

    fun get_permissions(){
        var permissionLst = mutableListOf<String>()
        if(checkSelfPermission(android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) permissionLst.add(android.Manifest.permission.CAMERA)
        if(checkSelfPermission(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) permissionLst.add(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) permissionLst.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)

        if(permissionLst.size>0){
            requestPermissions(permissionLst.toTypedArray(), 101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        grantResults.forEach{
            if(it!=PackageManager.PERMISSION_GRANTED){
                get_permissions()
            }
        }
    }
}