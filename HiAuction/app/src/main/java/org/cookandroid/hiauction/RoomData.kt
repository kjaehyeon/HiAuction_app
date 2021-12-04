package org.cookandroid.hiauction

data class RoomData(
    val other_name : String,
    val other_id : String,
    val room_id : Int,
    val item_id : Int,
    val item_name : String,
    val reg_date : String,
    val content : String,
    val address : String,
    val score : Float,
    val img_url: String
)

data class Chatdata(
    val sender_id : String,
    val reg_date : String,
    val content : String,
    val type : Int
)