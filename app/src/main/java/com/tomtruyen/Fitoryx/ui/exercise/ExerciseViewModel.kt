package com.tomtruyen.Fitoryx.ui.exercise

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tomtruyen.Fitoryx.event.RxBus
import com.tomtruyen.Fitoryx.event.RxEvent
import com.tomtruyen.Fitoryx.model.Exercise
import com.tomtruyen.Fitoryx.service.CacheService
import io.reactivex.rxjava3.disposables.Disposable
import com.tomtruyen.Fitoryx.data.exercises as defaultExercises

class ExerciseViewModel : ViewModel() {
    // Filter Observables
    private var addCategoryObserver: Disposable
    private var removeCategoryObserver: Disposable
    private var addEquipmentObserver: Disposable
    private var removeEquipmentObserver: Disposable
    private var clearFilterObserver: Disposable

    private var _exercises: List<Exercise> = emptyList()

    val exercises = MutableLiveData(_exercises.sortedBy { it.name.lowercase() })

    private var filterQuery = ""
    private val filterCategories = MutableLiveData<List<String>>(emptyList())
    private val filterEquipment = MutableLiveData<List<String>>(emptyList())

    init {
        refreshExercises()

        addCategoryObserver = RxBus.listen(RxEvent.AddCategoryFilter::class.java).subscribe {
            addCategoryFilter(it.category)
        }

        removeCategoryObserver = RxBus.listen(RxEvent.RemoveCategoryFilter::class.java).subscribe {
            removeCategoryFilter(it.category)
        }

        addEquipmentObserver = RxBus.listen(RxEvent.AddEquipmentFilter::class.java).subscribe {
            addEquipmentFilter(it.equipment)
        }

        removeEquipmentObserver = RxBus.listen(RxEvent.RemoveEquipmentFilter::class.java).subscribe {
            removeEquipmentFilter(it.equipment)
        }

        clearFilterObserver = RxBus.listen(RxEvent.ClearFilter::class.java).subscribe {
            clearFilters()
        }
    }

    fun getExerciseCount(): Int = exercises.value?.size ?: 0

    fun getCategoryFilters(): List<String> = filterCategories.value ?: emptyList()

    fun getEquipmentFilters(): List<String> = filterEquipment.value ?: emptyList()

    private fun filter() {
        exercises.value = _exercises.filter categoryFilter@ {
            if(filterCategories.value.isNullOrEmpty()) return@categoryFilter true

            if(it.category.isEmpty() && filterCategories.value!!.contains("None")) {
                return@categoryFilter true
            }

            return@categoryFilter filterCategories.value!!.contains(it.category)
        }.filter equipmentFilter@ {
            if(filterEquipment.value.isNullOrEmpty()) return@equipmentFilter true

            if(it.equipment.isEmpty() && filterEquipment.value!!.contains("None")) {
                return@equipmentFilter true
            }

            return@equipmentFilter filterEquipment.value!!.contains(it.equipment)
        }.filter {
            it.name.contains(filterQuery, true)
        }.sortedBy { it.name.lowercase() }

        RxBus.publish(RxEvent.FilterCount(exercises.value!!.size))
    }

    private fun addCategoryFilter(category: String) {
        filterCategories.value = filterCategories.value?.plus(category)
        filter()
    }

    private fun removeCategoryFilter(category: String) {
        filterCategories.value = filterCategories.value?.minus(category)
        filter()
    }

    private fun addEquipmentFilter(equipment: String) {
        filterEquipment.value = filterEquipment.value?.plus(equipment)
        filter()
    }

    private fun removeEquipmentFilter(equipment: String) {
        filterEquipment.value = filterEquipment.value?.minus(equipment)
        filter()
    }

    private fun clearFilters() {
        filterCategories.value = emptyList()
        filterEquipment.value = emptyList()
        filter()
    }

    fun refreshExercises() {
        _exercises = defaultExercises + CacheService.getExercises()
        filter()
    }

    fun search(text: String) {
        filterQuery = text
        filter()
    }

    override fun onCleared() {
        super.onCleared()

        addCategoryObserver.dispose()
        removeCategoryObserver.dispose()
        addEquipmentObserver.dispose()
        removeEquipmentObserver.dispose()
        clearFilterObserver.dispose()
    }
}