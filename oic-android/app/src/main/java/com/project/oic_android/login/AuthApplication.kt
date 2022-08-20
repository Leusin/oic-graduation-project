package com.project.oic_android.login

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class AuthApplication : MultiDexApplication() {
    companion object {
        lateinit var auth: FirebaseAuth
        var email: String? = null
        fun checkAuth(): Boolean {  // 리턴 값 true/false 에 따라 인증 여부 확인
            val currentUser = auth.currentUser
            return currentUser?.let {
                email = currentUser.email
                currentUser.isEmailVerified
            } ?: let { false }
        }
    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth  // auth 객체 초기화
    }
}