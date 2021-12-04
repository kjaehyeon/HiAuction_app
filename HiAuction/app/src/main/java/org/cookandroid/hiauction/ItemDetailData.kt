package org.cookandroid.hiauction

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

class ItemDetailData//TODO("이미지 넣는 거 구현해야함")
    (
    var seller_id: String,
    var seller_name: String,
    var seller_rate: Float,
    var address: String,
    var item_id: Int,
    var item_name: String,
    var immediate_price: Int,
    var current_price: Int,
    @RequiresApi(Build.VERSION_CODES.O) var create_date: String,
    @RequiresApi(Build.VERSION_CODES.O) var expire_date: String,
    var description: String,
    var img_url: String
) {
    //@RequiresApi(Build.VERSION_CODES.O)
    //var create_date : String = LocalDate.now().toString()

}