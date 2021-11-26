package com.example.todolist

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG: String = "MyFirebaseMessagingService"
    @SuppressLint("LongLogTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // 앱이 실행 중일 때 (Foreground 상황) 에서 푸쉬를 받으면 호출됩니다.
        // 백그라운드 상황에서는 호출되지 않고 그냥 알림목록에 알림이 추가됩니다. Log.d(TAG,"Message Arrived");
        Log.d(TAG, "hello")
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "FCM Data Message : " + remoteMessage.data)
        }
        if (remoteMessage.notification != null) {
            val messageBody = remoteMessage.notification!!.body
            Log.d(TAG, "FCM Notification Message Body : $messageBody")
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Toast.makeText(applicationContext, messageBody, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingService"
    }
}