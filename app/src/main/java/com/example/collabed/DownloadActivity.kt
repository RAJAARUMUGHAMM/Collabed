package com.example.collabed

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class DownloadActivity : AppCompatActivity() {
    private lateinit var dload: Button
    private lateinit var butup: Button
    private lateinit var butdown: Button
    private lateinit var butpre: Button
    private lateinit var filePathTextView: TextView
    private lateinit var list: StringBuilder
    private lateinit var requestfile: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val Auth = FirebaseAuth.getInstance()
        var click = 0
        setContentView(R.layout.activity_download)
        dload = findViewById<Button>(R.id.downloadbutton)
        filePathTextView = findViewById(R.id.filePathTextView)
        val storage = FirebaseStorage.getInstance()

        requestfile = findViewById(R.id.filename)
        val email = intent.getStringExtra("mail")
        val name = intent.getStringExtra("name")
        val storageRef = storage.getReference("$name")
        var list = ""
        val customDir = File("/storage/emulated/0/Download/firedownload")
        if (!customDir.exists())
            customDir.mkdirs()
        click = click + 1
// Get a list of videos in Firebase Storage
        storageRef.listAll().addOnSuccessListener { listResult ->
            for (item in listResult.items) {
                Log.d("Firebase Storage" + click, "Video: ${item.name}",)
                list += item.name + "\n"
                if (click == 1)
                    filePathTextView.text = list
            }

            butpre = findViewById(R.id.previewbutton1)
            butpre.setOnClickListener {
                val file = requestfile.text.toString().trim()
                val videoRef = storageRef.child(file)

// Get a download URL for the video file
                videoRef.downloadUrl.addOnSuccessListener { uri ->
                    // Load the video using a VideoView widget
                    val videoView = findViewById<VideoView>(R.id.video_view)
                    val mediaController = MediaController(this)
                    mediaController.setAnchorView(videoView)
                    videoView.setMediaController(mediaController)
                    videoView.setVideoURI(uri)
                    videoView.requestFocus()
                    videoView.start()
                }.addOnFailureListener { exception ->
                    // Handle any errors that occur while downloading the video
                    Log.e(TAG, "Error downloading video", exception)
                }
            }
            butdown = findViewById(R.id.downloadbutton1)
            butdown.setOnClickListener {
                val file = requestfile.text.toString().trim()
//            val videoRef = storageRef.child('"'+file+'"')
                val videoRef = storageRef.child(file)
                val localFile = File(customDir, "$file.mp4")
                videoRef.getFile(localFile).addOnSuccessListener {
                    Log.d("Firebase Storage", "Video downloaded successfully")
                    Toast.makeText(this@DownloadActivity, "Download Completed", Toast.LENGTH_SHORT)
                        .show()
                }.addOnFailureListener { exception ->
                    Log.e("Firebase Storage", "Error downloading video: $exception")
                }
            }

            butup = findViewById(R.id.uploadFileButton)
            butup.setOnClickListener {
                val intent = Intent(this, UploadActivity::class.java)
                startActivity(intent)
            }
        }
    }
}