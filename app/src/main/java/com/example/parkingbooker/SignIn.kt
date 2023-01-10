package com.example.parkingbooker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class SignIn : AppCompatActivity() {

    // Creating firebaseAuth object
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        var loginemail = findViewById<EditText>(R.id.loginemail)
        var password = findViewById<EditText>(R.id.password)
        var loginbtn = findViewById<MaterialButton>(R.id.loginbtn)
        var signuptxt = findViewById<TextView>(R.id.signuptxt)

        auth = FirebaseAuth.getInstance()

        loginbtn.setOnClickListener {
            val loginemail = loginemail.text.toString()
            val password = password.text.toString()
            // calling signInWithEmailAndPassword(email, pass)
            // function using Firebase auth object
            // On successful response Display a Toast
            auth.signInWithEmailAndPassword(loginemail, password).addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
            }
        }

        signuptxt.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            // using finish() to end the activity
            finish()
        }
    }
}