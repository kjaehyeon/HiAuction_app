package org.cookandroid.hiauction.interfaces

import org.cookandroid.hiauction.datas.BidListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BidService {
    @GET("/api/my/bids")
    fun getBidItems(
        //@Header("X-Naver-Client-Id") clientId: String,
        //@Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("user_id") u_id: String,
    ): Call<BidListResponse>
}