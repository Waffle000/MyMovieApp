package com.waffle.mymovieapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.waffle.mymovieapp.MainActivity
import com.waffle.mymovieapp.R


class NotificationService : Service() {
    private val handler: Handler = Handler()

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            startNotification()
            handler.postDelayed(this, 60000)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    private fun startNotification() {
        val channelId = "notification_channel_notif"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav)
            .setDestination(R.id.homeFragment)
            .createPendingIntent()
        val builder = NotificationCompat.Builder(
            applicationContext,
            channelId
        )
        val icon = BitmapFactory.decodeResource(
            this.resources,
            R.drawable.ic_logo_movie
        )
        builder.setLargeIcon(icon)
        builder.setSmallIcon(R.drawable.ic_logo_movie)
        builder.setContentTitle("Sudah Menonton Film Apa Hari ini?")
        builder.setContentText("Lihat banyak film baru di aplikasi MyMovie, dan rasakan sensasinya")
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        builder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        builder.priority = NotificationCompat.PRIORITY_MAX
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    "Notification",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.description = "This channel is used by notification service"
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
        notificationManager.notify(176, builder.build())
    }


    private fun startService() {
        val channelId = "notification_channel"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav)
            .setDestination(R.id.homeFragment)
            .createPendingIntent()
        val builder = NotificationCompat.Builder(
            applicationContext,
            channelId
        )
        val icon = BitmapFactory.decodeResource(
            this.resources,
            R.drawable.ic_logo_movie
        )
        builder.setLargeIcon(icon)
        builder.setSmallIcon(R.drawable.ic_logo_movie)
        builder.setContentTitle("MyMovieApp Starting For Notification")
        builder.setContentText("Running...")
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        builder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        builder.priority = NotificationCompat.PRIORITY_MAX
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    "Notification Service",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.description = "This channel is used by notification service"
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
        handler.post(runnable)
        startForeground(Constants.NOTIFICATION_SERVICE_ID, builder.build())
    }

    private fun stopService() {
        handler.removeCallbacks(runnable);
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            if (action != null) {
                if (action == Constants.ACTION_START_NOTIFICATION_SERVICE) {
                    startService()
                } else if (action == Constants.ACTION_STOP_NOTIFICATION_SERVICE) {
                    stopService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}