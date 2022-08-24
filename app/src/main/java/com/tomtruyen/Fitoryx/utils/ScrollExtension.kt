package com.tomtruyen.Fitoryx.utils

import androidx.appcompat.app.ActionBar
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.Fitoryx.MainActivity



fun NestedScrollView.setActionBarElevationOnScroll(actionBar: ActionBar?) {
    this.setOnScrollChangeListener { it, _, _, _, _ ->
        if(it.canScrollVertically(-1)) {
            actionBar?.elevation = 4f
        } else {
            actionBar?.elevation = 0f
        }
    }
}

fun RecyclerView.setActionBarElevationOnScroll(actionBar: ActionBar?) {
    this.setOnScrollChangeListener { it, _, _, _, _ ->
        if(it.canScrollVertically(-1)) {
            actionBar?.elevation = 4f
        } else {
            actionBar?.elevation = 0f
        }
    }
}
