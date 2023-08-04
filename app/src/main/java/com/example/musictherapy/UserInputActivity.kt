package com.example.musictherapy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class UserInputActivity : AppCompatActivity(), View.OnClickListener {
    private var btn_Music: Button? = null
    //Edit text
    lateinit var ed_bpS: EditText
    lateinit var ed_bpD: EditText
    lateinit var ed_hr: EditText
    lateinit var ed_breathe: EditText
//    var BP = ed_bp!!.text.toString().toInt()
//    var HR = ed_hr!!.text.toString().toInt()
//    var BR = ed_breathe!!.text.toString().toInt()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_input)
        init()
        listener()

    }
    private fun init()
    {
        btn_Music = findViewById(R.id.btn_Music)
        ed_bpS = findViewById(R.id.ed_bpS)
        ed_bpD = findViewById(R.id.ed_bpD)
        ed_hr = findViewById(R.id.ed_hr)
        ed_breathe = findViewById(R.id.ed_breathe)
    }
    private fun listener()
    {
        btn_Music?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_Music->{
                val bpSText = ed_bpS.text.toString()
                val bpDText = ed_bpD.text.toString()
                val hrText = ed_hr.text.toString()
                val brText = ed_breathe.text.toString()

                if (bpSText.isEmpty() || bpDText.isEmpty() || hrText.isEmpty() || brText.isEmpty()) {
                    Toast.makeText(this, "Please enter all values", Toast.LENGTH_SHORT).show()
                    return
                }
                val BPS = bpSText.toIntOrNull()
                val BPD = bpSText.toIntOrNull()
                val HR = hrText.toIntOrNull()
                val BR = brText.toIntOrNull()

                if (BPS == null ||BPD == null || HR == null || BR == null) {
                    Toast.makeText(this, "Please enter valid numeric values", Toast.LENGTH_SHORT).show()
                    return
                }

                val intent = Intent(this, MusicPlayerActivity::class.java)
                intent.putExtra("ed_bpS", BPS)
                intent.putExtra("ed_bpS", BPD)
                intent.putExtra("ed_hr", HR)
                intent.putExtra("ed_breathe", BR)
                startActivity(intent)
            }
        }
    }

}