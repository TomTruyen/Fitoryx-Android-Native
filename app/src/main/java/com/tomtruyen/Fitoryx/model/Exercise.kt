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

    override fun equals(other: Any?): Boolean =
        other is Exercise
                && other.id == id
                && other.name == name
                && other.category == category
                && other.equipment == equipment
                && other.userCreated == userCreated

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + equipment.hashCode()
        result = 31 * result + userCreated.hashCode()
        return result
    }
}