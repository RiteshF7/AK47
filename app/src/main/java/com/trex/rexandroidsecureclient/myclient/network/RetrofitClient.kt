package com.trex.rexandroidsecureclient.myclient.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trex.rexcommon.data.NewDevice
import com.trex.rexcommon.data.SendMessageDto
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.POST

interface RexKtorServer {
    @POST("/send")
    suspend fun sendMessage(
        @Body body: SendMessageDto,
    ): Response<Unit>

    @POST("/regdevice")
    suspend fun registerNewDevice(
        @Body body: NewDevice,
    ): Response<Unit>
}

object RetrofitClient {
    val moshi =
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory()) // Add this line
            .build()

    //    private const val BASE_URL = "http://10.0.2.2:8080/"
    private const val BASE_URL = "http://192.168.0.165:8080/"

    val builder: RexKtorServer =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
}
