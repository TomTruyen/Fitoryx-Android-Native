package com.tomtruyen.Fitoryx.ui.exercise.detail

import androidx.lifecycle.ViewModel
import com.tomtruyen.Fitoryx.model.Exercise

class ExerciseDetailViewModel: ViewModel() {
    private var exercise: Exercise? = null

    fun setExercise(exercise: Exercise) {
        this.exercise = exercise
    }
}