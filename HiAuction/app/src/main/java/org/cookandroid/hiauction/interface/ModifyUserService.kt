package org.cookandroid.hiauction.`interface`

import org.cookandroid.hiauction.BidListResponse
import org.cookandroid.hiauction.ItemListResponse
import org.cookandroid.hiauction.ModifyUserResponse
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
}