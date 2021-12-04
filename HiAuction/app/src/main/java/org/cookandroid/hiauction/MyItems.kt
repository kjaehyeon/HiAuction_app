package org.cookandroid.hiauction

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.cookandroid.hiauction.`interface`.BidService
import org.cookandroid.hiauction.`interface`.ItemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate

class MyItems : AppCompatActivity() {
    var itemListResponse:ItemListResponse? = null
    @RequiresApi(Build.VERSION_CODES.O)
//    var itemsArr = arrayListOf<ItemData>(
//        ItemData("최신 맥북 프로", "대현동", 2500000, LocalDate.now()),
//        ItemData("아이폰13 프로", "복현동", 1100000, LocalDate.now()),
//        ItemData("갤럭시s21", "산격동", 780000, LocalDate.now()),
//        ItemData("갤럭시 워치4", "침산동", 180000, LocalDate.now()),
//        ItemData("애플 워치6", "대현동", 200000, LocalDate.now())
//    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myitems)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var user_id:String? = LoginActivity.prefs.getString("id", null)
        var retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.17:4000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var itemService: ItemService = retrofit.create(ItemService::class.java)
        var itemsArr : ArrayList<ItemData>? = null
        Log.d("로그", itemsArr.toString())
        itemService.getItems(user_id!!).enqueue(object: Callback<ItemListResponse> {
            override fun onFailure(call: Call<ItemListResponse>, t: Throwable) {
                t.message?.let { Log.e("BIDREQUSET", it) }
                var dialog = AlertDialog.Builder(this@MyItems)
                dialog.setTitle("에러")
                dialog.setMessage("호출실패했습니다.")
                dialog.show()
            }

            override fun onResponse(call: Call<ItemListResponse>, response: Response<ItemListResponse>) {
                itemListResponse = response.body()
                Log.d("BIDREQUSET","bids : "+itemListResponse?.item_list)
                //var dialog = AlertDialog.Builder(this@MyBids)
                //dialog.setMessage(bidListResponse?.bid_list?.get(1)?.item_name.toString())
                itemsArr = itemListResponse?.item_list ?: ArrayList()
                //Log.i("BidsArr", bidsArr.toString())
                //dialog.show()
                Log.d("is_end", itemsArr!!.get(1).is_end)
                var myItemList = findViewById<ListView>(R.id.listMyItems)
                var itemAdapter = MyItemListViewAdapter(this@MyItems, itemsArr!!)
                myItemList.adapter = itemAdapter
            }
        })


    }

    inner class MyItemListViewAdapter(var context: Context, var itemList: ArrayList<ItemData>): BaseAdapter() {
        override fun getCount(): Int {
            return itemList.size
        }

        override fun getItem(position: Int): Any {
            return itemList[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var itemView = LayoutInflater.from(context).inflate(R.layout.item, null)

            var itemImg = itemView.findViewById<ImageView>(R.id.itemImg)
            var itemTitle = itemView.findViewById<TextView>(R.id.item_title)
            var itemAddress = itemView.findViewById<TextView>(R.id.item_address)
            var itemState = itemView.findViewById<TextView>(R.id.item_state)
            var itemPrice = itemView.findViewById<TextView>(R.id.item_price)
            var itemDate = itemView.findViewById<TextView>(R.id.item_date)

            var item = itemList[position]
            //TODO("이미지 넣는 거 구현해야함")
            itemTitle.text = item.item_name
            itemAddress.text = item.address
            itemDate.text = item.create_date
            itemPrice.text = "현재가 " + item.item_price.toString() + " 원"
            when (item.is_end){
                "0" -> {
                    itemState.text = "판매중"
                    var bgShape : GradientDrawable = itemState.background as GradientDrawable
                    bgShape.setColor(resources.getColor(R.color.itemOnSale, null))

                }
                "1" -> {
                    itemState.text = "낙찰완료"
                    var bgShape : GradientDrawable = itemState.background as GradientDrawable
                    bgShape.setColor(resources.getColor(R.color.bidFinish, null))

                }
                "2" -> {
                    itemState.text = "기간만료"
                    var bgShape : GradientDrawable = itemState.background as GradientDrawable
                    bgShape.setColor(resources.getColor(R.color.itemExpired, null))
                }
                "3" -> {
                    itemState.text = "거래완료"
                    var bgShape : GradientDrawable = itemState.background as GradientDrawable
                    bgShape.setColor(resources.getColor(R.color.itemFinish, null))

                }
                "4" -> {
                    itemState.text = "거래완료"
                    var bgShape : GradientDrawable = itemState.background as GradientDrawable
                    bgShape.setColor(resources.getColor(R.color.itemFinish, null))

                }
            }

            return itemView
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}
