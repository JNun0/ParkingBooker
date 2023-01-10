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

        var loginbtn = findViewById<MaterialButton>(R.id.loginbtn)
        var signuptxt = findViewById<TextView>(R.id.signuptxt)

        auth = FirebaseAuth.getInstance()

        loginbtn.setOnClickListener {
            login()
        }

        signuptxt.setOnClickListener{
            goToRegister()
        }
    }
    private fun login(){
        var loginemail = findViewById<EditText>(R.id.loginemail)
        var password = findViewById<EditText>(R.id.password)

        val email = loginemail.text.toString()
        val pass = password.text.toString()

        if(email.isBlank() || pass.isBlank()){
            Toast.makeText(this, "Introduce a valid email and password", Toast.LENGTH_SHORT).show()
        }
        else{
            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun goToRegister(){
        val intent= Intent(this,SignUp::class.java)
        startActivity(intent)
    }

}