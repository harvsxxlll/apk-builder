package com.example.camera

import android.app.Activity
import android.os.Bundle
import android.view.TextureView
import android.hardware.camera2.*
import android.content.Context
import android.view.Surface
import android.os.Handler
import android.os.HandlerThread
import android.media.ImageReader
import android.graphics.ImageFormat
import java.io.File
import java.io.FileOutputStream
import android.os.Environment
import android.widget.Button
import android.content.Intent

class MainActivity : Activity() {

    lateinit var textureView: TextureView
    lateinit var captureButton: Button
    lateinit var flipButton: Button
    lateinit var galleryButton: Button

    lateinit var cameraManager: CameraManager
    lateinit var cameraDevice: CameraDevice
    lateinit var imageReader: ImageReader

    lateinit var handler: Handler
    lateinit var handlerThread: HandlerThread

    var cameraId = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        textureView = findViewById(R.id.cameraPreview)
        captureButton = findViewById(R.id.captureButton)
        flipButton = findViewById(R.id.flipButton)
        galleryButton = findViewById(R.id.galleryButton)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        handlerThread = HandlerThread("cameraThread")
        handlerThread = HandlerThread("cameraThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {

            override fun onSurfaceTextureAvailable(surface, width, height) {
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(surface, width, height) {}

            override fun onSurfaceTextureDestroyed(surface): Boolean { return false }

            override fun onSurfaceTextureUpdated(surface) {}
        }

        captureButton.setOnClickListener {
            takePicture()
        }

        flipButton.setOnClickListener {
            flipCamera()
        }

        galleryButton.setOnClickListener {
            openGalleryApp()
        }

    }

    fun openCamera() {

        imageReader = ImageReader.newInstance(
            1920,
            1080,
            ImageFormat.JPEG,
            1
        )

        cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {

            override fun onOpened(camera: CameraDevice) {

                cameraDevice = camera

                val texture = textureView.surfaceTexture
                texture.setDefaultBufferSize(1920,1080)

                val surface = Surface(texture)

                val request = camera.createCaptureRequest(
                    CameraDevice.TEMPLATE_PREVIEW
                )

                request.addTarget(surface)

                camera.createCaptureSession(
                    listOf(surface),
                    object : CameraCaptureSession.StateCallback() {

                        override fun onConfigured(session: CameraCaptureSession) {

                            session.setRepeatingRequest(
                                request.build(),
                                null,
                                handler
                            )

                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {}

                    },
                    handler
                )

            }

            override fun onDisconnected(camera: CameraDevice) {}

            override fun onError(camera: CameraDevice, error: Int) {}

        }, handler)

    }

    fun takePicture() {

        val request = cameraDevice.createCaptureRequest(
            CameraDevice.TEMPLATE_STILL_CAPTURE
        )

        request.addTarget(imageReader.surface)

        imageReader.setOnImageAvailableListener({

            val image = imageReader.acquireLatestImage()
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)

            val file = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                ),
                "photo_${System.currentTimeMillis()}.jpg"
            )

            val output = FileOutputStream(file)
            output.write(bytes)
            output.close()

            image.close()

        }, handler)

        cameraDevice.createCaptureSession(
            listOf(imageReader.surface),
            object : CameraCaptureSession.StateCallback() {

                override fun onConfigured(session: CameraCaptureSession) {

                    session.capture(
                        request.build(),
                        null,
                        handler
                    )

                }

                override fun onConfigureFailed(session: CameraCaptureSession) {}

            },
            handler
        )

    }

    fun flipCamera() {

        cameraId = if (cameraId == "0") "1" else "0"

        cameraDevice.close()

        openCamera()

    }

    fun openGalleryApp() {

        val intent = packageManager.getLaunchIntentForPackage("com.example.gallery")

        if (intent != null) {
            startActivity(intent)
        }

    }

}