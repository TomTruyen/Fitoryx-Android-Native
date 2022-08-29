package com.tomtruyen.Fitoryx.ui.exercise.custom

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
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
import com.tomtruyen.Fitoryx.model.Exercise
import com.tomtruyen.Fitoryx.model.utils.Result
import com.tomtruyen.Fitoryx.service.CacheService
import com.tomtruyen.Fitoryx.ui.exercise.ExerciseFragment
import com.tomtruyen.Fitoryx.ui.exercise.detail.ExerciseDetailActivity
import com.tomtruyen.Fitoryx.utils.Utils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CustomExerciseActivity : AppCompatActivity() {
    companion object {
        const val ARG_EXERCISE = "exercise"
        const val ARG_IS_NEW_EXERCISE = "is_new_exercise"
    }

    private val viewModel: CustomExerciseViewModel by inject()

    private var isCreate: Boolean = true
    private lateinit var saveButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_exercise)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isCreate = intent.getBooleanExtra(ARG_IS_NEW_EXERCISE, true)
        isCreate.let { isCreate ->
            supportActionBar?.title = if(isCreate) {
                getString(R.string.title_custom_exercise_add)
            } else {
                getString(R.string.title_custom_exercise_edit)
            }

            if(!isCreate) {
                viewModel.exercise = intent.getSerializableExtra(ARG_EXERCISE) as Exercise
            }
        }

        setExercise()

        saveButton = findViewById(R.id.save_fab)
        saveButton.setOnClickListener {
            saveExercise()
        }
    }

    private fun setExercise() {
        viewModel.exercise?.let {
            findViewById<TextInputLayout>(R.id.name_text_input_layout).editText?.setText(viewModel.exercise!!.name)
            findViewById<AutoCompleteTextView>(R.id.category_auto_complete_text_view).text = Editable.Factory.getInstance().newEditable(viewModel.exercise!!.category)
            findViewById<AutoCompleteTextView>(R.id.equipment_auto_complete_text_view).text = Editable.Factory.getInstance().newEditable(viewModel.exercise!!.equipment)
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
                if(isCreate) {
                    viewModel.exercise =  Exercise().apply {
                        this.id = Utils.generateUUID()
                        this.name = name
                        this.category = category
                        this.equipment = equipment
                        userCreated = true
                    }
                } else {
                    viewModel.exercise?.apply {
                        this.name = name
                        this.category = category
                        this.equipment = equipment
                    }
                }

                viewModel.saveExercise().addOnCompleteListener {  task ->
                    if(task.isSuccessful) {
                        if(isCreate) {
                            CacheService.addExercise(viewModel.exercise!!)
                            setResult(ExerciseFragment.CUSTOM_EXERCISE_REQUEST_CODE)
                        } else {
                            CacheService.editExercise(viewModel.exercise!!)
                            setResult(ExerciseDetailActivity.EDIT_EXERCISE_REQUEST_CODE, Intent().apply {
                                putExtra(ExerciseDetailActivity.ARG_EXERCISE, viewModel.exercise)
                            })
                        }
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
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        saveButton.visibility = View.GONE
        findViewById<LinearLayout>(R.id.content).visibility = View.GONE
        findViewById<View>(R.id.saving_indicator).visibility = View.VISIBLE
    }

    private fun hideSaving() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        saveButton.visibility = View.VISIBLE
        findViewById<LinearLayout>(R.id.content).visibility = View.VISIBLE
        findViewById<View>(R.id.saving_indicator).visibility = View.GONE
    }

}