package org.cookandroid.hiauction

import android.content.Context
import com.bumptech.glide.Glide
import android.content.Intent
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
import org.cookandroid.hiauction.datas.ItemData
import org.cookandroid.hiauction.datas.ItemListResponse
import org.cookandroid.hiauction.interfaces.ItemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyItems : AppCompatActivity() {
    var itemListResponse: ItemListResponse? = null
    @RequiresApi(Build.VERSION_CODES.O)
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
                itemsArr = itemListResponse?.item_list ?: ArrayList()
                var myItemList = findViewById<ListView>(R.id.listMyItems)
                var itemAdapter = MyItemListViewAdapter(this@MyItems, itemsArr!!)
                myItemList.adapter = itemAdapter
            }
        })


    }

    override fun onRestart() {
        super.onRestart()
        finish() //인텐트 종료
        overridePendingTransition(0, 0) //인텐트 효과 없애기
        val intent = getIntent() //인텐트
        intent.putExtra("type", 1)
        startActivity(intent) //액티비티 열기
        overridePendingTransition(0, 0)
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
            Glide.with(this@MyItems).load(item.img_url).into(itemImg)
            when (item.is_end){
                "0" -> {
                    itemState.text = "판매중"
                    var bgShape : GradientDrawable = itemState.background as GradientDrawable
                    bgShape.setColor(resources.getColor(R.color.itemOnSale, null))
                    itemView.setOnClickListener {
                        var intent = Intent(this@MyItems, ItemDetail::class.java)
                        intent.putExtra("type", 1)
                        intent.putExtra("item_id", item.item_id)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                    }
                }
                "1" -> {
                    itemState.text = "낙찰완료"
                    var bgShape : GradientDrawable = itemState.background as GradientDrawable
                    bgShape.setColor(resources.getColor(R.color.bidFinish, null))
                    itemView.setOnClickListener {
                        var intent = Intent(this@MyItems, ItemDetail::class.java)
                        intent.putExtra("type", 3)
                        intent.putExtra("item_type", 1) // 채팅버튼. 거래완료 버튼 표시
                        intent.putExtra("item_id", item.item_id)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                    }

                }
                "2" -> {
                    itemState.text = "기간만료"
                    var bgShape : GradientDrawable = itemState.background as GradientDrawable
                    bgShape.setColor(resources.getColor(R.color.itemExpired, null))
                    itemView.setOnClickListener {
                        var intent = Intent(this@MyItems, ItemDetail::class.java)
                        intent.putExtra("type", 3)
                        intent.putExtra("item_type", 2) // 기간연장버튼 표시
                        intent.putExtra("item_id", item.item_id)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                    }
                }
                "3" -> {
                    itemState.text = "거래완료"
                    var bgShape : GradientDrawable = itemState.background as GradientDrawable
                    bgShape.setColor(resources.getColor(R.color.itemFinish, null))
                    itemView.setOnClickListener {
                        var intent = Intent(this@MyItems, ItemDetail::class.java)
                        intent.putExtra("type", 2)
                        intent.putExtra("bid_type", 2) //버튼 없애고, 낙찰가 표시
                        intent.putExtra("item_id", item.item_id)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                    }

                }
                "4" -> {
                    itemState.text = "거래완료"
                    var bgShape : GradientDrawable = itemState.background as GradientDrawable
                    bgShape.setColor(resources.getColor(R.color.itemFinish, null))
                    itemView.setOnClickListener {
                        var intent = Intent(this@MyItems, ItemDetail::class.java)
                        intent.putExtra("type", 2)
                        intent.putExtra("bid_type", 2) //버튼 없애고, 낙찰가 표시
                        intent.putExtra("item_id", item.item_id)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                    }
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
