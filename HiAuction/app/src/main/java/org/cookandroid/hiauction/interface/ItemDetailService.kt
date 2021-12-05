package org.cookandroid.hiauction.`interface`

import org.cookandroid.hiauction.*
import retrofit2.Call
import retrofit2.http.*

interface ItemDetailService {
    @GET("/api/main/item")
    fun getItem(
        @Query("item_id") item_id: Int,
    ): Call<ItemDetailData>

    // 거래완료
    @FormUrlEncoded
    @PUT("api/my/item/completion")
    fun completeItem(
        @Field("item_id") item_id: Int,
    ): Call<ResponseData>

    // 기간연장(변경)
    @FormUrlEncoded
    @PUT("api/my/item/extension")
    fun modifyExpireDate(
        @Field("item_id") item_id: Int,
        @Field("expired_date") expired_date :String,
    ): Call<ResponseData>

    // 상품삭제
    @DELETE("api/my/item")
    fun deleteItem(
        @Query("item_id") item_id: Int,
    ): Call<ResponseData>

    // 후기등록
    @FormUrlEncoded
    @POST("api/my/rating")
    fun enrollRating(
        @Field("seller_id") seller_id: String,
        @Field("buyer_id") buyer_id: String,
        @Field("score") score :Float,
        @Field("description") description: String,
        @Field("item_id") item_id: Int,
    ): Call<ResponseData>

    // 채팅방 번호 받기
    @GET("api/my/room")
    fun getChatRoom(
        @Query("item_id") item_id: Int,
    ): Call<roomData>
}