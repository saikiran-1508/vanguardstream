package com.example.vanguardstream.viewmodel

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import com.example.vanguardstream.streaming.FrameStreamer
import com.example.vanguardstream.streaming.StreamState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StreamViewModel : ViewModel() {
    // The Brain owns the Artist (FrameStreamer)
    private val frameStreamer = FrameStreamer()

    // I put your desktop's exact IP address here to make it easy!
    var targetIp = "192.168.1.6"
    var targetPort = "8554"

    // This keeps track of whether the button says START or STOP
    private val _streamState = MutableStateFlow(StreamState.STANDBY)
    val streamState: StateFlow<StreamState> = _streamState.asStateFlow()

    fun toggleStream() {
        if (_streamState.value == StreamState.STANDBY) {
            _streamState.value = StreamState.STREAMING
        } else {
            _streamState.value = StreamState.STANDBY
        }
    }

    // Every time the camera takes a picture, it hands it to this function
    fun processCameraFrame(imageProxy: ImageProxy) {
        if (_streamState.value == StreamState.STREAMING) {
            // If we are streaming, throw it to the computer!
            frameStreamer.processAndSendFrame(
                imageProxy = imageProxy,
                ipAddress = targetIp,
                port = targetPort.toIntOrNull() ?: 8554
            )
        } else {
            // If we are NOT streaming, we just throw the picture in the trash
            // so the camera doesn't freeze.
            imageProxy.close()
        }
    }
}