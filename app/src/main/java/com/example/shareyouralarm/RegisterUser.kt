package com.example.shareyouralarm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging


class RegisterUser: AppCompatActivity(), View.OnClickListener {
    // Configure Google Sign In
    private lateinit var mAuth: FirebaseAuth
    private lateinit var banner: TextView
    private lateinit var registerUser: TextView
    private lateinit var editTextFullName: EditText
    private lateinit var editTextRoomNum: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextGroupID: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var progressBar: ProgressBar
    private val TAG = "RegisterUser"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        mAuth = Firebase.auth

        banner = findViewById(R.id.banner)
        banner.setOnClickListener(this)

        registerUser = findViewById(R.id.registerUser)
        registerUser.setOnClickListener(this)

        editTextFullName = findViewById(R.id.fullName)
        editTextRoomNum = findViewById(R.id.roomNum)
        editTextEmail = findViewById(R.id.email)
        editTextGroupID = findViewById(R.id.groupID)
        editTextPassword = findViewById(R.id.password)

        progressBar = findViewById(R.id.progressBar)
    }

    /*public override fun onStart(){
        super.onStart()
        val currentUser = mAuth.currentUser
        if(currentUser != null){
            reload()
        }
    }

    private fun reload() {
        TODO("Not yet implemented")
    }*/

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id) {
                R.id.banner -> startActivity(Intent(this, MainActivity::class.java))
                R.id.registerUser -> registerUser()
            }
        }
    }

    private fun registerUser() {
        val fullName = editTextFullName.text.toString().trim()
        val roomNum = editTextRoomNum.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val groupID = editTextGroupID.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val token = MyFirebaseMessagingService.getToken(this)

        Log.d(TAG, token)

        if (fullName.isEmpty()) {
            editTextFullName.error = "Full name is required!"
            editTextFullName.requestFocus()
            return
        }
        if (roomNum.isEmpty()) {
            editTextRoomNum.error = "Room Number is required!"
            editTextRoomNum.requestFocus()
            return
        }
        if (email.isEmpty()) {
            editTextEmail.error = "Email is required!"
            editTextEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Please provide valid email!"
            editTextEmail.requestFocus()
            return
        }
        if(groupID.isEmpty()){
            editTextGroupID.error = "Group ID is required!"
            editTextGroupID.requestFocus()
            return
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!")
            editTextPassword.requestFocus()
            return
        }
        if (password.length < 6) {
            editTextPassword.setError("Min password length should be 6 characters!")
            editTextPassword.requestFocus()
            return
        }
        progressBar.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = User(fullName, roomNum, email, token, groupID)
                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(user).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@RegisterUser,
                                    "User has been registered successfully!",
                                    Toast.LENGTH_LONG
                                ).show()
                                progressBar.visibility = View.GONE

                                Firebase.messaging.subscribeToTopic(groupID)
                                    .addOnCompleteListener { task ->
                                        var msg = "Successfully subscribed"
                                        if (!task.isSuccessful) {
                                            msg = "Subscription failed"
                                        }
                                        Log.d(TAG, msg)
                                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                                    }

                                startActivity(Intent(this, MainActivity::class.java))
                            } else {
                                Toast.makeText(
                                    this@RegisterUser,
                                    "Failed to register! Try again! - 11111",
                                    Toast.LENGTH_LONG
                                ).show()
                                progressBar.visibility = View.GONE
                            }
                        }
                } else {
                    //Registration error
                    Toast.makeText(
                        this@RegisterUser,
                        "Failed to register! Try again! - 22222",
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }

    }
}