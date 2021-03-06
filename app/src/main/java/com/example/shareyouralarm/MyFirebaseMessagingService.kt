package com.example.shareyouralarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "MyFirebaseMessagingService"
    /**
     * 메시지 수신받는 메소드
     * @param msg
     */
    override fun onMessageReceived(msg: RemoteMessage) {
        Log.i("### msg : ", msg.toString())
        if (msg.data.size > 0) {
            Log.i("### data : ", msg.data.toString())
            sendTopNotification(msg.data["title"], msg.data["content"])
            if (true) {
                scheduleJob()
            } else {
                handleNow()
            }
        }
        if (msg.notification != null) {
            Log.i("### notification : ", msg.notification!!.body!!)
            sendTopNotification(msg.notification!!.title, msg.notification!!.body)
        }
    }

    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .build()
        WorkManager.getInstance().beginWith(work).enqueue()
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    private fun sendTopNotification(title: String?, content: String?) {
        val CHANNEL_DEFAULT_IMPORTANCE = "channel_id"
        val ONGOING_NOTIFICATION_ID = 1
        val notificationIntent = Intent(this, MyFirebaseMessagingService::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification: Notification = Notification.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .build()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_DEFAULT_IMPORTANCE,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(ONGOING_NOTIFICATION_ID, notification)
    }

    companion object {
        fun getToken(context: Context): String {
            return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty").toString()
        }
    }
}