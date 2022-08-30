package com.tomtruyen.Fitoryx.ui.workout.custom

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.model.Exercise

class CustomWorkoutAdapter(
    var exercises: List<Exercise>
): RecyclerView.Adapter<CustomWorkoutAdapter.ViewHolder>() {
    @SuppressLint("NotifyDataSetChanged")
    fun updateExercises(exercises: List<Exercise>) {
        this.exercises = exercises
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView

        init {
            exerciseName = itemView.findViewById(R.id.exercise_name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.workout_exercise_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = exercises[position]

        holder.exerciseName.text = exercise.name
    }


    override fun getItemCount(): Int = exercises.size
}