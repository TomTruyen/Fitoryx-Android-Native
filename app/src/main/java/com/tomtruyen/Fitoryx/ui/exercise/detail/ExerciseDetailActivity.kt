package com.tomtruyen.Fitoryx.ui.exercise.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.model.Exercise
import org.koin.android.ext.android.inject

class ExerciseDetailActivity : AppCompatActivity() {
    companion object {
        const val ARG_EXERCISE = "exercise"
    }

    private val viewModel: ExerciseDetailViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent.getSerializableExtra(ARG_EXERCISE)?.let {
            (it as Exercise).let { exercise ->
                viewModel.setExercise(exercise)
                supportActionBar?.title = exercise.name
            }
        }
    }
}