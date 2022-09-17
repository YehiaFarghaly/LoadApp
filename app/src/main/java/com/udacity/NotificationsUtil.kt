package com.udacity

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(
    status: String,
    applicationContext: Context,
    fileName: String
) {
    val intent = Intent(applicationContext, DetailActivity::class.java)
    intent.putExtra("downloadStatus", status)
    intent.putExtra("fileName", fileName)
    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val bitmap: Bitmap = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.download
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        "111"
    ).setSmallIcon(R.drawable.download)
        .setContentTitle("LoadApp")
        .setContentText("Your Download Status")
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setLargeIcon(bitmap)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotification() {
    cancelAll()
}