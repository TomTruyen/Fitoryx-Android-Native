package com.tomtruyen.Fitoryx

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.tomtruyen.Fitoryx.helper.ConnectivityObserver
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        findNavController(R.id.nav_host).let { controller ->
            navView.setupWithNavController(controller)

            // Handles the appBar title
            setupActionBarWithNavController(
                controller,
                AppBarConfiguration(
                    setOf(
                        R.id.navigation_profile,
                        R.id.navigation_nutrition,
                        R.id.navigation_workout,
                        R.id.exerciseFragment,
                        R.id.navigation_settings
                    )
                )
            )
        }

        observeConnectionStatus(navView)
    }

    private fun observeConnectionStatus(anchor: View) {
        var previousStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.Connected
        val container = findViewById<CoordinatorLayout>(R.id.container)
        ConnectivityObserver(this).observe().onEach { status ->
            when(status) {
                ConnectivityObserver.Status.Connected -> {
                    if(previousStatus != ConnectivityObserver.Status.Connected) {
                        Snackbar.make(
                            container,
                            "You have been reconnected!",
                            Snackbar.LENGTH_SHORT
                        ).also{
                            it.anchorView = anchor
                        }.show()
                    }
                }
                ConnectivityObserver.Status.Disconnected, ConnectivityObserver.Status.Unavailable -> {
                    Snackbar.make(
                        container,
                        "You are offline! Please reconnect to the internet.",
                        Snackbar.LENGTH_INDEFINITE
                    ).also{
                        it.anchorView = anchor
                    }.show()
                }
                ConnectivityObserver.Status.Losing -> {
                    Snackbar.make(
                        container,
                        "Your connection is weak! Please find a stronger connection.",
                        Snackbar.LENGTH_INDEFINITE
                    ).also{
                        it.anchorView = anchor
                    }.show()
                }
            }

            previousStatus = status
        }.launchIn(lifecycleScope)
    }
}