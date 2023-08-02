package com.example.collabed

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.collabed.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var username: String
    lateinit var mail: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.butsignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        database =
            FirebaseDatabase.getInstance("https://collabed-5df4c-default-rtdb.firebaseio.com/")
                .getReference("User Profile")
        val displayName =intent.getStringExtra("name")
        Log.d("STATUSNAME:","$displayName")
        val displayMail =intent.getStringExtra("mail")
        Log.d("NAME:","$displayName")
        binding.butlogin.setOnClickListener {
            val email = binding.etMail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
//                Log.d("MAIL:","$ema")
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        username = ""
                        mail = ""
                        database.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                Toast.makeText(this@LoginActivity,"$snapshot",Toast.LENGTH_LONG).show()

                                val usersQuery = database.orderByChild("email").equalTo("$email")
                                usersQuery.addListenerForSingleValueEvent(object : ValueEventListener {

                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        Log.d("RAJ:","$dataSnapshot")
                                        if (dataSnapshot.exists()) {
                                            // There should only be one user with the specified email, so we can retrieve the first result
                                            val userSnapshot = dataSnapshot.children.first()

                                            val username = userSnapshot.child("username").getValue(String::class.java)
                                            // Do something with the username
                                            Log.d("Username for"," $email: $username")
                                            if (snapshot.hasChild("$username/role")){
                                                Log.d("STATUS:","create")
                                                val usersQuerycreate = database.orderByChild("role").equalTo("CREATOR")
                                                val usersQueryedit = database.orderByChild("role").equalTo("EDITOR")
                                                usersQuerycreate.addValueEventListener(object: ValueEventListener {
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        if(snapshot.exists()) {
                                                            Log.d("LOGIN:","successcrea")
                                                            val intent = Intent(this@LoginActivity, CreatorActivity::class.java)
                                                            intent.putExtra("name", username)
                                                            intent.putExtra("mail", email)
                                                            startActivity(intent)
                                                        }

                                                    }


                                                    override fun onCancelled(error: DatabaseError) {
                                                        TODO("Not yet implemented")
                                                    }
                                                })
                                                usersQueryedit.addValueEventListener(object: ValueEventListener {
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        if(snapshot.exists()) {
                                                            Log.d("STATUS:","edit")
                                                            Log.d("LOGIN:","$email")
                                                            val intent = Intent(this@LoginActivity, EditorActivity::class.java)
                                                            intent.putExtra("name", username)
                                                            intent.putExtra("mail", email)
                                                            startActivity(intent)
                                                        }

                                                    }


                                                    override fun onCancelled(error: DatabaseError) {
                                                        TODO("Not yet implemented")
                                                    }
                                                })


                                            }
                                            else {
                                                Log.d("STATUS:","role")
                                                Log.d("STATUSm:","$username")
                                                Log.d("STATUSmail:", email)

                                                if (username != null) {
                                                    role1(username,email)
                                                }
                                            }
                                        } else {
                                            println("No user found with email $email")
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        // Handle errors here
                                        println("Error retrieving username: ${error.message}")
                                    }
                                })





                                Log.d("SNAP:","$usersQuery")

                            }

                            override fun onCancelled(error: DatabaseError) {
                                // handle error
                            }
                        })
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }

                }
            } else {
                Toast.makeText(this, "Enter Credentials Properly", Toast.LENGTH_SHORT).show()
            }
        }


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        findViewById<Button>(R.id.butgooglelogin).setOnClickListener {
            signInGoogle()
        }
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {

            if (it.isSuccessful) {
                username = account.displayName.toString()
                mail = account.email.toString()
                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild("$username/role")){

                            val usersQuery = database.orderByChild("role").equalTo("CREATOR")
                            usersQuery.addValueEventListener(object: ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if(snapshot.exists()) {

                                        creator()
                                    }

                                }


                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })


                        } else if(snapshot.hasChild("$username/role")) {

                            val usersQuery = database.orderByChild("role").equalTo("EDITOR")
                            usersQuery.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        Log.d("LOGIN","successedit")
                                        editor()
                                    }

                                }


                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                        }
                            else {
                            role()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // handle error
                    }
                })
                val user = user(account.displayName, account.email)
                account.displayName?.let { it1 ->
//                    database.child(it1).setValue(user).addOnSuccessListener {
//
//
//                        Toast.makeText(
//                            this, "Database updated",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }

//                intent.putExtra("email",account.email)
//                intent.putExtra("name",account.displayName)
//                intent.putExtra("image", account.photoUrl?.path)

                }


            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun home() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("name", username)
        intent.putExtra("mail", mail)
        startActivity(intent)
    }
    private fun creator() {
        val intent = Intent(this, CreatorActivity::class.java)
        intent.putExtra("name", username)
        intent.putExtra("mail", mail)
        startActivity(intent)
    }
    private fun editor() {
        val intent = Intent(this, EditorActivity::class.java)
        intent.putExtra("name", username)
        intent.putExtra("mail", mail)
        startActivity(intent)
    }

    private fun role() {
        val intent = Intent(this, RoleActivity::class.java)
        intent.putExtra("name", username)
        intent.putExtra("mail", mail)
        startActivity(intent)

    }
    private fun role1(username:String,email:String) {
        val intent = Intent(this, RoleActivity::class.java)
        intent.putExtra("name", username)
        intent.putExtra("mail", email)
        startActivity(intent)

    }
    private fun login() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("name", username)
        intent.putExtra("mail", mail)
        startActivity(intent)

    }
}