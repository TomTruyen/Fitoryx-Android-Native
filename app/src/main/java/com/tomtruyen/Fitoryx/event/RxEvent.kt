package com.tomtruyen.Fitoryx.event

class RxEvent {
    data class FilterCount(val count: Int)
    data class AddCategoryFilter(val category: String)
    data class AddEquipmentFilter(val equipment: String)
    data class RemoveCategoryFilter(val category: String)
    data class RemoveEquipmentFilter(val equipment: String)
    class ClearFilter
}