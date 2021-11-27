package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity(), View.OnClickListener {
    /*lateinit var mainFragment: Fragment
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
    }*/

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