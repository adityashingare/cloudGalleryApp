package com.project.mycloudgalleryapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide

class FullscreenImageAdapter(private val context: Context, private val images: List<ImageData>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_fullscreen_image, container, false)
        val imageView = view.findViewById<ImageView>(R.id.fullscreen_image_view)
        Glide.with(context).load(images[position].imageUrl).into(imageView)
        container.addView(view)
        return view
    }

    override fun getCount(): Int = images.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
