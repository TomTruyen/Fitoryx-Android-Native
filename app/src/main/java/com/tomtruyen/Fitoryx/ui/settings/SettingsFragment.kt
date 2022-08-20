package com.tomtruyen.Fitoryx.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.tomtruyen.Fitoryx.AuthenticationActivity
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.service.AuthService
import org.koin.android.ext.android.inject


class SettingsFragment : Fragment() {
    val viewModel: SettingsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.sign_out_button).setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        AuthService.signOut()
        startActivity(Intent(context, AuthenticationActivity::class.java))
        activity?.finish()
    }
}