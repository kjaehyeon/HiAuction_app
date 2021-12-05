package org.cookandroid.hiauction.interfaces

import org.cookandroid.hiauction.datas.DeleteUserResponse
import org.cookandroid.hiauction.fragment.ModifyUserResponse
import retrofit2.Call
import retrofit2.http.*

interface ModifyUserService {
    @FormUrlEncoded
    @PUT("/api/my/info")
    fun modifyUserInfo(
        @Field("user_id") user_id : String,
        @Field("current_password") current_password: String,
        @Field("new_password") new_password: String,
        @Field("email") email: String,
        @Field("description") description: String,
    ): Call<ModifyUserResponse>

    @DELETE("api/my/user")
    fun deleteUser(
        @Query("user_id") user_id : String,
    ): Call<DeleteUserResponse>
}