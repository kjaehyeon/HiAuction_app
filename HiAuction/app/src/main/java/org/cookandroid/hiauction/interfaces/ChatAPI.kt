package org.cookandroid.hiauction.interfaces

import org.cookandroid.hiauction.datas.Chatdata
import org.cookandroid.hiauction.datas.RoomData
import retrofit2.Call
import retrofit2.http.*

interface ChatAPI {

    @GET("api/chat/rooms")
    fun getChatRooms(
        @Query("user_id") id: String,
    ): Call<List<RoomData>>

    @GET("api/chat/chats")
    fun getChatlist(
        @Query("room_id") room_id : Int
    ): Call<List<Chatdata>>
}