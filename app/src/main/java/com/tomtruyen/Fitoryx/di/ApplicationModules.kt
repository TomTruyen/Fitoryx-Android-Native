package com.tomtruyen.Fitoryx.di

import com.tomtruyen.Fitoryx.service.FirebaseService
import com.tomtruyen.Fitoryx.ui.exercise.ExerciseViewModel
import com.tomtruyen.Fitoryx.ui.exercise.custom.CustomExerciseViewModel
import com.tomtruyen.Fitoryx.ui.exercise.detail.ExerciseDetailViewModel
import com.tomtruyen.Fitoryx.ui.exercise.filter.ExerciseFilterViewModel
import com.tomtruyen.Fitoryx.ui.nutrition.NutritionViewModel
import com.tomtruyen.Fitoryx.ui.profile.ProfileViewModel
import com.tomtruyen.Fitoryx.ui.settings.SettingsViewModel
import com.tomtruyen.Fitoryx.ui.workout.WorkoutViewModel
import com.tomtruyen.Fitoryx.utils.InputValidator
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModules = module {
    single { InputValidator(androidContext().resources) }
    single { FirebaseService() }
    viewModel { ProfileViewModel() }
    viewModel { NutritionViewModel() }
    viewModel { WorkoutViewModel() }
    viewModel { ExerciseViewModel() }
    viewModel { SettingsViewModel() }
    viewModel { ExerciseFilterViewModel() }
    viewModel { ExerciseDetailViewModel(firebaseService = get()) }
    viewModel { CustomExerciseViewModel(firebaseService = get()) }
}