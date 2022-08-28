package com.tomtruyen.Fitoryx.ui.exercise.detail

import androidx.lifecycle.ViewModel
import com.tomtruyen.Fitoryx.model.Exercise

class ExerciseDetailViewModel: ViewModel() {
    var exercise: Exercise? = null
        private set

    fun setExercise(exercise: Exercise) {
        this.exercise = exercise
    }
}