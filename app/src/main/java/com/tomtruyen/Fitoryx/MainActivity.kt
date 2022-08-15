package com.tomtruyen.Fitoryx

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findNavController(R.id.nav_host).let { controller ->
            val navView: BottomNavigationView = findViewById(R.id.nav_view)

            navView.setupWithNavController(controller)

            // Handles the appBar title
            setupActionBarWithNavController(
                controller,
                AppBarConfiguration(
                    setOf(
                        R.id.navigation_profile,
                        R.id.navigation_nutrition,
                        R.id.navigation_workout,
                        R.id.navigation_exercise,
                        R.id.navigation_settings
                    )
                )
            )
        }
    }
}