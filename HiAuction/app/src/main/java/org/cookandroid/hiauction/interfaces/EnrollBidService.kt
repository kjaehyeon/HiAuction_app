package org.cookandroid.hiauction.interfaces

import org.cookandroid.hiauction.datas.PriceData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface EnrollBidService {
    @FormUrlEncoded
    @POST("api/main/bid")
    fun enrollBid(
        @Field("user_id") user_id: String,
        @Field("price") price: Int,
        @Field("item_id") item_id: Int,
    ): Call<PriceData>

    @FormUrlEncoded
    @POST("api/main/immediate")
    fun enrollIm(
        @Field("user_id") user_id: String,
        @Field("item_id") item_id: Int
    ): Call<PriceData>
}