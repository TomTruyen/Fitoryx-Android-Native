package com.tomtruyen.Fitoryx.model

import java.io.Serializable

class Exercise(
    var id: String? = null,
    var name: String = "",
    var category: String = "",
    var equipment: String = "",
    var userCreated: Boolean = false,
): Serializable {
    fun getTitle(): String {
        return if(equipment.isNotEmpty() && equipment != "None") {
            "$name ($equipment)"
        } else {
            name
        }
    }
    fun getSubtitle(): String {
        return category.ifEmpty {
            ""
        }
    }
}