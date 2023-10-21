package com.example.core_network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class ResponseProcessor @Inject constructor() {

    inline fun <reified T> deserializeToClass(gson: Gson, response: String): T {
        return gson.fromJson(response, T::class.java)
    }

    suspend inline fun <reified R : Any> getResultFromResponse(
        gson: Gson,
        output: HttpResponse
    ): NetworkResult<R> {
        return when (val code = output.status.value) {
            in 200..201 -> NetworkResult.Success(
                data = deserializeToClass(gson, output.body<String>())
            )

            in 300..399 -> NetworkResult.RedirectError(
                e = Exception("Error Redirect"),
                code = code,
                message = "Error Redirect"
            )

            in 400..499 -> NetworkResult.UnAuthorised(
                e = Exception("Error Authentication"),
                code = code,
                message = "UnAuthorised Access"
            )

            in 500..599 -> NetworkResult.ServerError(
                e = Exception("Server Error"),
                code = code,
                message = "Server Error"
            )

            else -> {
                NetworkResult.Exception(Exception())
            }
        }
    }

    suspend inline fun <reified R : Any> getResultFromListResponse(
        gson: Gson,
        output: HttpResponse
    ): NetworkResult<List<R>> {
        return when (val code = output.status.value) {
            in 200..201 -> {
                val list = deserializeToList<R>(gson, output.body<String>().toString())
                NetworkResult.Success(
                    data = list
                )
            }

            in 300..399 -> NetworkResult.RedirectError(
                e = Exception("Error Redirect"),
                code = code,
                message = "Error Redirect"
            )

            in 400..499 -> NetworkResult.UnAuthorised(
                e = Exception("Error Authentication"),
                code = code,
                message = "UnAuthorised Access"
            )

            in 500..599 -> NetworkResult.ServerError(
                e = Exception("Server Error"),
                code = code,
                message = "Server Error"
            )

            else -> {
                NetworkResult.Exception(Exception())
            }
        }
    }

    inline fun <reified R> deserializeToList(gson: Gson, body: String): List<R> {
        return try {
            val listType = object : TypeToken<List<R>>() {}.type
            val result = gson.fromJson<List<R>>(body, listType)
            result
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


}