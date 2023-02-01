package com.example.parkingbooker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompatSideChannelService
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private companion object{
        private const val CHANNEL_ID = "channel01"
    }

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val btnA: Button = findViewById(R.id.slotA)
        val btnB: Button = findViewById(R.id.slotB)
        val btnC: Button = findViewById(R.id.slotC)
        val btnD: Button = findViewById(R.id.slotD)
        val btnE: Button = findViewById(R.id.slotE)
        val btnF: Button = findViewById(R.id.slotF)
        val btnG: Button = findViewById(R.id.slotG)
        val btnH: Button = findViewById(R.id.slotH)
        val btnI: Button = findViewById(R.id.slotI)
        val btnJ: Button = findViewById(R.id.slotJ)
        val btnK: Button = findViewById(R.id.slotK)
        val btnL: Button = findViewById(R.id.slotL)
        val logout = findViewById<Button>(R.id.logout)

        btnA.setOnClickListener{
            val intent = Intent(this,Spota::class.java)
            startActivity(intent)
        }
        btnB.setOnClickListener(){
            val intent = Intent(this,Spotb::class.java)
            startActivity(intent)
        }
        btnC.setOnClickListener(){
            val intent = Intent(this,Spotc::class.java)
            startActivity(intent)
        }
        btnD.setOnClickListener(){
            val intent = Intent(this,Spotd::class.java)
            startActivity(intent)
        }
        btnE.setOnClickListener(){
            val intent = Intent(this,Spote::class.java)
            startActivity(intent)
        }
        btnF.setOnClickListener(){
            val intent = Intent(this,Spotf::class.java)
            startActivity(intent)
        }
        btnG.setOnClickListener(){
            val intent = Intent(this,Spotg::class.java)
            startActivity(intent)
        }
        btnH.setOnClickListener(){
            val intent = Intent(this,Spoth::class.java)
            startActivity(intent)
        }
        btnI.setOnClickListener(){
            val intent = Intent(this,Spoti::class.java)
            startActivity(intent)
        }
        btnJ.setOnClickListener(){
            val intent = Intent(this,Spotj::class.java)
            startActivity(intent)
        }
        btnK.setOnClickListener(){
            val intent = Intent(this,Spotk::class.java)
            startActivity(intent)
        }
        btnL.setOnClickListener(){
            val intent = Intent(this,Spotl::class.java)
            startActivity(intent)
        }
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,SignIn::class.java)
            finish()
            startActivity(intent)
        }

    }

    private fun sendNotification(){
        createNotificationChannel()

        val notificationId = 1

        val mainIntent = Intent(this, SignIn::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val mainPendingIntent = PendingIntent.getActivity(this,1,mainIntent,PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, "$CHANNEL_ID")

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
            val notificationChannel = NotificationChannel(CHANNEL_ID, name, importance)
            notificationChannel.description = description
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
