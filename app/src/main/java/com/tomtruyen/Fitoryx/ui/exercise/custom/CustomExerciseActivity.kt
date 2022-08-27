package com.tomtruyen.Fitoryx.ui.exercise.custom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
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

        val categories = resources.getStringArray(R.array.categories)
        val categoryAdapter = ArrayAdapter(this, R.layout.dropdown_item, categories)
        findViewById<AutoCompleteTextView>(R.id.category_auto_complete_text_view).also {
            it.setAdapter(categoryAdapter)
        }

        val equipment = resources.getStringArray(R.array.equipment)
        val equipmentAdapter = ArrayAdapter(this, R.layout.dropdown_item, equipment)
        findViewById<AutoCompleteTextView>(R.id.equipment_auto_complete_text_view).also {
            it.setAdapter(equipmentAdapter)
        }
    }
}