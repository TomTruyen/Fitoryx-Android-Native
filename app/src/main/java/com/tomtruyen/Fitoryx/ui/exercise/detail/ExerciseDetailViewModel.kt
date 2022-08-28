package com.tomtruyen.Fitoryx.ui.exercise.detail

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.tomtruyen.Fitoryx.model.Exercise
import com.tomtruyen.Fitoryx.service.CacheService.deleteExercise
import com.tomtruyen.Fitoryx.service.FirebaseService

class ExerciseDetailViewModel(
    private val firebaseService: FirebaseService
): ViewModel() {
    var exercise: Exercise? = null
        private set

    fun setExercise(exercise: Exercise) {
        this.exercise = exercise
    }

    fun deleteExercise(): Task<Void> {
        return firebaseService.deleteExercise(exercise!!)
    }
}