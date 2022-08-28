package com.tomtruyen.Fitoryx.ui.exercise.custom

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.model.Exercise
import com.tomtruyen.Fitoryx.model.utils.Result
import com.tomtruyen.Fitoryx.service.FirebaseService
import com.tomtruyen.Fitoryx.utils.Utils
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CustomExerciseViewModel(
    private val firebaseService: FirebaseService,
): ViewModel() {
    fun saveExercise(exercise: Exercise): Task<Void> {
        return firebaseService.saveExercise(exercise)
    }
}