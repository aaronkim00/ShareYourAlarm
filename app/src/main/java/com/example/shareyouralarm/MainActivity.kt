package com.example.shareyouralarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    val TAG = "MainActivity"

    private lateinit var register: TextView
    private lateinit var forgotPassword: TextView
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var signIn: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register=findViewById(R.id.register)
        register.setOnClickListener(this)

        signIn=findViewById(R.id.signIn)
        signIn.setOnClickListener(this)

        editTextEmail=findViewById(R.id.email)
        editTextPassword=findViewById(R.id.password)

        progressBar=findViewById(R.id.progressBar)

        mAuth = FirebaseAuth.getInstance()

        forgotPassword=findViewById(R.id.forgotPassword)
        forgotPassword.setOnClickListener(this)

        FirebaseInstallations.getInstance().getToken(false)
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }
                    // Get new Instance ID token
                    Log.d("hello", task.result!!.token.toString())
                })
        /*logTokenButton.setOnClickListener(View.OnClickListener() {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                Log.d(TAG, msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })
        })*/

    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id){
                R.id.register -> startActivity(Intent(this, RegisterUser::class.java))
                R.id.signIn -> userLogin()
                R.id.forgotPassword -> startActivity(Intent(this, ForgotPassword::class.java))
            }
        }
    }

    
    private fun userLogin() {
        val email: String = editTextEmail.text.toString().trim()
        val password: String = editTextPassword.text.toString().trim()

        if(email.isEmpty()){
            editTextEmail.error="Email is required!"
            editTextEmail.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.error="Please enter a valid email!"
            editTextEmail.requestFocus()
            return
        }

        if(password.length < 6){
            editTextPassword.error="Min password length is 6 characters!"
            editTextPassword.requestFocus()
            return
        }

        progressBar.setVisibility(View.VISIBLE)

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //redirect to user profile
                Log.d("hello", "good")
                startActivity(Intent(this, ProfileActivity::class.java))
            } else {
                Toast.makeText(this@MainActivity,
                        "Failed to login! Please check your credentials",
                        Toast.LENGTH_LONG).show()
            }
        }
    }


}