package com.kagroup.baseProject.core.source.local

import com.google.gson.Gson
import com.kagroup.baseProject.core.User
import com.utils.Constants.USER
import com.utils.preferencesGateway

object UserDataSource {

    fun saveUser(user: User?) {
        preferencesGateway.save(USER, Gson().toJson(user))
    }

    fun getUser(): User? {
        return Gson().fromJson(
            preferencesGateway.load(USER, ""), User::class.java
        )
    }

    fun getToken(): String {
        return if (hasUser()) "Bearer " + getUser()?.token else ""
    }

    fun hasUser(): Boolean {
        val user = Gson().fromJson(
            preferencesGateway.load(USER, ""),
            User::class.java
        )

        if (user?.token != null) {
            return user.token.toString().isNotEmpty()
        }

        return false
    }

}