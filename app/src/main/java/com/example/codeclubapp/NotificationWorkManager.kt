package com.example.codeclubapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorkManager(context: Context, parameters: WorkerParameters): Worker(context, parameters) {
    private val PRIMARY_CHANNEL_ID = "primary_channel_id"
    private val NOTIFICATION_ID = 0
    private var notificationWorkManager: NotificationManager? = null

    override fun doWork(): Result {
        val name = inputData.getString("já olhou o feed hoje?")
        return name?.let {
            createChannel()
            sendNotification(it)
            Result.success()
        } ?: Result.failure()
    }

    private fun sendNotification(taskName: String){
        val notificationBuilder: NotificationCompat.Builder = getNotificationBuilder(taskName)

        //DELIVERY
        notificationWorkManager?.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getNotificationBuilder(taskName: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, PRIMARY_CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(taskName)
            .setContentText(applicationContext.getText(androidx.core.R.string.call_notification_screening_text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }
    private fun createChannel(){
        notificationWorkManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                applicationContext.getString(com.google.android.gms.base.R.string.common_google_play_services_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableVibration(true)

            notificationWorkManager?.createNotificationChannel(notificationChannel)
        }
    }
}