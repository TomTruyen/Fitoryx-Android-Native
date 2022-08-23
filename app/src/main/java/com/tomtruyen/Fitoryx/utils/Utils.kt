package com.tomtruyen.Fitoryx.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tomtruyen.Fitoryx.R

object Utils {
    fun showErrorDialog(context: Context, message: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.error_title)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}