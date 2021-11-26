package com.example.todolist

import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.messaging.FirebaseMessagingService


class MyFirebaseInstanceIDService: FirebaseMessagingService() {
    val TAG :String = "MyFirebaseIIDService";
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.d(TAG, "Refreshed token: " + s)
    }
}