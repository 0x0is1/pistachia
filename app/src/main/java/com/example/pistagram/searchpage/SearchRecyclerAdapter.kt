package com.example.pistagram.searchpage

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pistagram.R
import com.example.pistagram.profileviewerpage.ProfileViewerActivity
import com.example.pistagram.utils.CredsEditor
import com.example.pistagram.utils.ImageUri2B64

class SearchRecyclerAdapter(private val searchList: List<SearchItem>) :
    RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder>() {
    private val imageUri2B64 = ImageUri2B64()
    private val credsEditor = CredsEditor()
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.findViewById(R.id.userAvatar)
        val name: TextView = view.findViewById(R.id.name)
        val username: TextView = view.findViewById(R.id.username)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchItem = searchList[position]
        val selfName = credsEditor.readFileFromInternalStorage(holder.itemView.context, "creds.txt").toString()

        if (selfName != searchItem.username) {
            imageGlider(searchItem.avatar, holder.itemView.context, holder.avatar)
            holder.name.text = searchItem.name
            holder.username.text = searchItem.username

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, ProfileViewerActivity::class.java)
                intent.putExtra("username", searchItem.username)
                holder.itemView.context.startActivity(intent)
            }
        } else {
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        }
    }


    override fun getItemCount(): Int = searchList.size

    private fun imageGlider(imageUrl:String?, context: Context, imageViewContainer: ImageView){
        if (imageUrl != null) {
            if(imageUrl.contains("https://")){
                Glide.with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_home_profile)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageViewContainer)
            }
            else{
                val decodedImage = imageUri2B64.convertBase64ToBitmap(imageUrl)
                Glide.with(context)
                    .asBitmap()
                    .load(decodedImage)
                    .centerCrop()
                    .placeholder(R.drawable.ic_home_profile)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageViewContainer)
            }
        }
    }

}
