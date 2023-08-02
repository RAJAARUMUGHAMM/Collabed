package com.example.collabed

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EditorAdapter(val editorsList: ArrayList<editors>,private val context: Context):RecyclerView.Adapter<EditorAdapter.EditorViewHolder>() {
    class EditorViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val cName:TextView=itemView.findViewById(R.id.creator_name)
        val cMail:TextView=itemView.findViewById(R.id.creator_mail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditorViewHolder {
        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.creatorlist_item,parent,false)
        return EditorViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return editorsList.size
    }

    override fun onBindViewHolder(holder: EditorViewHolder, position: Int) {
        holder.cName.text=editorsList[position].username
        holder.cMail.text=editorsList[position].email

        val intent = Intent(context, DownloadActivity::class.java)
        intent.putExtra("name",editorsList[position].username)
        intent.putExtra("mail",editorsList[position].email)
        holder.itemView.setOnClickListener {

            context.startActivity(intent)
        }

    }
}