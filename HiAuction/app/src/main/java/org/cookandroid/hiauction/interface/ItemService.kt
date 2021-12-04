package org.cookandroid.hiauction.`interface`

import org.cookandroid.hiauction.BidListResponse
import org.cookandroid.hiauction.ItemListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ItemService {
    @GET("/api/my/items")
    fun getItems(
        @Query("user_id") u_id: String,
    ): Call<ItemListResponse>
}