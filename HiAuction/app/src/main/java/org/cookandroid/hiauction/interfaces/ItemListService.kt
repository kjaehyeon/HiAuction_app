package org.cookandroid.hiauction.interfaces

import org.cookandroid.hiauction.datas.ListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItemListService {
    @GET("/api/main/items")
    fun getItemList(
        @Query("category_id") category_id: Int,
        @Query("address_id") address_id: String
    ): Call<ListResponse>
}