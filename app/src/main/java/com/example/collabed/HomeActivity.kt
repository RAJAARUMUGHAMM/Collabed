package com.example.collabed

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder


class HomeActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
private lateinit var drawer_header:NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var butedit:Button
    lateinit var butprofile:Button
    lateinit var butcreate:Button
    private lateinit var database:FirebaseDatabase
    private lateinit var databaseref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        firebaseAuth=FirebaseAuth.getInstance()

        val email = intent.getStringExtra("email")
        val displayName =intent.getStringExtra("name")
        val displayImage =intent.getStringExtra("image")
        butedit=findViewById(R.id.butupload)
        butedit.setOnClickListener{
            val intent = Intent(this,UploadActivity::class.java)
            intent.putExtra("email",email)
            intent.putExtra("name",displayName)
            startActivity(intent)
        }
        butcreate=findViewById(R.id.buttopic)
        butcreate.setOnClickListener{
            val intent = Intent(this,TopicActivity::class.java)
            intent.putExtra("email",email)
            intent.putExtra("name",displayName)
            startActivity(intent)
        }

//        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)
//        View vi = inflater.inflate(R.layout.drawer_header,null)
////
//        setContentView(R.layout.drawer_header)
//        findViewById<TextView>(R.id.txtUsername).text = email + "\n" + displayName
        drawer_header=findViewById(R.id.navigationView)
        drawer_header.getHeaderView(0).findViewById<TextView>(R.id.txtUsername).text = email + "\n" + displayName+"\n"
//
//        drawer_header.getHeaderView(0).findViewById<TextView>(R.id.imgProfile). = URLDecoder('"'+displayImage+'"')
//        if(displayImage!=null){
//            drawer_header.getHeaderView(0).findViewById<ImageView>(R.id.imgProfile).setImageURI(Uri.parse(displayImage))
//        }

        findViewById<Button>(R.id.butgooglelogout).setOnClickListener{
            firebaseAuth.signOut()
            finishAffinity()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)

        }


    drawerLayout = findViewById(R.id.drawerLayout)
//    coordinatorLayout = findViewById(R.id.coordinatorLayout)
    toolbar = findViewById(R.id.toolbar)


    setUpToolbar()
    val actionBarDrawerToggle= ActionBarDrawerToggle(
        this@HomeActivity,
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
            R.id.creator_mode->{
                val intent = Intent(this,CreatorActivity::class.java)

                intent.putExtra("email",email)
                intent.putExtra("name",displayName)
                startActivity(intent)
            }
            R.id.editor_mode->{
                val intent = Intent(this,EditorActivity::class.java)

                intent.putExtra("email",email)
                intent.putExtra("name",displayName)
                startActivity(intent)
            }


        }
        return@setNavigationItemSelectedListener true
    }
        database= FirebaseDatabase.getInstance()
        databaseref= database.getReference("User Profile")
        val usersQuery = databaseref.orderByChild("role").equalTo("CREATOR")
        usersQuery.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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