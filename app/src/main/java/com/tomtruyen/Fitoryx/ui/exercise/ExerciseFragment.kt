package com.tomtruyen.Fitoryx.ui.exercise

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.factor.bouncy.BouncyNestedScrollView
import com.factor.bouncy.BouncyRecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.tomtruyen.Fitoryx.MainActivity
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.model.Exercise
import com.tomtruyen.Fitoryx.ui.exercise.custom.CustomExerciseActivity
import com.tomtruyen.Fitoryx.ui.exercise.detail.ExerciseDetailActivity
import com.tomtruyen.Fitoryx.ui.exercise.filter.ExerciseFilterActivity
import com.tomtruyen.Fitoryx.ui.workout.custom.CustomWorkoutActivity
import com.tomtruyen.Fitoryx.utils.Utils
import com.tomtruyen.Fitoryx.utils.setActionBarElevationOnScroll
import org.koin.android.ext.android.inject
import java.io.Serializable

class ExerciseFragment : Fragment() {
    val viewModel: ExerciseViewModel by inject()

    private var isSearching = false
    private var isParentWorkout = false

    private lateinit var adapter: ExerciseAdapter
    private lateinit var optionsMenu: Menu

    private lateinit var intentLauncher: ActivityResultLauncher<Intent>
    private lateinit var inputManager: InputMethodManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        arguments?.getBoolean(ARG_PARENT_IS_WORKOUT)?.let { isParentWorkout ->
            this.isParentWorkout = isParentWorkout

            if(isParentWorkout) {
                val workoutExercises = (arguments?.getSerializable(ARG_WORKOUT_EXERCISES) as ArrayList<*>)
                    .filterIsInstance<Exercise>()

                viewModel.exercises.value?.forEach { exercise ->
                    exercise.selected = workoutExercises.contains(exercise)
                }

                adapter = ExerciseAdapter(viewModel.exercises.value!!) { exercise, position ->
                    exercise.selected = !exercise.selected
                    adapter.notifyItemChanged(position)
                }

                view.findViewById<FloatingActionButton>(R.id.add_exercise_fab)?.let { fab ->
                    fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_check))
                    fab.setOnClickListener {
                        activity?.let { activity ->
                            activity.setResult(CustomWorkoutActivity.REQUEST_CODE_EXERCISES, Intent().apply {
                                putExtra(CustomWorkoutActivity.ARG_EXERCISES, adapter.exercises.filter { it.selected } as ArrayList<Exercise>)
                            })
                            activity.finish()
                        }
                    }
                }

            }
        }

        if(!isParentWorkout) {
            adapter = ExerciseAdapter(viewModel.exercises.value!!) {  exercise, _ ->
                intentLauncher.launch(Intent(requireContext(), ExerciseDetailActivity::class.java).apply {
                    putExtra(ExerciseDetailActivity.ARG_EXERCISE, exercise)
                })
            }

            view.findViewById<FloatingActionButton>(R.id.add_exercise_fab).setOnClickListener {
                intentLauncher.launch(Intent(requireContext(), CustomExerciseActivity::class.java).apply {
                    putExtra(CustomExerciseActivity.ARG_IS_NEW_EXERCISE, true)
                })
            }
        }

        viewModel.exercises.observe(viewLifecycleOwner) {
            adapter.updateExercises(it)
        }

        setActions()
        setRecyclerView()

        intentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == CUSTOM_EXERCISE_REQUEST_CODE) {
                handleCustomExerciseResult()
            }

            if(result.resultCode == EDIT_EXERCISE_RESULT_CODE) {
                handleEditExerciseResult()
            }

            if(result.resultCode == DELETE_EXERCISE_REQUEST_CODE) {
                handleExerciseDeleteResult()
            }
        }
    }

    override fun onDestroy() {
        hideSearchField()
        super.onDestroy()
    }

    private fun setActions() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                optionsMenu = menu
                menuInflater.inflate(R.menu.exercise_actions_menu, menu)

                if(isSearching) {
                    hideSearchIcon()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    android.R.id.home -> {
                        if(isSearching) {
                            hideSearchField()
                        }

                        if(isParentWorkout) {
                            activity?.finish()
                        }

                        true
                    }
                    R.id.action_filter -> {
                        openFilterActivity()
                        true
                    }
                    R.id.action_search -> {
                        showSearchField()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setRecyclerView() {
        view?.let {
            it.findViewById<BouncyRecyclerView>(R.id.exercise_recycler_view).apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = this@ExerciseFragment.adapter
            }.also { recyclerView ->
                recyclerView.setActionBarElevationOnScroll(Utils.getSupportActionBar(requireActivity()))
            }
        }
    }

    private fun openFilterActivity() {
        findNavController().navigate(R.id.action_exercises_to_filter, Bundle().apply {
            putInt(ExerciseFilterActivity.ARG_COUNT, viewModel.getExerciseCount())
            putStringArrayList(ExerciseFilterActivity.ARG_FILTER_CATEGORIES, ArrayList(viewModel.getCategoryFilters()))
            putStringArrayList(ExerciseFilterActivity.ARG_FILTER_EQUIPMENTS, ArrayList(viewModel.getEquipmentFilters()))
        })
    }

    private fun showSearchField() {
        isSearching = true
        Utils.getSupportActionBar(requireActivity())?.apply {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setCustomView(R.layout.exercise_search_bar)
        }?.also {
            hideSearchIcon()

            it.customView.findViewById<TextInputLayout>(R.id.search_input_layout)?.let { searchInputLayout ->
                searchInputLayout.setEndIconOnClickListener {
                    searchInputLayout.editText?.text?.clear()
                }

                searchInputLayout.editText?.let { editText ->
                    editText.requestFocus()
                    inputManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

                    editText.addTextChangedListener { text ->
                        viewModel.search(text.toString())
                    }
                }
            }
        }
    }

    private fun hideSearchIcon() {
        optionsMenu.findItem(R.id.action_search)?.isVisible = false
    }

    private fun hideSearchField() {
        Utils.getSupportActionBar(requireActivity())?.also {
            it.customView?.findViewById<TextInputLayout>(R.id.search_input_layout)?.editText?.let { editText ->
                inputManager.hideSoftInputFromWindow(editText.windowToken, 0)
            }

            it.setDisplayShowCustomEnabled(false)
            it.setDisplayHomeAsUpEnabled(false)
        }.also {
            optionsMenu.findItem(R.id.action_search)?.isVisible = true
            isSearching = false
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            requireContext(),
            requireView().findViewById(R.id.exercise_fragment_container),
            message,
            Snackbar.LENGTH_SHORT
        ).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.success))
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }.show()
    }

    private fun handleCustomExerciseResult() {
        viewModel.refreshExercises()
        showSnackbar(getString(R.string.message_exercise_saved))
    }

    private fun handleEditExerciseResult() {
        viewModel.refreshExercises()
    }

    private fun handleExerciseDeleteResult() {
        viewModel.refreshExercises()
        showSnackbar(getString(R.string.message_exercise_deleted))
    }

    companion object {
        const val ARG_EDIT_EXERCISE = "edit_exercise"
        const val ARG_PARENT_IS_WORKOUT = "parent_is_workout"
        const val ARG_WORKOUT_EXERCISES = "workout_exercises"
        const val CUSTOM_EXERCISE_REQUEST_CODE = 1
        const val DELETE_EXERCISE_REQUEST_CODE = 2
        const val EDIT_EXERCISE_RESULT_CODE = 3

        fun newInstance(exercises: List<Exercise> = emptyList(), parentIsWorkout: Boolean = false) = ExerciseFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_WORKOUT_EXERCISES, exercises as Serializable)
                putBoolean(ARG_PARENT_IS_WORKOUT, parentIsWorkout)
            }
        }
    }
}