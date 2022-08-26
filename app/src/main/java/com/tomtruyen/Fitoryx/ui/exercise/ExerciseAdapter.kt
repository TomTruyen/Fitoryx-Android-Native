package com.tomtruyen.Fitoryx.ui.exercise

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.model.Exercise

const val TYPE_DIVIDER = 0
const val TYPE_EXERCISE = 1

class ExerciseAdapter(
    var exercises: List<Exercise>,
    private val onClick: (Exercise) -> Unit
): RecyclerView.Adapter<ExerciseAdapter.BaseViewHolder>() {
    @SuppressLint("NotifyDataSetChanged")
    fun updateExercises(exercises: List<Exercise>) {
        this.exercises = exercises
        notifyDataSetChanged()
    }

    open inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseLayout: LinearLayout
        val titleTextView: TextView
        val subtitleTextView: TextView

        init {
            exerciseLayout = itemView.findViewById(R.id.exercise_item_layout)
            titleTextView = itemView.findViewById(R.id.title)
            subtitleTextView = itemView.findViewById(R.id.subtitle)
        }
    }

    inner class ExerciseViewHolder(itemView: View): BaseViewHolder(itemView)

    inner class DividerViewHolder(itemView: View): BaseViewHolder(itemView) {
        var dividerTitleTextView: TextView

        init {
            dividerTitleTextView = itemView.findViewById(R.id.dividerTitle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if(viewType == TYPE_EXERCISE) {
            ExerciseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.exercise_list_item, parent, false))
        } else {
            DividerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.exercise_list_item_with_divider, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ExerciseAdapter.BaseViewHolder, position: Int) {
        val exercise = exercises[position]

        holder.titleTextView.text = exercise.getTitle()
        holder.subtitleTextView.text = exercise.getSubtitle()

        if(holder is DividerViewHolder) {
            holder.dividerTitleTextView.text = exercise.name.first().toString().uppercase()
        }

        holder.exerciseLayout.setOnClickListener { onClick(exercise) }
    }

    override fun getItemViewType(position: Int): Int {
        val currentLetter = exercises[position].name.first()
        val previousLetter = if (position > 0) exercises[position - 1].name.first() else null

        return if(position == 0 || (previousLetter != null && currentLetter != previousLetter)) {
            TYPE_DIVIDER
        } else {
            TYPE_EXERCISE
        }
    }

    override fun getItemCount(): Int = exercises.size
}