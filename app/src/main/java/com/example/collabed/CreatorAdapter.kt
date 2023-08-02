package com.example.collabed

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView


class CreatorAdapter(
    val creatorsList:ArrayList<creators>, private val context:Context
):RecyclerView.Adapter<CreatorAdapter.CreatorViewHolder>() {



    class CreatorViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val cName:TextView=itemView.findViewById(R.id.creator_name)
        val cMail:TextView=itemView.findViewById(R.id.creator_mail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatorViewHolder {
        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.creatorlist_item,parent,false)
        val context=parent.context
        return CreatorViewHolder(itemView)


    }

    override fun getItemCount(): Int {
        return creatorsList.size
    }

    override fun onBindViewHolder(holder: CreatorViewHolder, position: Int) {
        holder.cName.text=creatorsList[position].username
        holder.cMail.text=creatorsList[position].email
        val intent = Intent(context, DownloadActivity::class.java)
        intent.putExtra("name",creatorsList[position].username)
        intent.putExtra("mail",creatorsList[position].email)
        holder.itemView.setOnClickListener {

            context.startActivity(intent)
        }

    }


}