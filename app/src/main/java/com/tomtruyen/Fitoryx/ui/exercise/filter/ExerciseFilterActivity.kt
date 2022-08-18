package com.tomtruyen.Fitoryx.ui.exercise.filter

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.tomtruyen.Fitoryx.R

class ExerciseFilterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_filter)

        supportActionBar?.let {
            it.title = "Filter"
            it.setDisplayHomeAsUpEnabled(true)
        }

        // use chips as item for filter: https://material.io/components/chips/android#using-chips
        setCategories()
        setEquipment()
    }

    private fun setCategories() {
        val categories = resources.getStringArray(R.array.categories)
        val chipGroup = findViewById<ChipGroup>(R.id.category_chip_group)

        categories.forEach {
            createChip(it, chipGroup)
        }
    }

    private fun setEquipment() {
        val equipment = resources.getStringArray(R.array.equipment)
        val chipGroup = findViewById<ChipGroup>(R.id.equipment_chip_group)

        equipment.forEach {
            createChip(it, chipGroup)
        }
    }

    private fun createChip(value: String, group: ChipGroup) {
        val chip = Chip(this).apply {
            id = View.generateViewId()
            text = value
            isCheckable = true
            isCheckedIconVisible = false
            chipBackgroundColor = ContextCompat.getColorStateList(this@ExerciseFilterActivity, R.color.chip_background_color)
            setTextColor(ContextCompat.getColorStateList(this@ExerciseFilterActivity, R.color.chip_text_color))
        }


        group.addView(chip)
    }
}