package com.kagroup.baseProject.core.responses

import com.google.gson.annotations.SerializedName

open class BaseResponse<T> {

    @SerializedName("code")
    var code: Int = 0
    @SerializedName("error")
    var error: String = ""
    @SerializedName("message")
    var errorMessage: String = ""
    @SerializedName("data")
    var data: T? = null


    fun isSuccess(): Boolean {
        return code == 200 //&& error.isEmpty()
    }
}
