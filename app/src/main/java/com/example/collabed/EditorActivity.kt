package com.example.collabed

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.collabed.databinding.ActivityEditorBinding
import com.example.collabed.databinding.ActivitySignUpBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class EditorActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var drawer_header: NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var butcreate:Button
    lateinit var butcreatorlist:Button
    lateinit var butupload:Button
    private lateinit var binding:ActivityEditorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        val email = intent.getStringExtra("mail")
        val displayName =intent.getStringExtra("name")
        val displayImage =intent.getStringExtra("image")
        binding= ActivityEditorBinding.inflate(layoutInflater)
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
        butcreate=findViewById(R.id.butcreate)
        butcreate.setOnClickListener{

            Log.d("Topic:","$displayName")
            val intent = Intent(this,TopicActivity::class.java)
            intent.putExtra("email",email)
            intent.putExtra("name",displayName)
            startActivity(intent)
        }
        butcreatorlist=findViewById(R.id.butcreator)
        butcreatorlist.setOnClickListener{
            val intent = Intent(this,CreatorListActivity::class.java)
            intent.putExtra("email",email)
            intent.putExtra("name",displayName)
            startActivity(intent)
        }
        drawer_header.getHeaderView(0).findViewById<TextView>(R.id.txtUsername).text = email + "\n" + displayName+"\n"
        if(displayImage!=null){
            drawer_header.getHeaderView(0).findViewById<ImageView>(R.id.imgProfile).setImageURI(Uri.parse(displayImage))
        }

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
        this@EditorActivity,
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