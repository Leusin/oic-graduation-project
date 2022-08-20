package com.project.oic_android

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.SearchView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.project.oic_android.R.id.*
import com.project.oic_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 프래그먼트 화면 전환
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        supportFragmentManager.beginTransaction()

        // toolbar 변환
        InitNavigationUI()

        // 검색창
        SearchView()

    }

    private fun SearchView() {

        // 예시 데이터터
        val datas = arrayListOf("apple", "orange", "banana", "kiwifruit", "apple", "pineapple", "peach")

        // 자동 완성
        val searchAutoComplete: androidx.appcompat.widget.SearchView.SearchAutoComplete =
            binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchAutoComplete.setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_1, datas))

        searchAutoComplete.setOnItemClickListener { adapterView, view, itemIndex, id ->
            val query = adapterView.getItemAtPosition(itemIndex) as String
            searchAutoComplete.setText("$query")
            Toast.makeText(this,"$query",Toast.LENGTH_SHORT).show()
        }

    }

    // LoginActivity 에서 받은 데이터 Fragment로 전송
    fun getData(): String? {
        val method = intent.getStringExtra("method")
        return method
    }
    // 프래그먼트에 따라 toolbar 구성요소 visibility 변화
    private fun InitNavigationUI() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            title = when (destination.id) {
                R.id.navigation_search -> "oic"
                else -> "Default title"
            }

            when (destination.id) {
                navigation_search -> { binding.toolbarTitle.text = "단어장"; binding.searchView.visibility = View.VISIBLE; binding.orderSet.visibility = View.GONE }
                navigation_note -> { binding.toolbarTitle.text = "단어장"; binding.searchView.visibility = View.GONE; binding.orderSet.visibility = View.VISIBLE}
                navigation_account -> {binding.toolbarTitle.text = "내계정"; binding.searchView.visibility = View.GONE; binding.orderSet.visibility = View.GONE}
            }
        }
    }
}