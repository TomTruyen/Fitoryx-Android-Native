package com.tomtruyen.Fitoryx.ui.exercise.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.model.Exercise
import com.tomtruyen.Fitoryx.service.CacheService
import com.tomtruyen.Fitoryx.ui.exercise.ExerciseFragment
import com.tomtruyen.Fitoryx.ui.exercise.custom.CustomExerciseActivity
import com.tomtruyen.Fitoryx.utils.Utils
import org.koin.android.ext.android.inject

class ExerciseDetailActivity : AppCompatActivity() {
    companion object {
        const val ARG_EXERCISE = "exercise"
        const val EDIT_EXERCISE_REQUEST_CODE = 1
    }

    private val viewModel: ExerciseDetailViewModel by inject()
    private lateinit var intentLauncher: ActivityResultLauncher<Intent>

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

        intentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == EDIT_EXERCISE_REQUEST_CODE) {
                (result.data?.getSerializableExtra(ARG_EXERCISE) as Exercise).let { exercise ->
                    viewModel.setExercise(exercise)
                    supportActionBar?.title = exercise.name
                    setResult(ExerciseFragment.EDIT_EXERCISE_RESULT_CODE, Intent().putExtra(ExerciseFragment.ARG_EDIT_EXERCISE, exercise))
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.message_exercise_edited), Snackbar.LENGTH_SHORT).
                            apply {
                                setBackgroundTint(ContextCompat.getColor(this@ExerciseDetailActivity, R.color.success))
                                setTextColor(ContextCompat.getColor(this@ExerciseDetailActivity, R.color.white))
                            }.show()
                }
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
                intentLauncher.launch(Intent(this, CustomExerciseActivity::class.java).apply {
                    putExtra(CustomExerciseActivity.ARG_IS_NEW_EXERCISE, false)
                    putExtra(CustomExerciseActivity.ARG_EXERCISE, viewModel.exercise)
                })
                true
            }
            R.id.action_delete -> {
                deleteExercise()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteExercise() {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.delete_exercise_title))
            .setMessage(resources.getString(R.string.delete_exercise_message))
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                viewModel.deleteExercise().addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        CacheService.deleteExercise(viewModel.exercise!!)
                        setResult(ExerciseFragment.DELETE_EXERCISE_REQUEST_CODE)
                        finish()
                    } else {
                        Utils.showErrorDialog(this, task.exception?.message ?: resources.getString(R.string.error_delete_exercise))
                    }
                }
            }
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}