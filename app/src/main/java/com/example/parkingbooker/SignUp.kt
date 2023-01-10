package com.example.parkingbooker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {

    lateinit var email : EditText
    lateinit var password_confirm : EditText
    private lateinit var password_signup : EditText
    private lateinit var signup_button : MaterialButton

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        var username_signup = findViewById<EditText>(R.id.username_signup)
        var email = findViewById<EditText>(R.id.email)
        var password_signup = findViewById<EditText>(R.id.password_signup)
        var password_confirm = findViewById<EditText>(R.id.password_confirm)
        var signup_button = findViewById<MaterialButton>(R.id.signup_button)

        auth = Firebase.auth

        signup_button.setOnClickListener {
            signUpUser()
        }

    }
    private fun signUpUser() {
        val email = email.text.toString()
        val pass = password_signup.text.toString()
        val confirmPassword = password_confirm.text.toString()

        // check pass
        if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }
        // If all credential are correct
        // We call createUserWithEmailAndPassword
        // using auth object and pass the
        // email and pass in it.
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully Signed Up", Toast.LENGTH_SHORT).show()
                finish()
                val intent = Intent(this, SignIn::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Signed Up Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}