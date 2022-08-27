package com.tomtruyen.Fitoryx.ui.exercise.custom

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.model.utils.Result
import com.tomtruyen.Fitoryx.utils.Utils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CustomExerciseActivity : AppCompatActivity() {
    companion object {
        const val ARG_IS_NEW_EXERCISE = "is_new_exercise"
    }

    private val viewModel: CustomExerciseViewModel by inject()

    private lateinit var saveButton: FloatingActionButton

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

        saveButton = findViewById<FloatingActionButton>(R.id.save_fab)
        saveButton.setOnClickListener {
            saveExercise()
        }
    }

    private fun saveExercise() {
        findViewById<TextInputLayout>(R.id.name_text_input_layout).let {
            if(it.editText?.text.isNullOrEmpty()) {
                it.error = getString(R.string.error_exercise_name_empty)
                it.isErrorEnabled = true
                return@let
            }

            it.isErrorEnabled = false

            val name = it.editText?.text.toString()
            val category = findViewById<AutoCompleteTextView>(R.id.category_auto_complete_text_view).text.toString()
            val equipment = findViewById<AutoCompleteTextView>(R.id.equipment_auto_complete_text_view).text.toString()

            showSaving()

            lifecycleScope.launch {
                viewModel.saveExercise(
                    name = name,
                    category = category,
                    equipment = equipment
                ).addOnCompleteListener {  task ->
                    if(task.isSuccessful) {
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        hideSaving()
                        Utils.showErrorDialog(this@CustomExerciseActivity, task.exception?.message ?: resources.getString(R.string.error_save_exercise))
                    }
                }
            }
        }
    }

    private fun showSaving() {
        saveButton.visibility = View.GONE
        findViewById<LinearLayout>(R.id.content).visibility = View.GONE
        findViewById<View>(R.id.saving_indicator).visibility = View.VISIBLE
    }

    private fun hideSaving() {
        saveButton.visibility = View.VISIBLE
        findViewById<LinearLayout>(R.id.content).visibility = View.VISIBLE
        findViewById<View>(R.id.saving_indicator).visibility = View.GONE
    }

}