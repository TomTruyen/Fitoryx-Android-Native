package com.tomtruyen.Fitoryx.service

import android.content.Context
import android.content.res.Resources
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

    fun saveExercise(exercise: Exercise): Task<Void> {
            return db.collection(USER_COLLECTION_NAME)
                .document(AuthService.getCurrentUser()!!.uid)
                .set(FirebaseUserDocument().apply {
                    exercises = FieldValue.arrayUnion(exercise)
                }, SetOptions.merge())
    }
}