package com.example.todolist

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
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
    }

    private fun saveToDo() {
        TODO("Not yet implemented")
    }

}