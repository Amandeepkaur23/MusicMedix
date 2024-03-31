package com.example.musictherapy

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class PlaylistActivity : AppCompatActivity() {

//    private lateinit var toggle: ActionBarDrawerToggle
//    private lateinit var draw_layout: DrawerLayout
      private lateinit var txt_total_music: TextView
      private lateinit var recyclerView: RecyclerView
      private lateinit var musicAdapter: MusicAdapter

      companion object{
          lateinit var MusicListPA : ArrayList<Song>
      }
      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        //for nav drawer
//        toggle = ActionBarDrawerToggle(this, draw_layout, R.string.nav_open, R.string.nav_close)
//        draw_layout.addDrawerListener(toggle)
//        toggle.syncState()
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
            if(request())
          init()
          MusicListPA = getAudio()
//          musicList.add("1 song")
//          musicList.add("2 song")
//          musicList.add("3 song")
//          musicList.add("4 song")
//          musicList.add("5 song")
          recyclerView.setHasFixedSize(true)
          recyclerView.setItemViewCacheSize(13)
          recyclerView.layoutManager = LinearLayoutManager(this)
          musicAdapter = MusicAdapter(this, MusicListPA)
          recyclerView.adapter = musicAdapter
          txt_total_music.text = "Total Songs: " +musicAdapter.itemCount
      }
    private fun request(): Boolean
    {
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),13)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 13)
        {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
                MusicListPA = getAudio()
            }
            else
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),13)
        }
    }

    //    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(toggle.onOptionsItemSelected(item))
//            return true
//        return super.onOptionsItemSelected(item)
//    }
    private fun init()
    {
//        draw_layout = findViewById(R.id.draw_layout)
        recyclerView = findViewById(R.id.recyclerView)
        txt_total_music = findViewById(R.id.txt_total_music)
    }
    @SuppressLint("Range")
    private fun getAudio(): ArrayList<Song>{
        val tempList = ArrayList<Song>()
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val projection = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID)

        val cursor = this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null,
            MediaStore.Audio.Media.DATE_ADDED + " DESC", null)



//        val cursor = this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null,
//            MediaStore.Audio.Media.DATE_ADDED + "DESC", null)
        if(cursor != null) {
            if (cursor.moveToFirst())
                do {
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val albumC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val artistC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val albumIdC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val uri = Uri.parse( "content://media/external/audio/albumart")
                    val artUriC = Uri.withAppendedPath(uri,albumIdC).toString()
                    val song = Song(id = idC, title = titleC, album = albumC, artist = artistC, path = pathC, duration = durationC,
                    artUri = artUriC)
                    val file = File(song.path)
                    if(file.exists())
                        tempList.add(song)
                } while (cursor.moveToNext())
            cursor.close()
        }

        return tempList

    }
}