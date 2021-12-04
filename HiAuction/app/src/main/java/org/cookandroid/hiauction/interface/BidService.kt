package org.cookandroid.hiauction.`interface`

import org.cookandroid.hiauction.BidListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface BidService {
    @GET("/api/my/bids")
    fun getBidItems(
        //@Header("X-Naver-Client-Id") clientId: String,
        //@Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("user_id") u_id: String,
    ): Call<BidListResponse>
}