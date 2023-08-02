package com.example.collabed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.collabed.databinding.ActivityCreatorBinding
import com.example.collabed.databinding.ActivityEditorBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class CreatorActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var drawer_header: NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var butcreate:Button
    lateinit var butupload:Button
    lateinit var buteditorlist:Button
    private lateinit var binding: ActivityCreatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator)

        val email = intent.getStringExtra("mail")
        val displayName =intent.getStringExtra("name")
        val displayImage =intent.getStringExtra("image")
        binding= ActivityCreatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth=FirebaseAuth.getInstance()
        drawer_header=findViewById(R.id.navigationView)
        butupload=findViewById(R.id.butupload)
        butupload.setOnClickListener{
            val intent = Intent(this,UploadActivity::class.java)
            intent.putExtra("email",email)
            intent.putExtra("name",displayName)
            startActivity(intent)
        }
        butcreate=findViewById(R.id.butedit)
        butcreate.setOnClickListener{
            val intent = Intent(this,TopicActivity::class.java)
            intent.putExtra("email",email)
            intent.putExtra("name",displayName)
            startActivity(intent)
        }
        buteditorlist=findViewById(R.id.butcreator)
        buteditorlist.setOnClickListener{
            val intent = Intent(this,EditorListActivity::class.java)
            intent.putExtra("email",email)
            intent.putExtra("name",displayName)
            startActivity(intent)
        }

        drawer_header.getHeaderView(0).findViewById<TextView>(R.id.txtUsername).text = email + "\n" + displayName+"\n"
        drawerLayout = findViewById(R.id.drawerLayout)
//    coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        findViewById<Button>(R.id.butgooglelogout).setOnClickListener{
            firebaseAuth.signOut()
            finishAffinity()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)

        }

        setUpToolbar()
        val actionBarDrawerToggle= ActionBarDrawerToggle(
            this@CreatorActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        drawer_header.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.upload->{
                    val intent = Intent(this,UploadActivity::class.java)

                    intent.putExtra("email",email)
                    intent.putExtra("name",displayName)
                    startActivity(intent)            }
                R.id.topics->{
                    val intent = Intent(this,TopicActivity::class.java)

                    intent.putExtra("email",email)
                    intent.putExtra("name",displayName)
                    startActivity(intent)            }



            }
            return@setNavigationItemSelectedListener true
        }
    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
}