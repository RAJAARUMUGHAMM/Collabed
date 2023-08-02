package com.example.collabed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RoleActivity : AppCompatActivity() {
    lateinit var buteditor: Button
    lateinit var butcreator:Button
    lateinit var role:String

//    lateinit var
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        role=""
        super.onCreate(savedInstanceState)
//var role=""
        val displayName =intent.getStringExtra("name")
        val displayMail =intent.getStringExtra("mail")
        setContentView(R.layout.activity_role)
        database = FirebaseDatabase.getInstance("https://collabed-5df4c-default-rtdb.firebaseio.com/")
            .getReference("User Profile/$displayName")
        buteditor=findViewById(R.id.buteditor)
        buteditor.setOnClickListener{
            role="EDITOR"
            Log.d("STATUSrole:","$displayName+$displayMail")
            val rolename = user(username = displayName,email=displayMail,role =role)

            if (displayName != null) {
                database.setValue(rolename).addOnSuccessListener {
                    Toast.makeText(this, "Database updated",
                        Toast.LENGTH_LONG).show()
                    val intent = Intent(this, EditorActivity::class.java)
                    intent.putExtra("name", displayName)
                    intent.putExtra("mail", displayMail)
                    startActivity(intent)

                }
            }
        }
        butcreator=findViewById(R.id.butcreator)
        butcreator.setOnClickListener{
            role="CREATOR"
            val rolename = user(username = displayName,email=displayMail,role =role)
            if (displayName != null) {
                database.setValue(rolename).addOnSuccessListener {
                    Toast.makeText(this, "Database updated",
                        Toast.LENGTH_LONG).show()
                    val intent = Intent(this,CreatorActivity::class.java)
                    intent.putExtra("name", displayName)
                    intent.putExtra("mail", displayMail)
                    startActivity(intent)

                }
            }
        }


        Log.d("NAME:","$displayName")

    }
}