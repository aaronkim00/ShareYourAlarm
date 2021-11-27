package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class ProfileActivity : AppCompatActivity() {
    private lateinit var logout: Button
    private lateinit var user: FirebaseUser
    private lateinit var reference: DatabaseReference

    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        logout = findViewById(R.id.signOut)
        logout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
            }
        })

        user = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users")
        userID = user.uid

        val greetingTextView: TextView = findViewById(R.id.greeting)
        val fullNameTextView: TextView = findViewById(R.id.fullName)
        val emailTextView: TextView = findViewById(R.id.emailAddress)
        val ageTextView: TextView = findViewById(R.id.age)

        reference.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userProfile = snapshot.getValue(
                    User::class.java)
                if (userProfile != null) {
                    val fullName = userProfile.fullName
                    val email = userProfile.email
                    val age = userProfile.age
                    greetingTextView.text = "Welcome, $fullName!"
                    fullNameTextView.text = fullName
                    emailTextView.text = email
                    ageTextView.text = age
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "Something wrong happened!", Toast.LENGTH_LONG).show()
            }
        })


    }
}