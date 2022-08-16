package com.tomtruyen.Fitoryx.ui.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.Fitoryx.R
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
        recyclerView.adapter = ExerciseAdapter(viewModel.exercises)
    }
}