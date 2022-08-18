package com.tomtruyen.Fitoryx.ui.exercise

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.helper.BounceEdgeEffectFactory
import com.tomtruyen.Fitoryx.ui.exercise.filter.ExerciseFilterActivity
import org.koin.android.ext.android.inject

class ExerciseFragment : Fragment() {
    val viewModel: ExerciseViewModel by inject()
    private lateinit var adapter: ExerciseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActions()

        adapter = ExerciseAdapter(viewModel.exercises.value!!)
        setRecyclerView(view)

        viewModel.exercises.observe(viewLifecycleOwner) {
            adapter.updateExercises(it)
        }

        view.findViewById<FloatingActionButton>(R.id.add_exercise_fab).setOnClickListener {
            Toast.makeText(context, "Add exercise click!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setActions() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.exercise_actions_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    R.id.action_filter -> {
                        openFilterActivity()
                        true
                    }
                    R.id.action_search -> {
                        Toast.makeText(context, "Search exercise click!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setRecyclerView(view: View) {
        view.findViewById<RecyclerView>(R.id.exercise_recyler_view).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = this@ExerciseFragment.adapter
            edgeEffectFactory = BounceEdgeEffectFactory()
        }
    }

    private fun openFilterActivity() {
        val intent = Intent(context, ExerciseFilterActivity::class.java)

        intent.putExtra(ExerciseFilterActivity.ARG_COUNT, viewModel.getExerciseCount())
        intent.putStringArrayListExtra(ExerciseFilterActivity.ARG_FILTER_CATEGORIES, ArrayList(viewModel.getCategoryFilters()))
        intent.putStringArrayListExtra(ExerciseFilterActivity.ARG_FILTER_EQUIPMENTS, ArrayList(viewModel.getEquipmentFilters()))

        startActivity(intent)
    }
}