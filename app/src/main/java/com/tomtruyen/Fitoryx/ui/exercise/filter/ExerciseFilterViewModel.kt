package com.tomtruyen.Fitoryx.ui.exercise.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tomtruyen.Fitoryx.event.RxBus
import com.tomtruyen.Fitoryx.event.RxEvent
import io.reactivex.rxjava3.disposables.Disposable

class ExerciseFilterViewModel: ViewModel() {
    private var exerciseCountObserver: Disposable

    val count = MutableLiveData(0)

    init {
        exerciseCountObserver = RxBus.listen(RxEvent.FilterCount::class.java).subscribe {
            count.value = it.count
        }
    }

    override fun onCleared() {
        super.onCleared()
        exerciseCountObserver.dispose()
    }

    fun addCategoryFilter(category: String) {
        RxBus.publish(RxEvent.AddCategoryFilter(category))
    }

    fun removeCategoryFilter(category: String) {
        RxBus.publish(RxEvent.RemoveCategoryFilter(category))
    }

    fun addEquipmentFilter(equipment: String) {
        RxBus.publish(RxEvent.AddEquipmentFilter(equipment))
    }

    fun removeEquipmentFilter(equipment: String) {
        RxBus.publish(RxEvent.RemoveEquipmentFilter(equipment))
    }

    fun clearFilters() {
        RxBus.publish(RxEvent.ClearFilter())
    }
}