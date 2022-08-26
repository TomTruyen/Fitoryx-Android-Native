package com.tomtruyen.Fitoryx.ui.exercise.custom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tomtruyen.Fitoryx.R
import org.koin.android.ext.android.inject

class CustomExerciseActivity : AppCompatActivity() {
    companion object {
        const val ARG_IS_NEW_EXERCISE = "is_new_exercise"
    }

    private val viewModel: CustomExerciseViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_exercise)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent.getBooleanExtra(ARG_IS_NEW_EXERCISE, true).let { isCreate ->
            supportActionBar?.title = if(isCreate) {
                getString(R.string.title_custom_exercise_add)
            } else {
                getString(R.string.title_custom_exercise_edit)
            }
        }
    }
}