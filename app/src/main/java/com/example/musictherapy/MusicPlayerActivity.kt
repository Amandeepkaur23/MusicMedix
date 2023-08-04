package com.example.musictherapy

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import com.example.musictherapy.model.Mp3Files
import java.util.concurrent.TimeUnit

class MusicPlayerActivity : AppCompatActivity(), View.OnClickListener {
    private var seekbar: SeekBar? = null
    private var ic_play_circle: ImageView? = null   //icon for play
    private var music: MediaPlayer? = null
    private var ic_arrow_back: ImageView? = null
    private var ic_arrow_forward: ImageView? = null
    private var continue_music_preference:Button? = null
    private var isplay: Boolean = true     //for toggling
    private var handler:Handler = Handler()

    private var generate_random: Long = 0
    private var dec_anixety: RelativeLayout? = null

    private var pausedLength: Int = 0

    //display time duration
    lateinit var txt_start_time: TextView
    lateinit var txt_end_time: TextView

    private var txt_music_name: TextView? = null
    private var arrayList: ArrayList<Mp3Files> =
        ArrayList()   //create an arraylist for storing music files
    var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)
        // adding music in arraylist
//        arrayList.add(
//            Mp3Files(R.raw.threelittlebirds)
//        )
//        arrayList.add(
//            Mp3Files(R.raw.wewillrockyou)
//        )
//        arrayList.add(
//            Mp3Files(R.raw.youaremysunshine)
//        )

        //function call


        var BPSmusic = intent.getIntExtra("ed_bpS",0)
        var BPDmusic = intent.getIntExtra("ed_bpD",0)
        var HRmusic = intent.getIntExtra("ed_hr",0)
        var BRmusic = intent.getIntExtra("ed_breathe",0)

        if(BPSmusic >= 120)
        {
            if(BPDmusic>=80)
            {
                if(HRmusic > 80 )
                {
                    if(BRmusic > 18)
                    {
                        arrayList.add(Mp3Files(R.raw.youaremysunshine))
                        arrayList.add(Mp3Files(R.raw.justrelax))
                        arrayList.add(Mp3Files(R.raw.eveningbirdssinging))
                        arrayList.add(Mp3Files(R.raw.rivernaturefield))
                        arrayList.add(Mp3Files(R.raw.voiceofdesertmedievalhistory))
                        arrayList.add(Mp3Files(R.raw.inthecave))
                    }
                }
            }
        }
       if(BPSmusic<=120)
        {
            if(BPDmusic<=80)
            {
                 if(HRmusic < 80 )
                {
                     if(BRmusic<=18)
                     {
                         arrayList.add(Mp3Files(R.raw.wewillrockyou))
                         arrayList.add(Mp3Files(R.raw.threelittlebirds))
                         arrayList.add(Mp3Files(R.raw.thebeatofnature))
                         arrayList.add(Mp3Files(R.raw.feelfree))
                         arrayList.add(Mp3Files(R.raw.awake))
                         arrayList.add(Mp3Files(R.raw.inspirational))
                         arrayList.add(Mp3Files(R.raw.alphaspacemeditation))
                    }
                }
            }
        }
        init()
        listener()
        generate_random_method()

//        var heartrate = 10
//        if(heartrate==10){
//            //first music
//            Toast.makeText(this, "runnin first if block", Toast.LENGTH_SHORT).show()
//        }else if (heartrate==20){
//
//        }


        setBackgroundWithDelay(dec_anixety, "green", generate_random)

       seekbar?.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
           override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
               if(fromUser){
                   music?.seekTo(progress)
               }
           }

           override fun onStartTrackingTouch(seekBar: SeekBar?) {

           }

           override fun onStopTrackingTouch(seekBar: SeekBar?) {

           }

       })

        continue_music_preference?.isEnabled = false
        Handler().postDelayed({
            continue_music_preference?.isEnabled = true
        }, generate_random)
    }
    private fun init() {


        seekbar = findViewById(R.id.seek_bar)
        ic_play_circle = findViewById(R.id.ic_play_circle)
        ic_arrow_back = findViewById(R.id.ic_arrow_back)
        ic_arrow_forward = findViewById(R.id.ic_arrow_forward)
        continue_music_preference = findViewById(R.id.continue_music_preference)

        dec_anixety = findViewById(R.id.dec_anixety)
        //time duration of music
        txt_start_time = findViewById(R.id.txt_start_time)
        txt_end_time = findViewById(R.id.txt_end_time)
        txt_music_name = findViewById(R.id.txt_music_name)
    }

    private fun listener() {
        ic_play_circle?.setOnClickListener(this)
        ic_arrow_back?.setOnClickListener(this)
        ic_arrow_forward?.setOnClickListener(this)
        continue_music_preference?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ic_play_circle -> {
                if (isplay) {
                    if(pausedLength != 0)
                    {
                        music?.seekTo(pausedLength)
                        music?.start()
                        seekbarupdater()
                        ic_play_circle?.setImageResource(R.drawable.ic_pause)
                    }
                    else {
                        Log.i("Position","Position is : $position")
                        Log.i("Position","Position is : ${arrayList.size}")
                        music = MediaPlayer.create(this, arrayList.get(position).music)
                        music!!.start()
                        var durationInMillis: Long = music!!.duration.toLong()
                        formatDuration(durationInMillis)
                        seekbarupdater()
                        ic_play_circle?.setImageResource(R.drawable.ic_pause)
                    }
                } else {
                    ic_play_circle?.setImageResource(R.drawable.ic_play_circle)
                    music?.pause()
                    pausedLength = music?.currentPosition!!
                }
                isplay = !isplay
            }
            R.id.ic_arrow_back -> {
                if (position!! > 0 && position!! < 3) {
                    position = position!! - 1
                    music?.stop()
                    music = MediaPlayer.create(this, arrayList.get(position!!).music)
                    var durationInMillis: Long = music!!.duration.toLong()
                    formatDuration(durationInMillis)
                    music!!.start()
                    seekbarupdater()
                    ic_play_circle?.setImageResource(R.drawable.ic_pause)
                    isplay = false
                } else {
                    music?.stop()
                    music = MediaPlayer.create(this, arrayList.get(position!!).music)
                    music!!.start()
                    var durationInMillis: Long = music!!.duration.toLong()
                    formatDuration(durationInMillis)
                    seekbarupdater()
                    ic_play_circle?.setImageResource(R.drawable.ic_pause)
                    isplay = false
                }
            }
            R.id.ic_arrow_forward -> {
                position = position.plus(1)
                music?.stop()
                if (position!! < arrayList.size) {
                    music = MediaPlayer.create(this, arrayList.get(position!!).music)
                    music!!.start()
                    var durationInMillis: Long = music!!.duration.toLong()
                    formatDuration(durationInMillis)
                    seekbarupdater()
                    ic_play_circle?.setImageResource(R.drawable.ic_pause)
                    isplay = false

                } else {
                    position = 0
                    music?.stop()
                    music = MediaPlayer.create(this, arrayList.get(position!!).music)
                    music!!.start()
                    var durationInMillis: Long = music!!.duration.toLong()
                    formatDuration(durationInMillis)
                    seekbarupdater()
                    ic_play_circle?.setImageResource(R.drawable.ic_pause)
                    isplay = false
                }
            }
            R.id.continue_music_preference->{
                val intent = Intent(this, playlistActivity:: class.java)
                startActivity(intent)
                music?.stop()
            }
        }
    }
    private val updater =
        Runnable {
            seekbarupdater()
            val currentdur:Long = music!!.currentPosition.toLong()
            txt_start_time.text = milliseconds(currentdur)
        }
    private fun seekbarupdater(){
        if (music!!.isPlaying){
            val currentpos:Int = music!!.currentPosition/1000
            seekbar?.progress = currentpos
            handler.postDelayed(updater,1000)

        }
    }
    private fun generate_random_method()
    {
        generate_random = (10000..20000).random().toLong()
    }
    private fun setBackgroundWithDelay(layout: RelativeLayout?, colorName: String, delayMillis: Long) {
        val color = Color.parseColor(colorName)
        val handler = Handler()
        val runnable = Runnable {
            layout?.setBackgroundColor(color)
        }
        handler.postDelayed(
            runnable, delayMillis)
    }
    //time duration for music
    private fun milliseconds(milliseconds: Long): String {
        var timeString = ""
        var secondString = ""
        var hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000 // divide by 1000 to get seconds
        if (hours > 0) {
            timeString = "$hours:"
        }
        if (seconds < 10) {
            secondString = "0$seconds"
        } else {
            secondString = "$seconds"
        }
        timeString = timeString + minutes + ":" + secondString
        return timeString
    }
    private fun formatDuration(duration: Long){
        val min = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val sec = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) - min*TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        var time =  String.format("%02d:%02d", min, sec)
        txt_end_time.setText(time)
    }
}
