package com.project.mycloudgalleryapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.project.mycloudgalleryapp.databinding.ActivityFullscreenImageBinding

class FullscreenImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenImageBinding
    private lateinit var images: ArrayList<ImageData>
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var btnShare: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullscreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        images = intent.getParcelableArrayListExtra<ImageData>("images") ?: ArrayList()
        val currentPosition = intent.getIntExtra("position", 0)

        adapter = ViewPagerAdapter()
        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = currentPosition

        btnShare = findViewById(R.id.btnShare)
        btnDelete = findViewById(R.id.btnDelete)

        btnShare.setOnClickListener {
            shareImage(currentPosition)
        }

        btnDelete.setOnClickListener {
            deleteImage(currentPosition)
        }
    }

    private fun shareImage(position: Int) {
        val imageData = images[position]
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Sharing image: ${imageData.imageUrl}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share image via"))
    }

    private fun deleteImage(position: Int) {
        images.removeAt(position)
        adapter.notifyDataSetChanged()

        // Adjust current position if it exceeds the new list size
        if (position >= images.size && images.isNotEmpty()) {
            binding.viewPager.currentItem = images.size - 1
        }
    }

    inner class ViewPagerAdapter : androidx.viewpager.widget.PagerAdapter() {

        override fun instantiateItem(container: View, position: Int): Any {
            val imageView = ImageView(container.context)
            imageView.layoutParams = ViewPager.LayoutParams()
            imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE

            Glide.with(container.context)
                .load(images[position].imageUrl)
                .into(imageView)

            (container as ViewPager).addView(imageView, 0)
            return imageView
        }

        override fun getCount(): Int {
            return images.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun destroyItem(container: View, position: Int, `object`: Any) {
            (container as ViewPager).removeView(`object` as View)
        }
    }
}
