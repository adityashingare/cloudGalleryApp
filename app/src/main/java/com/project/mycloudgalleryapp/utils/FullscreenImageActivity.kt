package com.project.mycloudgalleryapp.utils

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.project.mycloudgalleryapp.R
import com.project.mycloudgalleryapp.adapters.FullscreenImageAdapter
import com.project.mycloudgalleryapp.data.ImageData

class FullscreenImageActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var imageAdapter: FullscreenImageAdapter
    private lateinit var images: MutableList<ImageData>
    private var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_image)

        images = intent.getParcelableArrayListExtra("images") ?: mutableListOf()
        currentPosition = intent.getIntExtra("position", 0)

        viewPager = findViewById(R.id.viewPager)
        imageAdapter = FullscreenImageAdapter(this, images)
        viewPager.adapter = imageAdapter
        viewPager.currentItem = currentPosition

        findViewById<Button>(R.id.btnDelete).setOnClickListener { deleteImage() }
        findViewById<Button>(R.id.btnShare).setOnClickListener { shareImage() }
    }

    private fun deleteImage() {
        val currentPosition = viewPager.currentItem
        val imageToDelete = images[currentPosition]

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Delete Image")
            .setMessage("Are you sure you want to delete this image?")
            .setPositiveButton("Yes") { dialog, _ ->
                val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageToDelete.imageUrl)
                storageReference.delete().addOnSuccessListener {
                    val databaseReference = FirebaseDatabase.getInstance().getReference("uploads").child(imageToDelete.id)
                    databaseReference.removeValue().addOnCompleteListener {
                        if (it.isSuccessful) {
                            images.removeAt(currentPosition)
                            imageAdapter.notifyDataSetChanged()
                            Toast.makeText(this, "Image deleted successfully", Toast.LENGTH_SHORT).show()

                            if (images.isEmpty()) {
                                finish() // Close the activity if no images are left
                            } else {
                                // Set the current item to the previous item if the last item was deleted
                                viewPager.currentItem = if (currentPosition >= images.size) {
                                    images.size - 1
                                } else {
                                    currentPosition
                                }
                            }
                        } else {
                            Toast.makeText(this, "Failed to delete image from database", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to delete image from storage", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("No", null)
            .create()

        alertDialog.show()
    }



    private fun shareImage() {
        val imageToShare = images[viewPager.currentItem]
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, Uri.parse(imageToShare.imageUrl))
            type = "image/*"
        }
        startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }
}
