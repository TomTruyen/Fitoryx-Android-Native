package com.tomtruyen.Fitoryx.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.utils.Utils
import com.tomtruyen.Fitoryx.utils.setActionBarElevationOnScroll
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment() {
    val viewModel: ProfileViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<NestedScrollView>(R.id.nested_scroll_view).setActionBarElevationOnScroll(
            Utils.getSupportActionBar(requireActivity())
        )

        viewModel.text.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.text_profile).text = it
        }
    }
}