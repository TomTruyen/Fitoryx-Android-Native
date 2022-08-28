package com.tomtruyen.Fitoryx.ui.exercise.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        viewModel.exercise?.let {
            if(it.userCreated) {
                menuInflater.inflate(R.menu.exercise_detail_actions_menu, menu)
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_edit -> {
                Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_delete -> {
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}