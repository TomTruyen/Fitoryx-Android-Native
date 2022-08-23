package com.tomtruyen.Fitoryx.ui.authentication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.tomtruyen.Fitoryx.MainActivity
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.model.utils.Result
import com.tomtruyen.Fitoryx.service.AuthService
import com.tomtruyen.Fitoryx.utils.InputValidator
import com.tomtruyen.android.material.loadingbutton.LoadingButton
import org.koin.android.ext.android.inject

class RegisterFragment : Fragment() {
    private val validator: InputValidator by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.login_text_view).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.setCustomAnimations(
                    R.anim.enter_left_to_right,
                    R.anim.exit_left_to_right,
                    R.anim.enter_right_to_left,
                    R.anim.exit_right_to_left
                )
                ?.replace(R.id.fragment_container_view, LoginFragment())
                ?.commit()
        }

        view.findViewById<LoadingButton>(R.id.register_button).onClick {
            handleRegister(it)
        }
    }

    private fun handleRegister(button: LoadingButton) {
        view?.let { view ->
            val emailLayout = view.findViewById<TextInputLayout>(R.id.email_text_input_layout)
            val passwordLayout = view.findViewById<TextInputLayout>(R.id.password_text_input_layout)

            emailLayout.isErrorEnabled = false
            passwordLayout.isErrorEnabled = false

            var email = ""
            var password = ""

            emailLayout.editText?.let layout@{
                email = it.text.toString()

                validator.isValidEmail(email).let { result ->
                    if (result is Result.Error) {
                        emailLayout.error = result.message
                        emailLayout.isErrorEnabled = true
                        return@layout
                    }
                }
            }

            passwordLayout.editText?.let layout@{
                password = it.text.toString()

                validator.isValidPassword(password).let { result ->
                    if (result is Result.Error) {
                        passwordLayout.error = result.message
                        passwordLayout.isErrorEnabled = true
                        return@layout
                    }
                }
            }

            if (!emailLayout.isErrorEnabled && !passwordLayout.isErrorEnabled) {
                button.startLoading()
                AuthService.signUpWithEmailAndPassword(
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
}