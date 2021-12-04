package org.cookandroid.hiauction

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.util.*


class BidData {
    var it_id : Int = -1
    var title: String = ""
    var address: String = ""
    var price: Int = 0
    @RequiresApi(Build.VERSION_CODES.O)
    var create_date : LocalDate = LocalDate.now()
    var u_id : String = ""

    constructor(title: String, address: String, price: Int, create_date: LocalDate ){
        //TODO("이미지 넣는 거 구현해야함")
        this.title = title
        this.address = address
        this.price = price
        this.create_date = create_date
    }
}