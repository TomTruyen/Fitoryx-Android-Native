package com.tomtruyen.Fitoryx.ui.workout.custom.exercises

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.model.Exercise
import com.tomtruyen.Fitoryx.ui.exercise.ExerciseFragment

class WorkoutExercisesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_exercises)

        supportActionBar?.title = getString(R.string.title_workout_exercises)

        val exercises = (intent.getSerializableExtra(ARG_WORKOUT_EXERCISES) as ArrayList<*>).filterIsInstance<Exercise>()

        supportFragmentManager.beginTransaction()
            .replace(R.id.exercise_fragment_container, ExerciseFragment.newInstance(exercises, true))
            .commit()
    }

    companion object {
        const val ARG_WORKOUT_EXERCISES = "exercises"
    }
}