package com.tomtruyen.Fitoryx.utils

import android.content.res.Resources
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.model.utils.Result

class InputValidator(private val resources: Resources) {
    fun isValidEmail(email: String): Result<String> {
        if(email.isEmpty()) {
            return Result.Error(resources.getString(R.string.error_email_required))
        }

        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
            return Result.Error(resources.getString(R.string.error_email_invalid))
        }

        return Result.Success()
    }

    fun isValidPassword(password: String): Result<String> {
        if(password.isEmpty()) {
            return Result.Error(resources.getString(R.string.error_password_required))
        }

        if(password.length < 6) {
            return Result.Error(resources.getString(R.string.error_password_length))
        }

        return Result.Success()
    }
}