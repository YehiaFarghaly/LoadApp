package com.udacity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        var intent = intent
        val name = intent.getStringExtra("fileName")
        val status = intent.getStringExtra("downloadStatus")
        text1.text = name
        text2.text=status
        if(status=="Fail") text2.setTextColor(Color.RED)
        else text2.setTextColor(Color.GREEN)
        ok_btn.setOnClickListener {
           startActivity(Intent(this,MainActivity::class.java))
        }
    }

}
