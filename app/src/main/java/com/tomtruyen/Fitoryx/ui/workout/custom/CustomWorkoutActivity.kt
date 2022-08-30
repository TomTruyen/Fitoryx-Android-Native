package com.tomtruyen.Fitoryx.ui.workout.custom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.factor.bouncy.BouncyRecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.model.Exercise
import com.tomtruyen.Fitoryx.ui.workout.custom.exercises.WorkoutExercisesActivity
import com.tomtruyen.Fitoryx.utils.setActionBarElevationOnScroll
import org.koin.android.ext.android.inject
import java.io.Serializable

class CustomWorkoutActivity : AppCompatActivity() {
    private val viewModel: CustomWorkoutViewModel by inject()

    private lateinit var adapter: CustomWorkoutAdapter
    private lateinit var intentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_workout)

        supportActionBar?.title = getString(R.string.title_create_workout)

        intentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == REQUEST_CODE_EXERCISES) {
                val exercises = (it.data?.getSerializableExtra(ARG_EXERCISES) as ArrayList<*>)
                    .filterIsInstance<Exercise>()

                viewModel.setExercises(exercises)
            }
        }

        adapter = CustomWorkoutAdapter(viewModel.exercises.value!!)

        findViewById<BouncyRecyclerView>(R.id.exercise_list).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = this@CustomWorkoutActivity.adapter
        }.also {
            it.setActionBarElevationOnScroll(supportActionBar)
        }

        viewModel.exercises.observe(this) {
            adapter.updateExercises(it)
        }

        findViewById<FloatingActionButton>(R.id.add_exercise_fab).setOnClickListener {
            intentLauncher.launch(Intent(this, WorkoutExercisesActivity::class.java).apply {
                putExtra(WorkoutExercisesActivity.ARG_WORKOUT_EXERCISES, viewModel.exercises.value as Serializable)
            })
        }
    }

    companion object {
        const val REQUEST_CODE_EXERCISES = 1
        const val ARG_EXERCISES = "exercises"
    }
}