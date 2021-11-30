package com.example.shareyouralarm

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


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
        sendFCM()
    }

    private fun sendFCM() {
        val tag = "fjAW2dzvUSbhvwifbYZGWuAJwbh2"
        val email = "user1@gmail_com" //should be changed
        val message = "HELP ME!!!" //should be changed
        val mAuth = FirebaseAuth.getInstance()
        val mUser = mAuth.currentUser
        Log.d(TAG, Firebase.database.getReference(tag).child("token").toString())
        Firebase.database.getReference(tag).child("token").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userData = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "1")
                Thread {
                    try {
                        val root = JSONObject()
                        val notification = JSONObject()
                        Log.d(TAG, "2")
                        notification.put("body", message)
                        notification.put("title", mUser!!.email.toString() + "로부터 온 메시지")
                        root.put("notification", notification)
                        root.put("to", userData)
                        val url = URL("https://fcm.googleapis.com/fcm/send")
                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                        Log.d(TAG, "3")
                        conn.requestMethod = "POST"
                        conn.doOutput = true
                        conn.doInput = true
                        conn.addRequestProperty("Authorization", "key=AAAAVoax96o:APA91bHSVhWAbKE9Sy7RkpCjn7lJ04jTSio-Hdms-fAZ1E_kpnB7AIxeNRO4vQ4UOuyB-3B6loP9UQJ796THzRImlnAVs7BUs_yiKpoflqe2qrA12AcMNReMCb97m45ao6JEMlMKV1HX")
                        conn.setRequestProperty("Accept", "application/json")
                        conn.setRequestProperty("Content-type", "application/json")
                        Log.d(TAG, "4")
                        val os: OutputStream = conn.outputStream
                        os.write(root.toString().toByteArray(charset("utf-8")))
                        os.flush()
                        conn.responseCode
                        Log.d(TAG, "5")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.start()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Toast.makeText(this, "취소되었습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }
}