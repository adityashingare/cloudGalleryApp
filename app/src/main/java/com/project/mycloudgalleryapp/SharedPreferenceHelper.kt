package com.project.mycloudgalleryapp

import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceHelper {
    private const val PREF_NAME = "MyCloudGalleryPreferences"
    private const val KEY_LAST_IMAGE_POSITION = "last_image_position"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveLastImagePosition(context: Context, position: Int) {
        val editor = getPreferences(context).edit()
        editor.putInt(KEY_LAST_IMAGE_POSITION, position)
        editor.apply()
    }

    fun getLastImagePosition(context: Context): Int {
        return getPreferences(context).getInt(KEY_LAST_IMAGE_POSITION, 0)
    }

    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }
}
