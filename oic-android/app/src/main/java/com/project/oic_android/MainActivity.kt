package com.project.oic_android

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.oic_android.R.id.*
import com.project.oic_android.databinding.ActivityMainBinding
import com.project.oic_android.retrofit.NaverAPI
import com.project.oic_android.modelData.ResultTransferPapago
import com.project.oic_android.modelData.Word
import com.project.oic_android.retrofit.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상태바 색 변경
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.darkblue)
        }

        // 프래그먼트 화면 전환
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        supportFragmentManager.beginTransaction()

        getUserData() // 유저 로그인 정보 전달
        InitNavigationUI() // toolbar 변환
        SearchView() // 검색창
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
                    startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
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
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        val navController = findNavController(nav_host_fragment_activity_main)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            title = when (destination.id) {
                navigation_search -> "oic"
                else -> "Default title"
            }

            when (destination.id) {
                navigation_search -> {
                    binding.toolbarTitle.text = "단어학습"; binding.searchView.visibility =
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
                navigation_ar -> {
                    binding.toolbarTitle.text = "AR"; binding.searchView.visibility = View.GONE
                    binding.orderSet.visibility = View.GONE
                }

            }
        }
    }

    private fun SearchView() {

        // 예시 데이터터
        val datas =
            arrayListOf("apple", "orange", "banana", "kiwifruit", "apple", "pineapple", "peach")

        // 자동 완성
        val searchAutoComplete: SearchView.SearchAutoComplete =
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

    private fun getData(input_word:String){
        val intent = Intent(this, WordDetailActivity::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.getWord(input_word)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful && response.body() != null) {

                        val responseBody = response.body()!!.listIterator()
                        val next = responseBody.next()

//                        openDetailWordPage(Word(
//                            input_word,
//                            TranslateTask(input_word),
                            //"example",
                            //"https://api.dictionaryapi.dev/media/pronunciations/en/$word-us.mp3",
                            //listOf(),
                            //listOf()
//                        ))
                        intent.putExtra("data", input_word)
                        startActivity(intent)

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

    private fun TranslateTask(word: String): String {

        val CLIENT_ID = "U3RduiCODnGBsgFtisU9"
        val CLIENT_SECRET = "mKWeLOuACH"
        val BASE_URL_NAVER_API = "https://openapi.naver.com/"

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_NAVER_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(NaverAPI::class.java)

        val wd = word

        val callPostTransferPapago = api.transferPapago(CLIENT_ID, CLIENT_SECRET,
            "en", "ko", wd)

        var result = ""

        callPostTransferPapago.enqueue(object : Callback<ResultTransferPapago> {
            override fun onResponse(
                call: Call<ResultTransferPapago>,
                response: Response<ResultTransferPapago>
            ) {
                Log.d("TAG", "성공 : ${response.raw()}")
                result = "뜻:" + response.message()
            }

            override fun onFailure(call: Call<ResultTransferPapago>, t: Throwable) {
                Log.d("TAG", "실패 : $t")
                result = "번역 실패"
            }
        })

        return result
    }
/*
    private fun openDetailWordPage(word: Word) {
        val intent = Intent(this, WordDetailActivity::class.java)
        intent.putExtra("data", word.word_eng)
        startActivity(intent)
    }

 */

    private fun checkInternetConnection(context: Context):Boolean{
        val connectivityManger=context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
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
