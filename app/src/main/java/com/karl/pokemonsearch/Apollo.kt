package com.karl.pokemonsearch

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

private class AuthorizationInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .build()
        return chain.proceed(request)
    }
}

val apolloClient = ApolloClient.Builder()
    .serverUrl("https://beta.pokeapi.co/graphql/v1beta")
    .okHttpClient(
        OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .build()
    )
    .build()
