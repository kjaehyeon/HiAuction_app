package org.cookandroid.hiauction.datas

class ItemListData {
    var item_id : Int = -1
    var item_name : String = ""
    var current_price : Int = 0
    var immediate_price : Int = 0
    var created_date : Int = 0
    var img_url : String = ""
    constructor(item_id: Int, item_name: String, item_price: Int, img_url:String, is_end:String, address: String, create_date: String){
        //TODO("이미지 넣는 거 구현해야함")
        this.item_id = item_id
        this.item_name = item_name
        this.current_price = current_price
        this.img_url = img_url
        this.immediate_price = immediate_price
        this.created_date = created_date
    }
}