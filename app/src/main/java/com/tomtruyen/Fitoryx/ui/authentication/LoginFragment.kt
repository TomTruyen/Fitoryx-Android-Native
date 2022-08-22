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
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputLayout
import com.tomtruyen.Fitoryx.MainActivity
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.model.utils.Result
import com.tomtruyen.Fitoryx.service.AuthService
import com.tomtruyen.Fitoryx.utils.InputValidator
import com.tomtruyen.android.material.loadingbutton.LoadingButton
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {
    private val validator: InputValidator by inject()

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


        view.findViewById<LoadingButton>(R.id.login_button).onClick {
            handleLogin(it, view)
        }
    }

    private fun handleLogin(button: LoadingButton, view: View) {
        val emailLayout = view.findViewById<TextInputLayout>(R.id.email_text_input_layout)
        val passwordLayout = view.findViewById<TextInputLayout>(R.id.password_text_input_layout)

        emailLayout.isErrorEnabled = false
        passwordLayout.isErrorEnabled = false

        var email = ""
        var password = ""

        emailLayout.editText?.let layout@ {
            email = it.text.toString()

            validator.isValidEmail(email).let { result ->
                if(result is Result.Error) {
                    emailLayout.error = result.message
                    emailLayout.isErrorEnabled = true
                    return@layout
                }
            }
        }

        passwordLayout.editText?.let layout@ {
            password = it.text.toString()

            validator.isValidPassword(password).let { result ->
                if(result is Result.Error) {
                    passwordLayout.error = result.message
                    passwordLayout.isErrorEnabled = true
                    return@layout
                }
            }
        }

        // TODO
        // Replace the Google error messages with my own:
        // "the password is invalid or the user does not have a password" should be "the password is invalid"

        if(!emailLayout.isErrorEnabled && !passwordLayout.isErrorEnabled) {
            button.startLoading()
            AuthService.signInWithEmailAndPassword(
                email = email,
                password = password,
                onSuccess = {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    activity?.finish()
                },
                onFailure = { error ->
                    button.stopLoading()
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