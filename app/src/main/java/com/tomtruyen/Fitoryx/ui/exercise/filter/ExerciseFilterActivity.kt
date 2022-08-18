package com.tomtruyen.Fitoryx.ui.exercise.filter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.tomtruyen.Fitoryx.R
import org.koin.android.ext.android.inject

class ExerciseFilterActivity : AppCompatActivity() {
    companion object {
        const val ARG_COUNT = "count"
        const val ARG_FILTER_CATEGORIES = "filter_categories"
        const val ARG_FILTER_EQUIPMENTS = "filter_equipments"
    }

    private val viewModel: ExerciseFilterViewModel by inject()

    private lateinit var categoryChipGroup: ChipGroup
    private lateinit var equipmentChipGroup: ChipGroup

    private var intentCategoryFilters: ArrayList<String> = arrayListOf()
    private var intentEquipmentFilters: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_filter)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intentCategoryFilters = intent.getStringArrayListExtra(ARG_FILTER_CATEGORIES) ?: arrayListOf()
        intentEquipmentFilters = intent.getStringArrayListExtra(ARG_FILTER_EQUIPMENTS) ?: arrayListOf()

        intent.getIntExtra(ARG_COUNT, 0).let {
            viewModel.count.value = it
        }

        viewModel.count.observe(this) { count ->
            supportActionBar?.title = "Filter ($count)"
        }

        categoryChipGroup = findViewById(R.id.category_chip_group)
        equipmentChipGroup = findViewById(R.id.equipment_chip_group)

        setCategories()
        setEquipment()

        findViewById<Button>(R.id.clear_filter_button).setOnClickListener {
            clearFilters()
        }
    }

    private enum class ChipType {
        CATEGORY, EQUIPMENT
    }

    private fun setCategories() {
        val categories = resources.getStringArray(R.array.categories)

        categories.forEach {
            createChip(it, categoryChipGroup, ChipType.CATEGORY)
        }
    }

    private fun setEquipment() {
        val equipment = resources.getStringArray(R.array.equipment)

        equipment.forEach {
            createChip(it, equipmentChipGroup, ChipType.EQUIPMENT)
        }
    }

    private fun clearFilters() {
        viewModel.clearFilters()
        categoryChipGroup.clearCheck()
        equipmentChipGroup.clearCheck()
    }

    private fun createChip(value: String, group: ChipGroup, type: ChipType) {
        val chip = Chip(this).apply {
            id = value.hashCode()
            text = value
            isCheckable = true
            isCheckedIconVisible = false
            chipBackgroundColor = ContextCompat.getColorStateList(this@ExerciseFilterActivity, R.color.chip_background_color)
            setTextColor(ContextCompat.getColorStateList(this@ExerciseFilterActivity, R.color.chip_text_color))
        }

        // Set already selected filters as checked by using the value passed in the Bundle of the Intent
        when(type) {
            ChipType.CATEGORY -> {
                if(intentCategoryFilters.contains(value)) {
                    chip.isChecked = true
                }
            }
            ChipType.EQUIPMENT -> {
                if(intentEquipmentFilters.contains(value)) {
                    chip.isChecked = true
                }
            }
        }

        // Handle CheckedState changes
        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                when (type) {
                    ChipType.CATEGORY -> viewModel.addCategoryFilter(value)
                    ChipType.EQUIPMENT -> viewModel.addEquipmentFilter(value)
                }
            } else {
                when (type) {
                    ChipType.CATEGORY -> viewModel.removeCategoryFilter(value)
                    ChipType.EQUIPMENT -> viewModel.removeEquipmentFilter(value)
                }
            }
        }

        group.addView(chip)
    }
}