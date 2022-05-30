package ru.binnyatoff.githubclient.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ru.binnyatoff.githubclient.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navController: NavController =
            (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment)
                .navController
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }
}