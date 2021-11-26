package com.example.todolist

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessaging.getInstance


class MainActivity : AppCompatActivity() {
    lateinit var mainFragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainFragment = MainFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, mainFragment).commit()
        val saveButton: Button = findViewById(R.id.saveButton)
        Log.d("hello", "oh")
        saveButton.setOnClickListener {
            saveToDo()
            Toast.makeText(applicationContext, "추가되었습니다.", Toast.LENGTH_SHORT).show()
        }
        //registerPushToken()
    }

    private fun saveToDo() {
        TODO("Not yet implemented")
    }

    private fun registerPushToken() {
        Log.d("hello", "a")
        var uid = Firebase.auth.currentUser!!.uid
        Log.d("hello", "b")
        var map = mutableMapOf<String, Any>()
        Log.d("hello", "c")
        getInstance().token.addOnCompleteListener { task ->
            if(task.isSuccessful){
                var pushToken = task.result?:""
                map["pushtoken"] = pushToken!!
                FirebaseFirestore.getInstance().collection("pushtokens").document(uid!!).set(map)
            }
        }
    }
}