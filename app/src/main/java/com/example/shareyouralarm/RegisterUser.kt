package com.example.shareyouralarm

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class RegisterUser: AppCompatActivity(), View.OnClickListener {
    // Configure Google Sign In
    private lateinit var mAuth: FirebaseAuth
    private lateinit var banner: TextView
    private lateinit var registerUser: TextView
    private lateinit var editTextFullName: EditText
    private lateinit var editTextAge: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        mAuth = Firebase.auth

        banner = findViewById(R.id.banner)
        banner.setOnClickListener(this)

        registerUser = findViewById(R.id.registerUser)
        registerUser.setOnClickListener(this)

        editTextFullName = findViewById(R.id.fullName)
        editTextAge = findViewById(R.id.age)
        editTextEmail = findViewById(R.id.email)
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
        val fullName: String? = editTextFullName.text.toString().trim()
        val age: String? = editTextAge.text.toString().trim()
        val email: String? = editTextEmail.text.toString().trim()
        val password: String? = editTextPassword.text.toString().trim()

        if (fullName!!.isEmpty()) {
            editTextFullName.error = "Full name is required!"
            editTextFullName.requestFocus()
            return
        }
        if (age!!.isEmpty()) {
            editTextAge.error = "Age is required!"
            editTextAge.requestFocus()
            return
        }
        if (email!!.isEmpty()) {
            editTextEmail.error = "Email is required!"
            editTextEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Please provide valid email!"
            editTextEmail.requestFocus()
            return
        }
        if (password!!.isEmpty()) {
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
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    val user = User(fullName, age, email)
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

                                // redirect to login layout!
                                startActivity(Intent(this, MainActivity::class.java))
                            } else {
                                Toast.makeText(
                                    this@RegisterUser,
                                    "Failed to register! Try again!",
                                    Toast.LENGTH_LONG
                                ).show()
                                progressBar.visibility = View.GONE
                            }
                        }
                } else {
                    //Registration error
                    Toast.makeText(
                        this@RegisterUser,
                        "Failed to register! Try again!",
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }

    }
}