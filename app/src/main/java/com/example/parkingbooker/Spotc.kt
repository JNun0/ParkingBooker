package com.example.parkingbooker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class Spotc : AppCompatActivity() {

    private companion object{
        private const val CHANNEL_ID = "channel01"
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private var userIdWhoReserved: String? = null
    private var spotReserved = false

    data class User(
        var name: String? = null,
        var carPlate: String? = null,
        var email: String? = null
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotc)

        var name = findViewById<TextView>(R.id.name)
        var carplate = findViewById<TextView>(R.id.carplate)
        var email = findViewById<TextView>(R.id.email)

        var reserve = findViewById<Button>(R.id.reserve)
        var vacate = findViewById<Button>(R.id.vacate)

        auth = FirebaseAuth.getInstance()
        mFirebaseDatabase =
            FirebaseDatabase.getInstance("https://parkingbooker-74692-default-rtdb.europe-west1.firebasedatabase.app")

        updateUi(name, carplate, email, reserve, vacate)
        setButtonListeners(name, carplate, email, reserve, vacate)
    }

    private fun updateUi(name: TextView, carplate: TextView, email: TextView, reserve: Button, vacate: Button) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)

        spotReserved = sharedPref.getBoolean("spotReserved", false)

        if (!spotReserved) {
            name.text = ""
            carplate.text = ""
            email.text = ""
            reserve.isEnabled = true
            vacate.isEnabled = false
        } else {
            name.text = sharedPref.getString("nameValue", "")
            carplate.text = sharedPref.getString("carPlateValue", "")
            email.text = sharedPref.getString("emailValue", "")
            reserve.isEnabled = false
            vacate.isEnabled = true
        }
    }

    private fun setButtonListeners(name: TextView, carplate: TextView, email: TextView, reserve: Button, vacate: Button) {
        reserve.setOnClickListener {
            reserveSpot(name, carplate, email, reserve, vacate)
        }
        vacate.setOnClickListener {
            vacateSpot(name, carplate, email, reserve, vacate)
        }
    }

    private fun reserveSpot(name: TextView, carplate: TextView, email: TextView, reserve: Button, vacate: Button) {
        val currentUser = auth.currentUser
        val userReference = mFirebaseDatabase.getReference("users").child(currentUser!!.uid)

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                if (user != null) {
                    Log.d("TAG", "Data retrieved: ${user.name}, ${user.carPlate}, ${user.email}")
                    val sharedPref = getPreferences(Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putBoolean("spotReserved", true)
                    editor.putString("nameValue", user.name)
                    editor.putString("carPlateValue", user.carPlate)
                    editor.putString("emailValue", user.email)
                    editor.apply()
                    mFirebaseDatabase.getReference("spots").child("spotC").child("user")
                        .setValue(user)
                    mFirebaseDatabase.getReference("spots").child("spotC").child("userIdWhoReserved")
                        .setValue(currentUser.uid)
                    userIdWhoReserved = currentUser.uid
                    updateUi(name, carplate, email, reserve, vacate)
                    Toast.makeText(this@Spotc, "Spot reserved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Spotc, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Spotc, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun vacateSpot(name: TextView, carplate: TextView, email: TextView, reserve: Button, vacate: Button) {
        val currentUser = auth.currentUser
        val userReference = mFirebaseDatabase.getReference("users").child(currentUser!!.uid)
        mFirebaseDatabase.getReference("spots").child("spotC").child("userIdWhoReserved")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userIdWhoReserved = dataSnapshot.getValue(String::class.java)
                    if (currentUser.uid == userIdWhoReserved) {
                        val sharedPref = getPreferences(Context.MODE_PRIVATE)
                        val editor = sharedPref.edit()

                        editor.putBoolean("spotReserved", false)
                        editor.apply()
                        name.text = ""
                        carplate.text = ""
                        email.text = ""
                        mFirebaseDatabase.getReference("spots").child("spotC").child("user").removeValue()
                        mFirebaseDatabase.getReference("spots").child("spotC")
                            .child("userIdWhoReserved").setValue(null)
                        updateUi(name, carplate, email, reserve, vacate)
                        sendNotification()
                    } else {
                        Toast.makeText(this@Spotc, "Cannot vacate the spot", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@Spotc, "Error", Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun sendNotification(){
        createNotificationChannel()

        val notificationId = 1

        val mainIntent = Intent(this, SignIn::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val mainPendingIntent = PendingIntent.getActivity(this,1,mainIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, "${Spotc.CHANNEL_ID}")

        notificationBuilder.setSmallIcon(R.drawable.car_foreground)

        notificationBuilder.setContentTitle("Vacant")

        notificationBuilder.setContentText("Your spot is vacant")

        notificationBuilder.priority = NotificationCompat.PRIORITY_DEFAULT

        notificationBuilder.setAutoCancel(true)

        notificationBuilder.setContentIntent(mainPendingIntent)

        val notificationManagerCompact = NotificationManagerCompat.from(this)
        notificationManagerCompact.notify(notificationId, notificationBuilder.build())
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "MyNotification"
            val description = "My Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(Spotc.CHANNEL_ID, name, importance)
            notificationChannel.description = description
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
