package com.tomtruyen.Fitoryx.ui.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.helper.BounceEdgeEffectFactory
import org.koin.android.ext.android.inject

class ExerciseFragment : Fragment() {
    val viewModel: ExerciseViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.exercise_recyler_view)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = ExerciseAdapter(viewModel.exercises)
            edgeEffectFactory = BounceEdgeEffectFactory()
        }

//        val anim: SpringAnimation = SpringAnimation(recyclerView, SpringAnimation.TRANSLATION_Y)
//            .setSpring(
//                SpringForce()
//                .setFinalPosition(0f)
//                .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
//                .setStiffness(SpringForce.STIFFNESS_LOW)
//            )
    }
}