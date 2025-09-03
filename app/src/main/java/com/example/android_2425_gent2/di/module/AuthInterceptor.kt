package com.example.android_2425_gent2.di.module

import com.example.android_2425_gent2.data.repository.APIResource
import com.example.android_2425_gent2.data.repository.auth.IAuthRepo
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authRepo: IAuthRepo) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val credentialsResponse = runBlocking {
            authRepo.getStoredCredentials().toList().last()
        }

        var request = chain.request().newBuilder()

        if (credentialsResponse is APIResource.Success) {
            val accessToken = credentialsResponse.data?.accessToken
            val type = credentialsResponse.data?.type

            if (accessToken != null)
                request = request.addHeader(
                    "Authorization",
                    "$type $accessToken"
                )
        }

        return chain.proceed(request.build())
    }
}
