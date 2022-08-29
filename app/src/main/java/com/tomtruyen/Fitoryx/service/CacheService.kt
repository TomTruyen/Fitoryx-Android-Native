package com.tomtruyen.Fitoryx.service

import com.tomtruyen.Fitoryx.model.Exercise
import com.tomtruyen.Fitoryx.model.FirebaseUserDocument

object CacheService {
    private var firebaseUserDocument: FirebaseUserDocument? = null

    fun setFirebaseUserDocument(firebaseUserDocument: FirebaseUserDocument) {
        this.firebaseUserDocument = firebaseUserDocument
    }

    fun getFirebaseUserDocument(): FirebaseUserDocument? {
        return firebaseUserDocument
    }

    fun addExercise(exercise: Exercise) {
        firebaseUserDocument?.exercises?.add(exercise)
    }

    fun editExercise(exercise: Exercise) {
        firebaseUserDocument?.exercises?.find {
            it.id == exercise.id
        }?.let {
            it.name = exercise.name
            it.equipment = exercise.equipment
            it.category = exercise.category
        }
    }

    fun deleteExercise(exercise: Exercise) {
        firebaseUserDocument?.exercises?.remove(exercise)
    }

    fun getExercises() = firebaseUserDocument?.exercises ?: emptyList()

}