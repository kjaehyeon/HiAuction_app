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

    //@FormUrlEncoded
    @POST("api/user/signup")
    fun postSignUp(
        @Body signUpBody: SignUpBody
    ): Call<ResultLogin>
}

