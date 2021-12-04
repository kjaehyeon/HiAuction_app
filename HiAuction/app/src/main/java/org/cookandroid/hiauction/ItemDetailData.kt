package org.cookandroid.hiauction

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

class ItemDetailData {
    var seller_id :String = ""
    var seller_name: String = ""
    var seller_rate: Float = 0.0f
    var address: String = ""
    var item_id : Int = -1
    var item_name: String = ""
    var immediate_price:Int = 0
    var current_price:Int = 0
    @RequiresApi(Build.VERSION_CODES.O)
    var create_date:String = LocalDate.now().toString()
    @RequiresApi(Build.VERSION_CODES.O)
    var expire_date:String = LocalDate.now().toString()
    var description:String = ""
    var img_url: String = ""
    //@RequiresApi(Build.VERSION_CODES.O)
    //var create_date : String = LocalDate.now().toString()

    constructor(seller_id :String, seller_name: String, seller_rate: Float, address: String, item_id: Int, item_name: String,
                immediate_price:Int, current_price:Int, create_date:String, expire_date:String, description:String, img_url:String){
        //TODO("이미지 넣는 거 구현해야함")
        this.seller_id = seller_id
        this.seller_name = seller_name
        this.seller_rate = seller_rate
        this.address = address
        this.item_id = item_id
        this.item_name = item_name
        this.immediate_price = immediate_price
        this.current_price = current_price
        this.create_date = create_date
        this.expire_date = expire_date
        this.description = description
        this.img_url = img_url
    }
}