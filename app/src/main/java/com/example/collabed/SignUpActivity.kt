package com.example.collabed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.LOG
import com.example.collabed.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpBinding

    private lateinit var database: DatabaseReference
    private  lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth= FirebaseAuth.getInstance()
        binding.butlogin.setOnClickListener{
            val intent =Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        database = FirebaseDatabase.getInstance("https://collabed-5df4c-default-rtdb.firebaseio.com/")
            .getReference("User Profile")

        binding.butsignup.setOnClickListener{
            val email=binding.etMail.text.toString()
            val password=binding.etPassword.text.toString()
            val cnfpassword=binding.etcnfPassword.text.toString()
            val username=binding.etUsername.text.toString()
            val user = user(username,email)
            database.child(username).setValue(user).addOnSuccessListener {


                Toast.makeText(this, "Database updated",
                    Toast.LENGTH_LONG).show()
            }
            if(email.isNotEmpty() && password.isNotEmpty() && cnfpassword.isNotEmpty()){
                if(password == cnfpassword){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if(it.isSuccessful){

                            val intent =Intent(this,LoginActivity::class.java)
                            intent.putExtra("name", username)
                            Log.d("STATUSNAME:","$username")
                            intent.putExtra("mail", email)

                            startActivity(intent)
                        }else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this,"Password is not matching",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Enter Credentials Properly",Toast.LENGTH_SHORT).show()
            }
    }
}
}