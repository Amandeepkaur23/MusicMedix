package com.example.musictherapy

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.concurrent.TimeUnit

class  UserPreferenceMusicActivity : AppCompatActivity() {

    companion object {
        lateinit var musicListU: ArrayList<Song>
        var musicPosition: Int = 0
        var isplay: Boolean = false //for toggling
    }

    private var ic_play_circle: ImageView? = null   //icon for play
    private var music: MediaPlayer? = null
    private var img_music: ImageView? = null
    private var txt_music_name: TextView? = null
    private var ic_arrow_back: ImageView? = null
    private var ic_arrow_forward: ImageView? = null
    private var ic_shuffle: ImageView? = null
    private var ic_repeat: ImageView? = null
    private var seek_bar: SeekBar? = null
    private var handler: Handler = Handler()
    lateinit var txt_start_time: TextView
    lateinit var txt_end_time: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_preference_music)
        initLayout()
        init()
        setLayout()
        ic_play_circle!!.setOnClickListener {
            if(isplay) {
                pauseMusic()
            }
            else {
                playMusic()
            }
        }
        ic_arrow_back!!.setOnClickListener {
            preNextSong(increment = false)
        }
        ic_arrow_forward!!.setOnClickListener {
            preNextSong(increment = true)
        }
        ic_shuffle!!.setOnClickListener {
            initLayout()
            musicListU.shuffle()
            music!!.start()
            var durationInMillis: Long = music!!.duration.toLong()
            formatDuration(durationInMillis)
            seekbarupdater()
            ic_play_circle!!.setImageResource(R.drawable.ic_pause)
        }
        ic_repeat!!.setOnClickListener {
            createMediaPlayer()
            music!!.start()
            ic_repeat!!.setImageResource(R.drawable.ic_repeat_one)
        }
        seek_bar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    music?.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

    }

    private fun init() {
        ic_play_circle = findViewById(R.id.ic_play_circle)
        img_music = findViewById(R.id.img_music)
        txt_music_name = findViewById(R.id.txt_music_name)
        ic_arrow_back = findViewById(R.id.ic_arrow_back)
        ic_arrow_forward = findViewById(R.id.ic_arrow_forward)
        ic_shuffle = findViewById(R.id.ic_shuffle)
        seek_bar = findViewById(R.id.seek_bar)
        txt_start_time = findViewById(R.id.txt_start_time)
        txt_end_time = findViewById(R.id.txt_end_time)
        ic_repeat = findViewById(R.id.ic_repeat)
    }
    private fun initLayout() {
        UserPreferenceMusicActivity.musicPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                UserPreferenceMusicActivity.musicListU = ArrayList()
                UserPreferenceMusicActivity.musicListU.addAll(playlistActivity.MusicListPA)
                createMediaPlayer()
            }
        }
    }
    private fun createMediaPlayer() {
        try {
            if (music == null) {music = MediaPlayer()
                 }
            music!!.reset()
            music!!.setDataSource(musicListU[musicPosition].path)
            music!!.prepare()
            setLayout()
//            music!!.start()
//            seekbarupdater()
//            var durationInMillis: Long = music!!.duration.toLong()
//            formatDuration(durationInMillis)
//            ic_play_circle!!.setImageResource(R.drawable.ic_pause)
              isplay = true
        } catch (e: Exception) {
            return
        }
    }
    private fun setLayout() {
        Glide.with(this)
            .load(musicListU[musicPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.img_music).centerCrop())
            .into(img_music!!)
        txt_music_name!!.text = musicListU[musicPosition].title
    }
    private fun playMusic()
    {
        ic_play_circle!!.setImageResource(R.drawable.ic_pause)
        isplay = true
        music!!.start()
        var durationInMillis: Long = music!!.duration.toLong()
        formatDuration(durationInMillis)
        seekbarupdater()
    }
    private fun pauseMusic()
    {
        ic_play_circle!!.setImageResource(R.drawable.ic_play_circle)
        isplay = false
        music!!.pause()
    }
    private fun preNextSong(increment: Boolean){
        if(increment)
        {
          setSongPosition(increment = true)
            music!!.stop()
          createMediaPlayer()
            music!!.start()
            var durationInMillis: Long = music!!.duration.toLong()
            formatDuration(durationInMillis)
            ic_play_circle!!.setImageResource(R.drawable.ic_pause)
            seekbarupdater()
        }
        else
        {
           setSongPosition(increment = false)
            music!!.stop()
           createMediaPlayer()
            music!!.start()
            var durationInMillis: Long = music!!.duration.toLong()
            formatDuration(durationInMillis)
            ic_play_circle!!.setImageResource(R.drawable.ic_pause)
            seekbarupdater()
        }
    }
    private fun setSongPosition(increment: Boolean){
        if(increment)
        {
            if(musicListU.size -1 == musicPosition)
                musicPosition = 0
            else ++musicPosition
        }else{
            if(0 == musicPosition)
                musicPosition = musicListU.size-1
            else --musicPosition
        }
    }
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
        val sec = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) - min* TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        var time =  String.format("%02d:%02d", min, sec)
        txt_end_time!!.setText(time)
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
            seek_bar?.progress = currentpos
            handler.postDelayed(updater,1000)
        }
    }
}