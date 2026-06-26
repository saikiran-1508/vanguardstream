package com.example.vanguardstream.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vanguardstream.ui.camera.CameraPreviewComponent
import com.example.vanguardstream.ui.components.ControlPanel

@Composable
fun StreamDashboardScreen() {
    var ipAddress by remember { mutableStateOf("192.168.1.") }
    var port by remember { mutableStateOf("8554") } // Defaulting to standard RTSP port
    var isStreaming by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background layer: Live Camera Feed
        CameraPreviewComponent(modifier = Modifier.fillMaxSize())

        // Foreground layer: Network Controls
        ControlPanel(
            ipAddress = ipAddress,
            onIpChange = { ipAddress = it },
            port = port,
            onPortChange = { port = it },
            isStreaming = isStreaming,
            onStreamToggle = { isStreaming = !isStreaming },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}