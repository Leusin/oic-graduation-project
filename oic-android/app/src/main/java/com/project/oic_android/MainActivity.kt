package com.project.oic_android

import android.os.Bundle
import android.view.Menu
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.project.oic_android.databinding.ActivityMainBinding
import com.project.oic_android.ui.account.AccountFragment
import com.project.oic_android.ui.note.NoteFragment
import com.project.oic_android.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 프래그먼트 화면 전환
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // 타이틀
        //val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_search, R.id.navigation_note, R.id.navigation_account))
        //findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController, appBarConfiguration)
        //setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        supportFragmentManager.beginTransaction()

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_search -> { binding.toolbarTitle.text = "검색"}
                R.id.navigation_note -> { binding.toolbarTitle.text = "단어장"}
                R.id.navigation_account -> { binding.toolbarTitle.text = "내계정"}
            }
           true
        }

        // UI 초기화
        navView.apply {
            setOnItemSelectedListener { item ->
                val action: Unit = when (item.itemId) {
                    R.id.navigation_search -> { binding.toolbarTitle.text = "검색"; binding.searchView.visibility = View.VISIBLE; binding.orderSet.visibility = View.GONE}
                    R.id.navigation_note -> { binding.toolbarTitle.text = "단어장"; binding.searchView.visibility = View.GONE; binding.orderSet.visibility = View.VISIBLE}
                    R.id.navigation_account -> { binding.toolbarTitle.text = "내계정"; binding.searchView.visibility = View.GONE; binding.orderSet.visibility = View.GONE}
                    else -> return@setOnItemSelectedListener true
                }
                true
            }
        }
    }
}