package com.tomtruyen.Fitoryx.ui.workout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.factor.bouncy.BouncyNestedScrollView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.ui.workout.custom.CustomWorkoutActivity
import com.tomtruyen.Fitoryx.utils.Utils
import com.tomtruyen.Fitoryx.utils.setActionBarElevationOnScroll
import org.koin.android.ext.android.inject

class WorkoutFragment : Fragment() {
    val viewModel: WorkoutViewModel by inject()

    private lateinit var intentLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == REQUEST_CODE_CREATE_WORKOUT) {
                // TODO handle new workout (add to list of workouts in VM)
            }
        }

        view.findViewById<BouncyNestedScrollView>(R.id.nested_scroll_view).setActionBarElevationOnScroll(
            Utils.getSupportActionBar(requireActivity())
        )

        view.findViewById<FloatingActionButton>(R.id.fab_create_workout).setOnClickListener {
            intentLauncher.launch(Intent(requireContext(), CustomWorkoutActivity::class.java))
        }
    }

    companion object {
        const val REQUEST_CODE_CREATE_WORKOUT = 1
    }
}