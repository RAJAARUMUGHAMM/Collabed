package com.example.collabed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class EditorListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var editorsArrayList: ArrayList<editors>
    private lateinit var filteredList: ArrayList<editors>
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseref: DatabaseReference
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor_list)

        recyclerView=findViewById(R.id.creator_card)
        recyclerView.layoutManager = LinearLayoutManager(this)

        editorsArrayList= arrayListOf()
        filteredList= arrayListOf()
        database= FirebaseDatabase.getInstance()
        databaseref= database.getReference("User Profile")
        val usersQuery = databaseref.orderByChild("role").equalTo("EDITOR")
        usersQuery.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(dataSnapShot in snapshot.children){
                        val editor=dataSnapShot.getValue(editors::class.java)
                        if(!editorsArrayList.contains(editor)){
                            editorsArrayList.add(editor!!)
                        }
                    }
                    // Save a copy of the full list for filtering
                    filteredList = ArrayList(editorsArrayList)
                    recyclerView.adapter=EditorAdapter(filteredList,this@EditorListActivity)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditorListActivity,"Error", Toast.LENGTH_LONG).show()
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
                    filteredList.addAll(editorsArrayList)
                } else {
                    val query = newText.toLowerCase()
                    editorsArrayList.forEach { editor ->
                        if (editor.username?.toLowerCase()?.contains(query) == true) {
                            filteredList.add(editor)
                        }
                    }
                }
                recyclerView.adapter?.notifyDataSetChanged()
                return true
            }
        })
    }
}