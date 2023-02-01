package com.example.parkingbooker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Spota : AppCompatActivity() {

    private lateinit var  auth: FirebaseAuth
    private var mFirebaseDatabaseInstances: FirebaseDatabase?=null
    private var mFirebaseDatabase: DatabaseReference?=null
    var userIdWhoReserved: String? = null
    var spotReserved: Boolean = false


    data class User(var name: String? = null,var carPlate: String? = null, var email: String? = null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spota)

        var name = findViewById<TextView>(R.id.name)
        var carplate = findViewById<TextView>(R.id.carplate)
        var email = findViewById<TextView>(R.id.email)

        var reserve = findViewById<Button>(R.id.reserve)
        var vacate = findViewById<Button>(R.id.vacate)

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()


        auth = FirebaseAuth.getInstance()
        mFirebaseDatabaseInstances= FirebaseDatabase.getInstance("https://parkingbooker-74692-default-rtdb.europe-west1.firebasedatabase.app")


        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                if (user != null) {

                    editor.putString("nameValue", user.name)
                    editor.putString("carPlateValue", user.carPlate)
                    editor.putString("emailValue", user.email)
                    editor.apply()

                    if (sharedPref.getBoolean("spotReserved", false)) {
                        name.text = sharedPref.getString("nameValue", "")
                        carplate.text = sharedPref.getString("carPlateValue", "")
                        email.text = sharedPref.getString("emailValue", "")
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", "Error getting Data from Database.")
            }
        }

        if (name.text.toString() == "" && carplate.text.toString()== "" && email.text.toString()== "" && spotReserved == false){
            reserve.isEnabled = true
            vacate.isEnabled = false
        }
        else{
            name.text = sharedPref.getString("nameValue", "")
            carplate.text = sharedPref.getString("carPlateValue", "")
            email.text = sharedPref.getString("emailValue", "")
            spotReserved = sharedPref.getBoolean("spotReserved", true)
            reserve.isEnabled = true
            vacate.isEnabled = false
            spotReserved = true
        }

        reserve.setOnClickListener {
            var user = FirebaseAuth.getInstance().currentUser
            val currentUserId = user!!.uid
            userIdWhoReserved = user.uid
            mFirebaseDatabase = mFirebaseDatabaseInstances!!.getReference("users/$currentUserId")
            mFirebaseDatabase!!.addValueEventListener(valueEventListener)
            Toast.makeText(this, "Spot Reserved", Toast.LENGTH_SHORT).show()
            spotReserved = true
            reserve.isEnabled = false
            vacate.isEnabled = true

        }
        vacate.setOnClickListener {
            if (mFirebaseDatabase != null) {
                mFirebaseDatabase!!.removeEventListener(valueEventListener)
                var user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    val currentUserId = user!!.uid
                    if(currentUserId == userIdWhoReserved) {
                        name.text = ""
                        carplate.text = ""
                        email.text = ""
                        editor.remove("nameValue")
                        editor.remove("carPlateValue")
                        editor.remove("emailValue")
                        editor.remove("spotReserved")
                        editor.apply()
                        spotReserved = false
                        reserve.isEnabled = true
                        vacate.isEnabled = false
                    } else {
                        Toast.makeText(this, "Spot not vacated", Toast.LENGTH_SHORT).show()
                    }
                    Toast.makeText(this, "Spot vacated", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("TAG", "Error.")
                }
            }
        }
    }
}