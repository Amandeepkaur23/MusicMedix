package com.example.musictherapy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var btn_home: Button? = null
    private var btn_userP: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        listener()
    }
    private fun init()
    {
        btn_home = findViewById(R.id.btn_home)
        btn_userP = findViewById(R.id.btn_userP)
    }
    private fun listener()
    {
        btn_home?.setOnClickListener(this)
        btn_userP?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_home->{
                val intent = Intent(this, UserInputActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_userP->{
                val intent = Intent(this, PlaylistActivity::class.java)
                startActivity(intent)
            }
        }
    }
}