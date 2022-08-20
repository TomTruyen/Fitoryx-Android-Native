package com.tomtruyen.Fitoryx.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.google.android.material.textfield.TextInputLayout
import com.tomtruyen.Fitoryx.MainActivity
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.service.AuthService

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.register_text_view).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.setCustomAnimations(
                    R.anim.enter_right_to_left,
                    R.anim.exit_right_to_left,
                    R.anim.enter_left_to_right,
                    R.anim.exit_left_to_right
                )
                ?.replace(R.id.fragment_container_view, RegisterFragment())
                ?.commit()
        }

        view.findViewById<MaterialButton>(R.id.login_button).setOnClickListener {
            handleLogin(it as MaterialButton, view)
        }
    }

    private fun handleLogin(button: MaterialButton, view: View) {
        val emailLayout = view.findViewById<TextInputLayout>(R.id.email_text_input_layout)
        val passwordLayout = view.findViewById<TextInputLayout>(R.id.password_text_input_layout)

        emailLayout.error = null
        passwordLayout.error = null

        var email = ""
        var password = ""

        emailLayout.editText?.let {
            email = it.text.toString()

            if(email.isEmpty()) {
                emailLayout.error = resources.getString(R.string.error_email_required)
                return@let
            }

            if(Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
                emailLayout.error = resources.getString(R.string.error_email_invalid)
                return@let
            }
        }

        passwordLayout.editText?.let {
            password = it.text.toString()

            if(password.isEmpty()) {
                passwordLayout.error = resources.getString(R.string.error_password_required)
                return@let
            }
        }

        // TODO
        // Show progress bar on Sign In
        // Replace the Google error messages with my own
        // "the password is invalid or the user does not have a password" should be "the password is invalid"

        if(emailLayout.error == null && passwordLayout.error == null) {
            AuthService.signInWithEmailAndPassword(
                email = email,
                password = password,
                onSuccess = {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    activity?.finish()
                },
                onFailure = { error ->
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.error_title))
                        .setMessage(error)
                        .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            )
        }
    }
}