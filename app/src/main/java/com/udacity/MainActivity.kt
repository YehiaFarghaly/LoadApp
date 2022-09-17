package com.udacity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var URL=""
    private var fileName=""
    private lateinit var notificationManager: NotificationManager
    private lateinit var downloadManager:DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
createChannel(
    "111",
    "Download Channel"
)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            download()
        }
    }


    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("SuspiciousIndentation")
        override fun onReceive(context: Context?, intent: Intent?) {
            val status:String
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if(id==downloadID) {
             val current = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
                if(current.moveToFirst()) {
                 val downloadStatus = current.getInt(current.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    if(downloadStatus==DownloadManager.STATUS_SUCCESSFUL) {
                       status = "Success"
                    }
                    else {
                      status="Fail"
                    }
                }
                else status = "Fail"
                custom_button.buttonState=ButtonState.Completed
                notificationManager=ContextCompat.getSystemService(
                    applicationContext,
                    NotificationManager::class.java
                ) as NotificationManager
                notificationManager.cancelAll()
                notificationManager.sendNotification(status,applicationContext,fileName)
            }
        }
    }

    private fun download() {
        URL = getUrl()
        if(URL.length==0) {
            Toast.makeText(this,"Please choose a file to download",Toast.LENGTH_SHORT).show()
        }
        else {
            custom_button.buttonState = ButtonState.Loading
            val request =
                DownloadManager.Request(Uri.parse(URL))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

             downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }
        }

    private fun getUrl():String{
        return when(radiogroup.checkedRadioButtonId) {
            R.id.glide_btn -> {
                fileName = resources.getString(R.string.radio1)
                glide_url
            }
            R.id.retrofit_btn -> {
                fileName = resources.getString(R.string.radio2)
                retrofit_url
            }
            R.id.loadApp_btn -> {
                fileName = resources.getString(R.string.radio3)
                Load_App_URL
            }
            else -> ""
        }
    }

    companion object {
        private const val Load_App_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
      private const val glide_url=
          "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
          private const val retrofit_url=
              "https://github.com/square/retrofit/archive/refs/heads/master.zip"
    }
    private fun createChannel(channelId:String,channelName:String) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.enableLights(true)
            channel.lightColor=Color.RED
            channel.enableVibration(true)
            channel.description="Download status"
          val notificationManager = this.getSystemService(
              NotificationManager::class.java
          )
            notificationManager.createNotificationChannel(channel)
        }
    }

}
