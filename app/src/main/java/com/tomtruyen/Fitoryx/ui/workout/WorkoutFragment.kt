package com.tomtruyen.Fitoryx.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.factor.bouncy.BouncyNestedScrollView
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.utils.Utils
import com.tomtruyen.Fitoryx.utils.setActionBarElevationOnScroll
import org.koin.android.ext.android.inject

class WorkoutFragment : Fragment() {
    val viewModel: WorkoutViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<BouncyNestedScrollView>(R.id.nested_scroll_view).setActionBarElevationOnScroll(
            Utils.getSupportActionBar(requireActivity())
        )

        viewModel.text.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.text_workout).text = it
        }
    }
}