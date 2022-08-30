package com.tomtruyen.Fitoryx.utils

import android.content.Context
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tomtruyen.Fitoryx.MainActivity
import com.tomtruyen.Fitoryx.R
import java.util.*

object Utils {
    fun getSupportActionBar(activity: FragmentActivity): ActionBar? {
        return (activity as AppCompatActivity).supportActionBar
    }

    fun showErrorDialog(context: Context, message: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.error_title)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }
}