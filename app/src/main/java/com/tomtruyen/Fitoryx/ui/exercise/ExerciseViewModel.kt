package com.tomtruyen.Fitoryx.ui.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tomtruyen.Fitoryx.model.Exercise
import com.tomtruyen.Fitoryx.data.exercises as defaultExercises

class ExerciseViewModel : ViewModel() {
    val exercises = defaultExercises
}