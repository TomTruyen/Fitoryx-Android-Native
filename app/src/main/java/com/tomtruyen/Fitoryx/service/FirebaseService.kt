package com.tomtruyen.Fitoryx.service

import android.content.Context
import android.content.res.Resources
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.Cache
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.model.Exercise
import com.tomtruyen.Fitoryx.model.FirebaseUserDocument
import com.tomtruyen.Fitoryx.model.utils.Result
import kotlinx.coroutines.flow.callbackFlow
import java.util.*

const val USER_COLLECTION_NAME = "users"

const val EXERCISE_FIELD = "exercises"

class FirebaseService {
    private val db = Firebase.firestore

    fun initialize(): Task<DocumentSnapshot> {
        return db.collection(USER_COLLECTION_NAME).document(AuthService.getCurrentUser()!!.uid).get()
    }

    fun saveExercise(exercise: Exercise): Task<Void> {
        val exercises = CacheService.getExercises()

        val newExercise = exercises.firstOrNull { it.id == exercise.id } == null

        if(newExercise) {
            return db.collection(USER_COLLECTION_NAME)
                .document(AuthService.getCurrentUser()!!.uid)
                .set(HashMap<String, FieldValue>().apply {
                    put(EXERCISE_FIELD, FieldValue.arrayUnion(exercise))
                }, SetOptions.merge())
        } else {
            exercises.first { it.id == exercise.id }.let {
                it.name = exercise.name
                it.category = exercise.category
                it.equipment = exercise.equipment
            }

            return db.collection(USER_COLLECTION_NAME)
                .document(AuthService.getCurrentUser()!!.uid)
                .set(HashMap<String, List<Exercise>>().apply {
                    put(EXERCISE_FIELD, exercises)
                }, SetOptions.merge())
        }
    }

    fun deleteExercise(exercise: Exercise): Task<Void> {
        return db.collection(USER_COLLECTION_NAME)
            .document(AuthService.getCurrentUser()!!.uid)
            .set(HashMap<String, FieldValue>().apply {
                put(EXERCISE_FIELD, FieldValue.arrayRemove(exercise))
            }, SetOptions.merge())
    }
}