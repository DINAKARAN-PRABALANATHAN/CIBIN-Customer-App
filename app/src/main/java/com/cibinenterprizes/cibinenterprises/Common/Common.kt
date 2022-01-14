package com.cibinenterprizes.cibinenterprises.Common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.cibinenterprizes.cibinenterprises.Model.Token
import com.cibinenterprizes.cibinenterprises.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object Common {

    var user = FirebaseAuth.getInstance().currentUser

    fun updateToken(context: Context, token: String) {
        FirebaseDatabase.getInstance().getReference().child("Tokens").child(user?.uid.toString()).setValue(Token(token)).addOnFailureListener {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }
    fun showNotification(context: Context, id: Int, title: String?, content: String?) {

        val NOTIFICATION_CHANNEL_ID = "com.cibinenterprizes.cibinenterprises"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID,"Cibin Notification",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.description = "Cibin Notification"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = (Color.GREEN)

            notificationManager.createNotificationChannel(notificationChannel)
        }
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        builder.setContentTitle(title).setContentText(content).setAutoCancel(true)
            .setSmallIcon(R.drawable.logo)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content).setBigContentTitle(title).setSummaryText("Work Update"))
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.logo))


        val notification = builder.build()
        notificationManager.notify(id,notification)
    }
}