// Message.kt
package com.example.myapplication

data class Message(
    val sender: String,
    val receiver: String,
    val product: String,
    val content: String,
    val timestamp: Long
)

