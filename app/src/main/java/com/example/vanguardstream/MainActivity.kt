package com.example.vanguardstream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.vanguardstream.ui.camera.CameraPermissionWrapper
import com.example.vanguardstream.ui.screen.StreamDashboardScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    CameraPermissionWrapper(
                        onPermissionGranted = {
                            StreamDashboardScreen()
                        }
                    )
                }
            }
        }
    }
}