package org.cookandroid.hiauction.interfaces

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.cookandroid.hiauction.datas.ItemListResponse
import org.cookandroid.hiauction.datas.ItemRegisterResult
import retrofit2.Call
import retrofit2.http.*

interface ItemService {
    @GET("/api/my/items")
    fun getItems(
        @Query("user_id") u_id: String,
    ): Call<ItemListResponse>

    @Multipart
    @POST("api/main/register")
    fun postItem(
        @Part("user_id") user_id : RequestBody,
        @Part("item_name") item_name : RequestBody,
        @Part("category_id") category_id : Int,
        @Part("start_price") start_price : Int,
        @Part("min_bid_unit") min_bid_unit : Int,
        @Part("immediate_price") immediate_price : Int,
        @Part("description") description: RequestBody,
        @Part("address") address : RequestBody,
        @Part("expired_date") expired_date : RequestBody,
        @Part image : MultipartBody.Part
    ): Call<ItemRegisterResult>
}