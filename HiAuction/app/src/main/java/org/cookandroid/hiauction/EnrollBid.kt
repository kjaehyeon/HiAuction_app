package org.cookandroid.hiauction

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import org.cookandroid.hiauction.LoginActivity.Companion.prefs
import org.cookandroid.hiauction.datas.PriceData
import org.cookandroid.hiauction.datas.ResponseData
import org.cookandroid.hiauction.interfaces.EnrollBidService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EnrollBid : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.enrollbid2)
        var retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.17:4000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var enrollBidService: EnrollBidService = retrofit.create(EnrollBidService::class.java)
        var btnBid = findViewById<Button>(R.id.btnBid)
        var seller_id = findViewById<TextView>(R.id.seller)
        var address = findViewById<TextView>(R.id.address)
        var item_name = findViewById<TextView>(R.id.itemname)
        var s_profile : ImageView = findViewById(R.id.S_profile)
        var user_id : String? = prefs.getString("id",null)
        var edtPrice = findViewById<EditText>(R.id.Bidprice)
        var img = findViewById<ImageView>(R.id.Itemimage)
        var curprice = findViewById<TextView>(R.id.curprice)
        var minbidunit = findViewById<TextView>(R.id.min_bid_unit)

        var intent = intent
        var item_id : Int = intent.getIntExtra("Id",0)
        address.text = intent.getStringExtra("address")
        item_name.text = intent.getStringExtra("itemname")
        seller_id.text = intent.getStringExtra("seller")
        var img_url : String? = intent.getStringExtra("img")
        curprice.text = intent.getIntExtra("curPrice", 0).toString()
        minbidunit.text = intent.getIntExtra("minBidUnit",0).toString()


        Glide.with(this@EnrollBid).load("https://avatars.dicebear.com/api/big-smile/"+seller_id.text+".png").into(s_profile)
        Glide.with(this@EnrollBid).load(img_url).into(img)
        btnBid.setOnClickListener {
            if (user_id != null) {
                var price : Int = Integer.parseInt(edtPrice.text.toString())
                enrollBidService.enrollBid(user_id, price, item_id)
                    .enqueue(object : Callback<PriceData> {
                        override fun onFailure(call: Call<PriceData>, t: Throwable) {
                            t.message?.let { Log.e("ITEMREQUSET", it) }
                            var dialog = androidx.appcompat.app.AlertDialog.Builder(this@EnrollBid)
                            dialog.setTitle("에러")
                            dialog.setMessage("호출실패했습니다.")
                            dialog.show()
                        }
                        @RequiresApi(Build.VERSION_CODES.M)
                        override fun onResponse(call: Call<PriceData>, response: Response<PriceData>) {
                            when(response.code()) {
                                200 -> {
                                    Log.i("efef","200")
                                    finish()//인텐트 종료
                                }
                                400 ->{
                                    val dlg: AlertDialog.Builder = AlertDialog.Builder(this@EnrollBid)
                                    var jsonObject : JSONObject = JSONObject(response.errorBody()?.string())
                                    dlg.setPositiveButton("닫기",null)
                                    dlg.show()
                                }
                                500 -> {
                                    var dialog = androidx.appcompat.app.AlertDialog.Builder(this@EnrollBid)
                                    dialog.setMessage("내부적으로 오류가 발생하였습니다.\n잠시 후 다시 시도해주세요")
                                    dialog.setNegativeButton("확인", null)
                                    dialog.show()
                                }
                            }
                        }
                    })
            }
        }
    }
}