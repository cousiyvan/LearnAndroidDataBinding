package com.example.databinding.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @BindingAdapter("app:popularity")
    @JvmStatic fun popularity(view: TextView, popularity: String) {
        view.text = popularity
    }

    @BindingAdapter("app:progressTint")
    @JvmStatic fun tintPopularitiy(progressBar: ProgressBar, popularity: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.progressTintList = ColorStateList.valueOf(Color.CYAN)
        }
    }

    @BindingAdapter("app:hideIfZero")
    @JvmStatic fun hideIfZero(view: View, number: Int) {
        view.visibility = if (number == 0) View.GONE else View.VISIBLE
    }
}