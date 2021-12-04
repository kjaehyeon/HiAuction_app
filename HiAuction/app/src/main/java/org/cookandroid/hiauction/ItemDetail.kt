package org.cookandroid.hiauction

import android.content.ClipData
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.cookandroid.hiauction.`interface`.BidService
import org.cookandroid.hiauction.`interface`.ItemDetailService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItemDetail: AppCompatActivity() {
    var itemDetailResponse:ItemDetailResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail)

        var intent = intent

        var type = intent.getIntExtra("type", 0)
        var itemId = intent.getIntExtra("item_id", -1)

        if (itemId != -1) {
            var retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.17:4000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            var itemDetailService: ItemDetailService = retrofit.create(ItemDetailService::class.java)

            itemDetailService.getItem(itemId!!).enqueue(object: Callback<ItemDetailResponse> {
                override fun onFailure(call: Call<ItemDetailResponse>, t: Throwable) {
                    t.message?.let { Log.e("ITEMREQUSET", it) }
                    var dialog = AlertDialog.Builder(this@ItemDetail)
                    dialog.setTitle("에러")
                    dialog.setMessage("호출실패했습니다.")
                    dialog.show()
                }

                override fun onResponse(call: Call<ItemDetailResponse>, response: Response<ItemDetailResponse>) {
                    itemDetailResponse = response.body()
                    //var dialog = AlertDialog.Builder(this@MyBids)
                    //dialog.setMessage(bidListResponse?.bid_list?.get(1)?.item_name.toString())
                    var item = itemDetailResponse?.item
                    //Log.i("BidsArr", bidsArr.toString())
                    //dialog.show()
                    when(type) {
                        //메인페이지에서 상품 상세페이지 접근
                        1 -> {}
                        //마이페이지 내 입찰목록에서 상품 상세페이지 접근
                        2 -> {
                            var bid_type = intent.getIntExtra("bid_type", 0)
                        }
                        //마이페이지 내가 등록한 상품에서 상품 상세페이지 접근
                        3 -> {
                            var item_type = intent.getIntExtra("item_type", 0)
                        }
                    }
                }
            })
        }



    }
}