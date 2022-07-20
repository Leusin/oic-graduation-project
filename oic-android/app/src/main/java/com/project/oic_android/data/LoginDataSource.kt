package com.project.oic_android.data

import com.project.oic_android.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 * 로그인 자격 증명을 포함한 인증을 처리하고 사용자 정보를 검색하는 클래스
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}