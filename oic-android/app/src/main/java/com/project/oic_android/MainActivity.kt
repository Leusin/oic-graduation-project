package com.project.oic_android

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.oic_android.R.id.*
import com.project.oic_android.databinding.ActivityMainBinding
import com.project.oic_android.modelData.Word
import com.project.oic_android.retrofit.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import kotlin.system.exitProcess

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
        // 유저 로그인 정보 전달
        getUserData()
        // toolbar 변환
        InitNavigationUI()
        // 검색창
        SearchView()
        // 검색 기능
        if (checkInternetConnection(this)) {
            postData()
        } else {
            val alert = AlertDialog.Builder(this)
            alert.setMessage("인터넷 연결을 확인해주세요")
            alert.setPositiveButton("Try again") { Dialog,_ ->
                if(checkInternetConnection(this)){
                    Dialog.dismiss()
                    postData()
                }else{
                    startActivity(Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS))
                    postData()
                }
            }
            alert.setNegativeButton("Exit"){_,_ ->
                this.finish()
                exitProcess(0)
            }
            alert.setCancelable(false)
            alert.show()
        }
    }

    private fun postData() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String): Boolean {
                val word = p0.toString()
                if(!word.equals(null)) {
                    getData(word)

                    Toast.makeText(this@MainActivity,"검색중", Toast.LENGTH_LONG).show()

                    binding.searchView.clearFocus()
                }
                return true
            }
            override fun onQueryTextChange(p0: String): Boolean {
                return true
            }
        })
    }

    // LoginActivity 에서 받은 데이터 Fragment로 전송
    fun getUserData(): String? {
        val method = intent.getStringExtra("method")
        return method
    }

    private fun InitNavigationUI() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            title = when (destination.id) {
                R.id.navigation_search -> "oic"
                else -> "Default title"
            }

            when (destination.id) {
                navigation_search -> {
                    binding.toolbarTitle.text = "단어장"; binding.searchView.visibility =
                        View.VISIBLE; binding.orderSet.visibility = View.GONE
                }
                navigation_note -> {
                    binding.toolbarTitle.text = "단어장"; binding.searchView.visibility =
                        View.GONE; binding.orderSet.visibility = View.VISIBLE
                }
                navigation_account -> {
                    binding.toolbarTitle.text = "내계정"; binding.searchView.visibility =
                        View.GONE; binding.orderSet.visibility = View.GONE
                }

            }
        }
    }

    private fun SearchView() {

        // 예시 데이터터
        val datas =
            arrayListOf("apple", "orange", "banana", "kiwifruit", "apple", "pineapple", "peach")

        // 자동 완성
        val searchAutoComplete: androidx.appcompat.widget.SearchView.SearchAutoComplete =
            binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchAutoComplete.setAdapter(ArrayAdapter(this,
            android.R.layout.simple_list_item_1,
            datas))

        searchAutoComplete.setOnItemClickListener { adapterView, view, itemIndex, id ->
            val query = adapterView.getItemAtPosition(itemIndex) as String
            searchAutoComplete.setText("$query")
            Toast.makeText(this, "$query", Toast.LENGTH_SHORT).show()
        }

    }

    private fun openDetailWordPage(word: Word) {
        val intent = Intent(this, WordDetailActivity::class.java)
        intent.putExtra("data", word)
        startActivity(intent)
    }

    private fun getData(word:String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.getWord(word)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful && response.body() != null) {

                        val responseBody = response.body()!!.listIterator()
                        val next = responseBody.next()



                        openDetailWordPage(Word(
                            R.mipmap.ic_launcher,
                            word,
                            PapagoTranslate(word,"ko", "en"),
                            "example",
                            "https://api.dictionaryapi.dev/media/pronunciations/en/$word-us.mp3",
                            listOf(),
                            listOf()
                        ))

                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "찾을 수 없는 단어 입니다",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }catch (e:HttpException){
                    Toast.makeText(this@MainActivity,"Cannot Reach Server",Toast.LENGTH_LONG).show()
                }catch(e:IOException){
                    Toast.makeText(this@MainActivity,"Check your Internet Connection",Toast.LENGTH_LONG).show()
                }
            }
        }


    }

    private fun PapagoTranslate(word: String, source: String, target: String): String {
        val client_id = "ryX3lv0VHFu2s_Ci1jMl"
        val client_secret = "6vbpG8mH1y"

        val apiUrl = "https://openapi.naver.com/v1/papago/n2mt"

        try {
            URLEncoder.encode(word, "UTF-8")
        }catch (e: UnsupportedEncodingException){
            throw RuntimeException("인코딩 실패", e)
        }

        val requestHeaders: MutableMap<String, String> = HashMap()
        requestHeaders["X-Naver-Client-Id"] = client_id
        requestHeaders["X-Naver-Client-Secret"] = client_secret

        // 기타 구현
        var translatedText: String = "한글 뜻"
        return translatedText
    }

    private fun checkInternetConnection(context: Context):Boolean{
        val connectivityManger=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            val network=connectivityManger.activeNetwork?:return false
            val activityNetwork=connectivityManger.getNetworkCapabilities(network)?:return false
            return when{
                activityNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                activityNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                else->false
            }
        }
        return false
    }
}