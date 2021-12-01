package com.example.shareyouralarm

data class PushNotification(
    val data: NotificationData,
    val to: String
)