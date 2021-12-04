package org.cookandroid.hiauction.`interface`

import org.cookandroid.hiauction.BidListResponse
import org.cookandroid.hiauction.ItemDetailResponse
import org.cookandroid.hiauction.ItemListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ItemDetailService {
    @GET("/api/main/item")
    fun getItem(
        @Query("item_id") item_id: Int,
    ): Call<ItemDetailResponse>
}