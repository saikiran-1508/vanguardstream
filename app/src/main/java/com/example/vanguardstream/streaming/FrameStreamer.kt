package com.example.vanguardstream.streaming

import android.graphics.Bitmap
import android.graphics.Matrix // <-- We added this to spin the image!
import androidx.camera.core.ImageProxy
import com.example.vanguardstream.network.UdpVideoTransmitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class FrameStreamer {

    private val streamScope = CoroutineScope(Dispatchers.IO)

    fun processAndSendFrame(
        imageProxy: ImageProxy,
        ipAddress: String,
        port: Int
    ) {
        try {
            // 1. Grab the picture from the camera
            val originalBitmap = imageProxy.toBitmap()

            // 2. Ask the camera: "How much is the phone tilted right now?"
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees.toFloat()

            // 3. Create a spinning tool (Matrix) and spin the picture!
            val matrix = Matrix().apply { postRotate(rotationDegrees) }
            val rotatedBitmap = Bitmap.createBitmap(
                originalBitmap, 0, 0,
                originalBitmap.width, originalBitmap.height,
                matrix, true
            )

            val stream = ByteArrayOutputStream()

            // 4. The Fast Artist: Compress the SPUN picture
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream)
            val byteArray = stream.toByteArray()

            streamScope.launch {
                UdpVideoTransmitter.sendFrame(ipAddress, port, byteArray)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // CRITICAL STEP: Close the image so the camera doesn't freeze.
            imageProxy.close()
        }
    }
}