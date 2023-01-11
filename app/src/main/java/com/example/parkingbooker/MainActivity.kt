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

        val btnAID = btnA.id
        val btnBID = btnB.id
        val btnCID = btnC.id
        val btnDID = btnD.id
        val btnEID = btnE.id
        val btnFID = btnF.id
        val btnGID = btnG.id
        val btnHID = btnH.id
        val btnIID = btnI.id
        val btnJID = btnJ.id
        val btnKID = btnK.id
        val btnLID = btnL.id

        class Slot(var slot: Int , var user: String) {
        }

        val slotA = Slot(btnAID , "user")
        val slotB = Slot(btnBID , "user")
        val slotC = Slot(btnCID , "user")
        val slotD = Slot(btnDID , "user")
        val slotE = Slot(btnEID , "user")
        val slotF = Slot(btnFID , "user")
        val slotG = Slot(btnGID , "user")
        val slotH = Slot(btnHID , "user")
        val slotI = Slot(btnIID , "user")
        val slotJ = Slot(btnJID , "user")
        val slotK = Slot(btnKID , "user")
        val slotL = Slot(btnLID , "user")

        val userID = Firebase.auth.currentUser.toString()

        auth = FirebaseAuth.getInstance()

        fun checkIfTaken(slot: Slot , btn : Button){
            if(slot.user == "user"){
                slot.user = userID
                btn.setBackgroundColor(Color.RED)
            }else if(slot.user != "user" && slot.user != userID){
                Toast.makeText(this , "Ocupado" , Toast.LENGTH_LONG).show()
            } else {
                slot.user = "user"
                btn.setBackgroundColor(Color.parseColor("#3BB143"))
                sendNotification()
            }
        }

        btnA.setOnClickListener{
            checkIfTaken(slotA , btnA )
        }
        btnB.setOnClickListener(){
            checkIfTaken(slotB , btnB )
        }
        btnC.setOnClickListener(){
            checkIfTaken(slotC , btnC )
        }
        btnD.setOnClickListener(){
            checkIfTaken(slotD , btnD )
        }
        btnE.setOnClickListener(){
            checkIfTaken(slotE , btnE )
        }
        btnF.setOnClickListener(){
            checkIfTaken(slotF , btnF )
        }
        btnG.setOnClickListener(){
            checkIfTaken(slotG , btnG )
        }
        btnH.setOnClickListener(){
            checkIfTaken(slotH , btnH )
        }
        btnI.setOnClickListener(){
            checkIfTaken(slotI , btnI )
        }
        btnJ.setOnClickListener(){
            checkIfTaken(slotJ , btnJ )
        }
        btnK.setOnClickListener(){
            checkIfTaken(slotK , btnK )
        }
        btnL.setOnClickListener(){
            checkIfTaken(slotL , btnL )
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
