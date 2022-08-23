package com.tomtruyen.Fitoryx.ui.authentication

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.GoogleAuthProvider
import com.tomtruyen.Fitoryx.MainActivity
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.model.utils.Result
import com.tomtruyen.Fitoryx.service.AuthService
import com.tomtruyen.Fitoryx.service.GoogleAuthService
import com.tomtruyen.Fitoryx.utils.InputValidator
import com.tomtruyen.Fitoryx.utils.Utils
import com.tomtruyen.android.material.loadingbutton.LoadingButton
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {
    private val validator: InputValidator by inject()

    private lateinit var oneTapClient: SignInClient

    private val oneTapIntentLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        if(it.resultCode == Activity.RESULT_OK) {
            try {
                val credentials = oneTapClient.getSignInCredentialFromIntent(it.data)
                val idToken = credentials.googleIdToken

                signInWithCredentials(idToken)
            } catch (e: ApiException) {
                when(e.statusCode) {
                    CommonStatusCodes.CANCELED -> {
                        println("Canceled OneTap")
                    }
                    CommonStatusCodes.NETWORK_ERROR -> {
                        println("Network error OneTap")
                    }
                    else -> {
                        println("Unknown error OneTap")
                    }
                }
            }
        }
    }

    private val fallbackIntentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            try {
                val credentials = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                val idToken = credentials.result.idToken

                signInWithCredentials(idToken)
            } catch (e: ApiException) {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oneTapClient = Identity.getSignInClient(requireContext())

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

        view.findViewById<ImageButton>(R.id.google_sign_in_button).setOnClickListener {
            GoogleAuthService(
                context = requireContext(),
                client = oneTapClient,
                clientId = getString(R.string.firebase_client_id),
                oneTapIntentLauncher = oneTapIntentLauncher,
                fallbackIntentLauncher = fallbackIntentLauncher,
                onFailure = { error ->
                    Utils.showErrorDialog(requireContext(), error)
                }
            ).also {
                it.signInWithGoogle()
            }
        }
    }

    private fun finishLogin() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
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

        if(!emailLayout.isErrorEnabled && !passwordLayout.isErrorEnabled) {
            button.startLoading()
            AuthService.signInWithEmailAndPassword(
                email = email,
                password = password,
                onSuccess = {
                    finishLogin()
                },
                onFailure = { error ->
                    button.stopLoading()
                    Utils.showErrorDialog(requireContext(), error)
                }
            )
        }
    }

    private fun signInWithCredentials(idToken: String?) {
        try {

        } catch (e: ApiException) {
            Utils.showErrorDialog(requireContext(), getString(R.string.google_sign_in_failed, e.statusCode.toString()))
        }
        GoogleAuthService.signInWithCredentials(idToken).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                finishLogin()
            } else {
                Utils.showErrorDialog(requireContext(), task.exception?.message ?: getString(R.string.google_sign_in_failed, "Credentials"))
            }
        }
    }
}