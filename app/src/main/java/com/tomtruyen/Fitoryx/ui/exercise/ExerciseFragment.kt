package com.tomtruyen.Fitoryx.ui.exercise

import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
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
import com.tomtruyen.Fitoryx.ui.exercise.custom.CustomExerciseActivity
import com.tomtruyen.Fitoryx.ui.exercise.detail.ExerciseDetailActivity
import com.tomtruyen.Fitoryx.ui.exercise.filter.ExerciseFilterActivity
import com.tomtruyen.Fitoryx.utils.Utils
import com.tomtruyen.Fitoryx.utils.setActionBarElevationOnScroll
import org.koin.android.ext.android.inject

class ExerciseFragment : Fragment() {
    val viewModel: ExerciseViewModel by inject()

    private var isSearching = false

    private lateinit var adapter: ExerciseAdapter
    private lateinit var optionsMenu: Menu

    private lateinit var customExerciseLauncher: ActivityResultLauncher<Intent>

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
        setRecyclerView()

        viewModel.exercises.observe(viewLifecycleOwner) {
            adapter.updateExercises(it)
        }

        customExerciseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Snackbar.make(
                    requireContext(),
                    view.findViewById(R.id.exercise_fragment_container),
                    getString(R.string.message_exercise_saved),
                    Snackbar.LENGTH_SHORT
                ).apply {
                    setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.success))
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }.show()
            }
        }

        view.findViewById<FloatingActionButton>(R.id.add_exercise_fab).setOnClickListener {
            customExerciseLauncher.launch(Intent(requireContext(), CustomExerciseActivity::class.java).apply {
                putExtra(CustomExerciseActivity.ARG_IS_NEW_EXERCISE, true)
            })
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
                        hideSearchField()
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
        adapter = ExerciseAdapter(viewModel.exercises.value!!) {
            findNavController().navigate(R.id.action_exercises_to_detail, Bundle().apply {
                putSerializable(ExerciseDetailActivity.ARG_EXERCISE, it)
            })
        }
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

                searchInputLayout.editText?.addTextChangedListener { text ->
                    viewModel.search(text.toString())
                }
            }
        }
    }

    private fun hideSearchIcon() {
        optionsMenu.findItem(R.id.action_search)?.isVisible = false
    }

    private fun hideSearchField() {
        Utils.getSupportActionBar(requireActivity())?.apply {
            setDisplayShowCustomEnabled(false)
            setDisplayHomeAsUpEnabled(false)
        }.also {
            optionsMenu.findItem(R.id.action_search)?.isVisible = true
            isSearching = false
        }
    }
}