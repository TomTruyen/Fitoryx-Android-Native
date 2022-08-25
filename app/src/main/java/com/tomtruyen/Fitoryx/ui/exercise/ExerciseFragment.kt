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
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.factor.bouncy.BouncyNestedScrollView
import com.factor.bouncy.BouncyRecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.tomtruyen.Fitoryx.MainActivity
import com.tomtruyen.Fitoryx.R
import com.tomtruyen.Fitoryx.ui.exercise.filter.ExerciseFilterActivity
import com.tomtruyen.Fitoryx.utils.Utils
import com.tomtruyen.Fitoryx.utils.setActionBarElevationOnScroll
import org.koin.android.ext.android.inject

class ExerciseFragment : Fragment() {
    val viewModel: ExerciseViewModel by inject()

    private var isSearching = false

    private lateinit var adapter: ExerciseAdapter
    private lateinit var optionsMenu: Menu

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

        view.findViewById<FloatingActionButton>(R.id.add_exercise_fab).setOnClickListener {
            Toast.makeText(context, "Add exercise click!", Toast.LENGTH_SHORT).show()
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
                println("onCreateMenu")
                optionsMenu = menu
                val actions = menuInflater.inflate(R.menu.exercise_actions_menu, menu)

                if(isSearching) {
                    hideSearchIcon()
                }

                return actions
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
        adapter = ExerciseAdapter(viewModel.exercises.value!!)
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
        val intent = Intent(context, ExerciseFilterActivity::class.java)

        intent.putExtra(ExerciseFilterActivity.ARG_COUNT, viewModel.getExerciseCount())
        intent.putStringArrayListExtra(ExerciseFilterActivity.ARG_FILTER_CATEGORIES, ArrayList(viewModel.getCategoryFilters()))
        intent.putStringArrayListExtra(ExerciseFilterActivity.ARG_FILTER_EQUIPMENTS, ArrayList(viewModel.getEquipmentFilters()))

        startActivity(intent)
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