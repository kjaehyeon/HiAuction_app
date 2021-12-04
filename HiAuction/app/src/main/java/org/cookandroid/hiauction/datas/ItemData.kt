package org.cookandroid.hiauction.datas

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

class ItemData {
    var item_id : Int = -1
    var item_name: String = ""
    var address: String = ""
    var item_price: Int = 0
    var img_url: String = ""
    //@RequiresApi(Build.VERSION_CODES.O)
    //var create_date : String = LocalDate.now().toString()
    var is_end: String = ""
    @RequiresApi(Build.VERSION_CODES.O)
    var create_date:String = LocalDate.now().toString()

    constructor(item_id: Int, item_name: String, item_price: Int, img_url:String, is_end:String, address: String, create_date: String){
        //TODO("이미지 넣는 거 구현해야함")
        this.item_id = item_id
        this.item_name = item_name
        this.item_price = item_price
        this.img_url = img_url
        this.is_end = is_end
        this.create_date = create_date
        this.address = address
    }
}