package com.project.mycloudgalleryapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UploadActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var buttonChooseImage: Button
    private lateinit var buttonUpload: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var storageReference: StorageReference
    private lateinit var databaseReference: DatabaseReference

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        imageView = findViewById(R.id.imageView)
        buttonChooseImage = findViewById(R.id.button_choose_image)
        buttonUpload = findViewById(R.id.button_upload)
        progressBar = findViewById(R.id.progress_bar)

        storageReference = FirebaseStorage.getInstance().reference
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads")

        buttonChooseImage.setOnClickListener { openFileChooser() }
        buttonUpload.setOnClickListener { uploadFile() }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun uploadFile() {
        if (imageUri != null) {
            val fileReference = storageReference.child(System.currentTimeMillis().toString() + "." + getFileExtension(imageUri!!))

            progressBar.visibility = ProgressBar.VISIBLE

            fileReference.putFile(imageUri!!)
                .addOnSuccessListener {
                    fileReference.downloadUrl.addOnSuccessListener { uri ->
                        val uploadId = databaseReference.push().key ?: return@addOnSuccessListener
                        val upload = ImageData(uploadId, uri.toString())
                        databaseReference.child(uploadId).setValue(upload)
                    }

                    progressBar.visibility = ProgressBar.INVISIBLE
                    finish()
                }
                .addOnFailureListener {
                    progressBar.visibility = ProgressBar.INVISIBLE
                }
        }
    }

    private fun getFileExtension(uri: Uri): String {
        return contentResolver.getType(uri)?.substringAfterLast("/") ?: "jpg"
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
