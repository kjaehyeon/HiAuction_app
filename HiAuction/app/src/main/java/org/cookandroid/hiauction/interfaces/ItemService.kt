package org.cookandroid.hiauction.interfaces

import org.cookandroid.hiauction.datas.ItemListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItemService {
    @GET("/api/my/items")
    fun getItems(
        @Query("user_id") u_id: String,
    ): Call<ItemListResponse>
}