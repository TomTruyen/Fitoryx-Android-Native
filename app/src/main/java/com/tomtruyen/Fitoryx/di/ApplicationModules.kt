package com.tomtruyen.Fitoryx.di

import com.tomtruyen.Fitoryx.ui.exercise.ExerciseViewModel
import com.tomtruyen.Fitoryx.ui.exercise.filter.ExerciseFilterViewModel
import com.tomtruyen.Fitoryx.ui.nutrition.NutritionViewModel
import com.tomtruyen.Fitoryx.ui.profile.ProfileViewModel
import com.tomtruyen.Fitoryx.ui.settings.SettingsViewModel
import com.tomtruyen.Fitoryx.ui.workout.WorkoutViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModules = module {
    viewModel { ProfileViewModel() }
    viewModel { NutritionViewModel() }
    viewModel { WorkoutViewModel() }
    viewModel { ExerciseViewModel() }
    viewModel { SettingsViewModel() }
    viewModel { ExerciseFilterViewModel() }
}