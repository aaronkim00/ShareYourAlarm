package com.example.todolist

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth


class ForgotPassword : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var resetPasswordButton: Button
    private lateinit var progressBar: ProgressBar

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        emailEditText = findViewById(R.id.email)
        resetPasswordButton = findViewById(R.id.resetPassword)
        progressBar = findViewById(R.id.progressBar)

        auth = FirebaseAuth.getInstance()

        resetPasswordButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                resetPassword()
            }
        })

    }

    private fun resetPassword(){
        var email = emailEditText.text.toString().trim()

        if(email.isEmpty()){
            emailEditText.setError("Email is required!")
            emailEditText.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide valid email!")
            emailEditText.requestFocus()
            return
        }

        progressBar.visibility=View.VISIBLE
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@ForgotPassword,
                        "Check our email to rest your password!",
                        Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@ForgotPassword,
                        "Try again! Something wrong happed!",
                        Toast.LENGTH_LONG).show()
                }
            }
    }
}