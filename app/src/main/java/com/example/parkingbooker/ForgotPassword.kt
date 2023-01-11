package com.example.parkingbooker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        var loginemail = findViewById<EditText>(R.id.loginemail)
        var sendbtn = findViewById<MaterialButton>(R.id.sendbtn)


        sendbtn.setOnClickListener {
            var email = loginemail.text.toString()
            if (email.isEmpty()){
                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
            }
            else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Email sent successfully", Toast.LENGTH_SHORT).show()
                        val intent= Intent(this,SignIn::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}