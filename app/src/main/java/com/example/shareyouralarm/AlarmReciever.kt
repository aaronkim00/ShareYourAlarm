package com.example.shareyouralarm

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    val TAG = "AlarmReceiver"
    override fun onReceive(context: Context?, intent: Intent?){
        Log.d(TAG, "Received something!")
        val i = Intent(context, DestinationActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)

        val builder = NotificationCompat.Builder(context!!, "foxandroid")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Foxandroid Alarm Manager")
                .setContentText("Subscribe for more android related content")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123, builder.build())
        sendNotification("hello", "world")
    }

    private fun sendNotification(body:String, title:String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val message = Message("/topics/subscribed", Notification(body, title))
            val response = RetrofitInstance.api.postNotification(message)
            if(response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(message)}")
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}