package com.example.collabed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.google.firebase.database.*

class TopicActivity : AppCompatActivity() {
    private lateinit var butsave: Button
    private lateinit var butadd: Button
    private lateinit var butsave1: Button
    private lateinit var butadd1: Button
    private lateinit var MyTopic: EditText
    private lateinit var FutTopic:EditText
    private lateinit var MyTopicadd:EditText
    private lateinit var FutTopicadd:EditText
    private lateinit var MyTopicName: String
    private lateinit var FutTopicName:String
    private lateinit var topic:String
    private lateinit var topiclist:StringBuilder
    private lateinit var database:FirebaseDatabase
    private lateinit var databaseref: DatabaseReference
    private lateinit var databaseref1: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic)
        val email = intent.getStringExtra("email")
        val displayName =intent.getStringExtra("name")
        database = FirebaseDatabase.getInstance("https://collabed-5df4c-default-rtdb.firebaseio.com/")

        databaseref= database.getReference("TOPICS")
        MyTopic = findViewById<EditText?>(R.id.editText)
        FutTopic = findViewById<EditText?>(R.id.editText2)
        val topicRef = displayName?.let { database.getReference("TOPICS").child(it).child("MyTopic") }
        val futtopicRef = displayName?.let { database.getReference("TOPICS").child(it).child("FutTopic") }
        if (topicRef != null) {
            topicRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var topiclist=""
                    for (topicSnapshot in dataSnapshot.children) {

                        val topic = topicSnapshot.getValue(mytopics::class.java)
                        Log.d("TopicValue:", topic?.my_topics ?: "")
                        topiclist += topic?.my_topics?.toString()+'\n'

                    }
                    MyTopic.text=Editable.Factory.getInstance().newEditable(topiclist)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Error:", "onCancelled", databaseError.toException())
                }
            })
        }
        if (futtopicRef != null) {
            futtopicRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var futtopiclist=""
                    for (topicSnapshot in dataSnapshot.children) {

                        val futtopic = topicSnapshot.getValue(futtopics::class.java)
                        Log.d("TopicValue:", futtopic?.fut_topics ?: "")
                        futtopiclist += futtopic?.fut_topics?.toString()+'\n'

                    }
                    FutTopic.text=Editable.Factory.getInstance().newEditable(futtopiclist)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Error:", "onCancelled", databaseError.toException())
                }
            })
        }

        MyTopicName=""
        FutTopicName=""
        MyTopicadd = findViewById<EditText?>(R.id.editTextadd)
        FutTopicadd = findViewById<EditText?>(R.id.editTextadd1)
        butadd = findViewById(R.id.buttonadd)
        butsave = findViewById(R.id.buttonsave)
        butadd1 = findViewById(R.id.buttonadd1)
        butsave1 = findViewById(R.id.buttonsave1)
        butadd.setOnClickListener {
            MyTopicadd.visibility= View.VISIBLE
            butadd.visibility=View.INVISIBLE
            butsave.visibility=View.VISIBLE
            butsave.setOnClickListener{
                butadd.visibility=View.VISIBLE
                butsave.visibility=View.INVISIBLE
                MyTopicadd.visibility= View.INVISIBLE
                MyTopicName =MyTopicadd.text.toString()

                Log.d("Topicname:","$MyTopicName+$FutTopicName")

                // Check if the topic already exists in FutTopic, and delete it if it does
                if (displayName != null) {
                    databaseref.child(displayName).child("FutTopic").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            databaseref.child(displayName).child("MyTopic").addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot != null && dataSnapshot.childrenCount > 0) {
                                        for (topicSnapshot in dataSnapshot.children) {
                                            val topic = topicSnapshot.getValue(mytopics::class.java)
                                            if (topic?.my_topics?.trim()
                                                    ?.toLowerCase() == MyTopicName.trim()
                                                    .toLowerCase()
                                            ) {
                                                topicSnapshot.ref.removeValue()
                                                // Topic already exists in the database
                                                Toast.makeText(
                                                    this@TopicActivity,
                                                    "Topic already exists",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                return
                                            }
                                        }
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Failed to read value
                                    Toast.makeText(
                                        this@TopicActivity,
                                        "Failed to read value",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                            if (dataSnapshot != null && dataSnapshot.childrenCount > 0) {
                                for (topicSnapshot in dataSnapshot.children) {
                                    val topic = topicSnapshot.getValue(futtopics::class.java)
                                    if (topic?.fut_topics?.trim()?.toLowerCase() == MyTopicName.trim().toLowerCase()) {
                                        // Topic exists in FutTopic, delete it
                                        topicSnapshot.ref.removeValue()
                                    }
                                }
                            }

                            // Add the topic to MyTopic
                            val topic = mytopics(my_topics=MyTopicName)
                            databaseref.child(displayName).child("MyTopic").push().setValue(topic)
                                .addOnSuccessListener{
                                    // Topic added successfully
                                    Toast.makeText(this@TopicActivity, "Topic added", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    // Failed to add the topic
                                    Toast.makeText(this@TopicActivity, "Failed to add topic", Toast.LENGTH_SHORT).show()
                                }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Failed to read value
                            Toast.makeText(this@TopicActivity, "Failed to read value", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }



        }
        butadd1.setOnClickListener{
            FutTopicadd.visibility= View.VISIBLE
            butadd1.visibility=View.INVISIBLE
            butsave1.visibility=View.VISIBLE
            butsave1.setOnClickListener{
                butadd1.visibility=View.VISIBLE
                butsave1.visibility=View.INVISIBLE
                FutTopicadd.visibility= View.INVISIBLE

                FutTopicName = FutTopicadd.text.toString()
                Log.d("FutTopicname:","$MyTopicName+$FutTopicName")
                val futtopic = futtopics(fut_topics=FutTopicName)
                if (displayName != null) {
                    databaseref.child(displayName).child("FutTopic").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot != null && dataSnapshot.childrenCount > 0) {
                                for (futtopicSnapshot in dataSnapshot.children) {
                                    val futtopic = futtopicSnapshot.getValue(futtopics::class.java)
                                    if (futtopic?.fut_topics?.trim()?.toLowerCase() == FutTopicName.trim().toLowerCase()) {
                                        // Topic already exists in the database
                                        Toast.makeText(this@TopicActivity, "Topic already exists", Toast.LENGTH_SHORT).show()
                                        return
                                    }
                                }
                            }
                            // Add the topic to the database if it does not exist
                            val futtopic = futtopics(fut_topics=FutTopicName)
                            databaseref.child(displayName).child("FutTopic").push().setValue(futtopic)
                                .addOnSuccessListener{
                                    // Topic added successfully
                                    Toast.makeText(this@TopicActivity, "Topic added", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    // Failed to add the topic
                                    Toast.makeText(this@TopicActivity, "Failed to add topic", Toast.LENGTH_SHORT).show()
                                }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Failed to read value
                            Toast.makeText(this@TopicActivity, "Failed to read value", Toast.LENGTH_SHORT).show()
                        }
                    })
//                    databaseref.child(displayName).child("MyTopic").push().setValue(topic).addOnSuccessListener {
//
//
//                        Toast.makeText(this, "Database updated",
//                            Toast.LENGTH_LONG).show()
//                    }
                }
            }



        }

    }
}