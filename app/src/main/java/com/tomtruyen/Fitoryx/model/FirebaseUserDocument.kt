package com.tomtruyen.Fitoryx.model

import com.google.firebase.firestore.FieldValue
import java.io.Serializable

data class FirebaseUserDocument(
    var exercises: MutableList<Exercise> = mutableListOf()
): Serializable