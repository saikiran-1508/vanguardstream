package com.example.vanguardstream.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vanguardstream.streaming.StreamState
import com.example.vanguardstream.ui.camera.CameraPreviewComponent
import com.example.vanguardstream.ui.components.ControlPanel
import com.example.vanguardstream.viewmodel.StreamViewModel

@Composable
fun StreamDashboardScreen() {
    // 1. We create the Brain here
    val viewModel = remember { StreamViewModel() }

    // 2. We ask the Brain if we are currently streaming
    val streamState by viewModel.streamState.collectAsState()
    val isStreaming = streamState == StreamState.STREAMING

    var ipAddress by remember { mutableStateOf(viewModel.targetIp) }
    var port by remember { mutableStateOf(viewModel.targetPort) }

    Box(modifier = Modifier.fillMaxSize()) {

        // 3. The Camera gives every picture it takes straight to the Brain
        CameraPreviewComponent(
            onFrameCaptured = { imageProxy ->
                viewModel.processCameraFrame(imageProxy)
            },
            modifier = Modifier.fillMaxSize()
        )

        // 4. The Buttons tell the Brain when you click START or STOP
        ControlPanel(
            ipAddress = ipAddress,
            onIpChange = {
                ipAddress = it
                viewModel.targetIp = it
            },
            port = port,
            onPortChange = {
                port = it
                viewModel.targetPort = it
            },
            isStreaming = isStreaming,
            onStreamToggle = { viewModel.toggleStream() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}