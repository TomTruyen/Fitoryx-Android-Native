package com.tomtruyen.Fitoryx.ui.workout.custom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tomtruyen.Fitoryx.model.Exercise
import com.tomtruyen.Fitoryx.service.FirebaseService

class CustomWorkoutViewModel(
    private val firebaseService: FirebaseService
): ViewModel() {
    var exercises = MutableLiveData<List<Exercise>>(emptyList())
        private set

    fun setExercises(exercises: List<Exercise>) {
        this.exercises.value = exercises
    }

    fun clear() {
        exercises.value = emptyList()
    }
}