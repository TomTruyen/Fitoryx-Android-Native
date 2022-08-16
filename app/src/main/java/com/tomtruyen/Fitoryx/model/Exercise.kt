package com.tomtruyen.Fitoryx.model

class Exercise(
    var id: String? = null,
    var name: String,
    var category: String = "",
    var equipment: String = "",
    var userCreated: Boolean = false,
) {
    fun getTitle(): String {
        return if(equipment.isNotEmpty() && equipment != "None") {
            "$name ($equipment)"
        } else {
            name
        }
    }
}