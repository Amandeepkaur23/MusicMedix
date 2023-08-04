package com.example.musictherapy

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MusicAdapter(private val context: Context, private val mList: ArrayList<Song>): RecyclerView.Adapter<MusicAdapter.ViewHolder>(){

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){   //hold views for adding it to image, text
        val title: TextView = itemView.findViewById(R.id.txt_music_name)
        val album: TextView = itemView.findViewById(R.id.txt_music_album)
        val image: ImageView = itemView.findViewById(R.id.img_music_list)
        val duration: TextView = itemView.findViewById(R.id.txt_duration)
        val root = itemView.rootView
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.music_list,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val currentItems = mList[position]
//        holder.img_music_list.setImageResource(currentItems.img_music_list)
//        holder.txt_music_name.text = currentItems.txt_music_name
//        holder.txt_music_album.text = currentItems.txt_music_album
//        holder.txt_duartion.text = currentItems.txt_duartion
        holder.title.text = mList[position].title
        holder.album.text = mList[position].album
        holder.duration.text = formatDuration(mList[position].duration)
        Glide.with(context)
            .load(mList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.img_music).centerCrop())
            .into(holder.image)
        holder.root.setOnClickListener{
            val intent = Intent(context, UserPreferenceMusicActivity::class.java)
            intent.putExtra("index", position)
            intent.putExtra("class", "MusicAdapter")
            ContextCompat.startActivity(context, intent, null)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

}
