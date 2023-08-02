package com.example.collabed

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class UploadActivity : AppCompatActivity() {
    private lateinit var selectFileButton: Button
    private lateinit var uploadFileButton: Button
    private lateinit var butdown: Button
    private lateinit var filePathTextView: TextView
    private lateinit var FileName: EditText
    private lateinit var email:String
    private lateinit var name:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        FileName = findViewById(R.id.filename)
        selectFileButton = findViewById(R.id.selectFileButton)
        uploadFileButton = findViewById(R.id.uploadFileButton)
        filePathTextView = findViewById(R.id.filePathTextView)

        selectFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(intent, REQUEST_CODE)
        }
        butdown = findViewById(R.id.downloadbutton)
        butdown.setOnClickListener {
            val intent = Intent(this, DownloadActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val email = intent.getStringExtra("email")
        val name =intent.getStringExtra("name")
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val uri = data?.data
            val path = uri.toString()


            val file = File(uri!!.path)

            filePathTextView.text = FileName.text


            val filename = FileName.text // Or any other filename you want
            val storageRef = FirebaseStorage.getInstance().reference.child("/$name/"+"$filename")
            uploadFileButton.setOnClickListener {
                val uploadTask = storageRef.putFile(uri!!)
                uploadTask.addOnSuccessListener { taskSnapshot ->
                    Toast.makeText(this@UploadActivity,"Upload Completed in $name", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { exception ->
                    // Handle upload failure
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE = 1
    }

}