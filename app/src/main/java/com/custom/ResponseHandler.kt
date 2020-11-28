package com.custom

import com.kagroup.baseProject.R
import com.utils.Domain
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class ResponseHandler {
    fun <T : Any> handleSuccess(data: T?): Resource<T> {
        return Resource.success(data)
    }

    fun <T : Any> handleException(e: Exception): Resource<Nothing> {
        return when (e) {
            is HttpException -> {
                when {
                    e.code() == 504 -> {
                        Resource.networkError(
                            Domain.application.getString(R.string.no_internet_message),
                            null
                        )
                    }
                    e.code() == 500 -> {
                        Resource.error(
                            Domain.application.getString(R.string.something_went_wrong),
                            null
                        )
                    }
                    else -> {
                        Resource.error(getErrorMessage(e), null)
                    }
                }
            }
            is SocketTimeoutException,
            is UnknownHostException,
            is UnsatisfiedLinkError,
            is ConnectException -> Resource.networkError(
                Domain.application.getString(R.string.no_internet_message),
                null
            )
            else -> Resource.error(
                Domain.application.getString(R.string.something_went_wrong),
                null
            )
        }
    }

    private fun getErrorMessage(httpException: HttpException): String {
        var errorMessage = Domain.application.getString(R.string.something_went_wrong)
        try {
            val jObjError = JSONObject(httpException.response()!!.errorBody()!!.string())
            errorMessage = jObjError.optString(
                "error",
                Domain.application.getString(R.string.something_went_wrong)
            )
        } catch (e: IOException) {
            e.printStackTrace()
//            errorMessage = Domain.application.getString(R.string.something_went_wrong)
        } finally {
            return errorMessage
        }
    }
}