package org.cookandroid.hiauction


import retrofit2.Call
import retrofit2.http.*

interface LoginAPI {

    @FormUrlEncoded
    @POST("api/user/signin")
    fun postLogin(
        @Field("id") id: String,
        @Field("password") password: String,
    ): Call<ResultLogin>

    @FormUrlEncoded
    @POST("api/user/signup")
    fun postSignUp(
        @Field("id") id: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("address") address: List<Int>,
        @Field("tel") tel: String,
        @Field("description") description: String,
        @Field("name") name: String
    ): Call<ResultLogin>
}

