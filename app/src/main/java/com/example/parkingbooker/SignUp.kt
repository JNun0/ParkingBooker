package com.example.parkingbooker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.math.sign

class SignUp : AppCompatActivity() {

    private lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        var username_signup = findViewById<EditText>(R.id.username_signup)
        var signup_button = findViewById<MaterialButton>(R.id.signup_button)

        auth= FirebaseAuth.getInstance()

        signup_button.setOnClickListener {
            register()
        }

    }
    private fun register(){
        var emai = findViewById<EditText>(R.id.email)
        var password_signup = findViewById<EditText>(R.id.password_signup)
        var password_confirm = findViewById<EditText>(R.id.password_confirm)

        val email = emai.text.toString()
        val password = password_signup.text.toString()
        val passwordConf = password_confirm.text.toString()

        if(email.isBlank() || password.isBlank() || passwordConf.isBlank()){
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
        }
        else if(password != passwordConf){
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show()
        }
        else{
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Creation Successful", Toast.LENGTH_SHORT).show()
                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }

    fun goToLogin(){
        val intent= Intent(this,SignIn::class.java)
        startActivity(intent)
    }
}