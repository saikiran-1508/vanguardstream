package com.example.vanguardstream.streaming

// This is just a simple list of the different "moods" our app can be in.
enum class StreamState {
    STANDBY,   // Waiting for the user to press start
    STREAMING  // Actively throwing frisbees (pictures) to the computer
}