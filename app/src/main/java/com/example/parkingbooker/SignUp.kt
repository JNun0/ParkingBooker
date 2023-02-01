package com.example.parkingbooker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {

    private lateinit var  auth: FirebaseAuth
    private var mFirebaseDatabaseInstances: FirebaseDatabase?=null
    private var mFirebaseDatabase: DatabaseReference?=null
    private var userId:String?=null


    data class User(var name: String? = null,var carPlate: String? = null, var email: String? = null)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        var signin_txt = findViewById<TextView>(R.id.signintxt)
        var signup_button = findViewById<MaterialButton>(R.id.signup_button)


        auth = FirebaseAuth.getInstance()
        mFirebaseDatabaseInstances= FirebaseDatabase.getInstance("https://parkingbooker-74692-default-rtdb.europe-west1.firebasedatabase.app")


        signup_button.setOnClickListener {
            register()
        }

        signin_txt.setOnClickListener {
            goToLogin()
        }

    }
    private fun register(){

        var emai = findViewById<EditText>(R.id.email)
        var password_signup = findViewById<EditText>(R.id.password_signup)
        var password_confirm = findViewById<EditText>(R.id.password_confirm)
        var name_txt = findViewById<EditText>(R.id.nome)
        var car_plate_txt = findViewById<EditText>(R.id.matricula)

        val name = name_txt.text.toString()
        val car_plate = car_plate_txt.text.toString()
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
                    //Toast.makeText(this, "Creation Successful", Toast.LENGTH_SHORT).show()

                    mFirebaseDatabase = mFirebaseDatabaseInstances!!.getReference("users")
                    val user = FirebaseAuth.getInstance().currentUser
                    userId = user!!.uid
                    val myUser = User(name,car_plate,email)
                    mFirebaseDatabase!!.child(userId!!).setValue(myUser).addOnSuccessListener {
                        Log.d("TAG", "Data saved successfully.")
                        Toast.makeText(this, "Creation Successful", Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener { exception ->
                        Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
                    }


                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun goToLogin(){
        val intent = Intent(this,SignIn::class.java)
        finish()
        startActivity(intent)
    }

}