package com.example.collabed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class CreatorListActivity : AppCompatActivity()  {
    private lateinit var recyclerView: RecyclerView
    private lateinit var creatorsArrayList: ArrayList<creators>
    private lateinit var filteredList: ArrayList<creators>
    private lateinit var database:FirebaseDatabase
    private lateinit var databaseref:DatabaseReference
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator_list)

        recyclerView = findViewById(R.id.creator_card)
        recyclerView.layoutManager = LinearLayoutManager(this)

        creatorsArrayList = arrayListOf()
        filteredList = arrayListOf()
        database = FirebaseDatabase.getInstance()
        databaseref = database.getReference("User Profile")
        val usersQuery = databaseref.orderByChild("role").equalTo("CREATOR")
        usersQuery.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapShot in snapshot.children) {
                        val creator = dataSnapShot.getValue(creators::class.java)
                        if (!creatorsArrayList.contains(creator)) {
                            creatorsArrayList.add(creator!!)
                        }
                    }
                    // Save a copy of the full list for filtering
                    filteredList = ArrayList(creatorsArrayList)
                    recyclerView.adapter = CreatorAdapter(filteredList, this@CreatorListActivity)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CreatorListActivity, "Error", Toast.LENGTH_LONG).show()
            }
        })

        // Get reference to SearchView and set up listener
        searchView = findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter the list based on user input
                filteredList.clear()
                if (newText.isNullOrBlank()) {
                    filteredList.addAll(creatorsArrayList)
                } else {
                    val query = newText.toLowerCase()
                    creatorsArrayList.forEach { creator ->
                        if (creator.username?.toLowerCase()?.contains(query) == true) {
                            filteredList.add(creator)
                        }
                    }
                }
                recyclerView.adapter?.notifyDataSetChanged()
                return true
            }
        })
    }
}