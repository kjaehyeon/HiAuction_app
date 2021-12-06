package org.cookandroid.hiauction.interfaces


import org.cookandroid.hiauction.ResultLogin
import org.cookandroid.hiauction.SignUpBody
import retrofit2.Call
import retrofit2.http.*

interface LoginAPI {

    @FormUrlEncoded
    @POST("api/user/signin")
    fun postLogin(
        @Field("id") id: String,
        @Field("password") password: String,
    ): Call<ResultLogin>

    @POST("api/user/signup")
    fun postSignUp(
        @Body signUpBody: SignUpBody
    ): Call<ResultLogin>
}

