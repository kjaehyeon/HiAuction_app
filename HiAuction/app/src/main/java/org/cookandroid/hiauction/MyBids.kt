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
import org.cookandroid.hiauction.datas.BidData
import org.cookandroid.hiauction.datas.BidListResponse
import org.cookandroid.hiauction.interfaces.BidService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyBids : AppCompatActivity() {
    var bidListResponse: BidListResponse? = null
    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mybids)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var user_id:String? = LoginActivity.prefs.getString("id", null)

        var retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.17:4000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var bidService: BidService = retrofit.create(BidService::class.java)
        var bidsArr : ArrayList<BidData>? = null
        bidService.getBidItems(user_id!!).enqueue(object: Callback<BidListResponse> {
            override fun onFailure(call: Call<BidListResponse>, t: Throwable) {
                t.message?.let { Log.e("BIDREQUSET", it) }
                var dialog = AlertDialog.Builder(this@MyBids)
                dialog.setTitle("??????")
                dialog.setMessage("????????????????????????.")
                dialog.show()
            }

            override fun onResponse(call: Call<BidListResponse>, response: Response<BidListResponse>) {
                bidListResponse = response.body()
                bidsArr = bidListResponse?.bid_list ?: ArrayList()
                var myBidList = findViewById<ListView>(R.id.listMyBids)
                var bidAdapter = MyBidListViewAdapter(this@MyBids, bidsArr!!)
                myBidList.adapter = bidAdapter
            }
        })

    }

    inner class MyBidListViewAdapter(var context: Context, var bidList: ArrayList<BidData>): BaseAdapter() {
        override fun getCount(): Int {
            return bidList.size
        }

        override fun getItem(position: Int): Any {
            return bidList[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var bidView = LayoutInflater.from(context).inflate(R.layout.bid, null)

            var bidImg = bidView.findViewById<ImageView>(R.id.bidImg)
            var bidTitle = bidView.findViewById<TextView>(R.id.bid_title)
            var bidAddress = bidView.findViewById<TextView>(R.id.bid_address)
            var bidState = bidView.findViewById<TextView>(R.id.bid_state)
            var bidPrice = bidView.findViewById<TextView>(R.id.bid_price)
            var bidDate = bidView.findViewById<TextView>(R.id.bid_date)

            var bid = bidList[position]
            bidTitle.text = bid.item_name
            bidAddress.text = bid.address
            bidDate.text = bid.bid_create_date
            bidPrice.text = "????????? " + bid.bid_price.toString() + " ???"
            Glide.with(this@MyBids).load(bid.img_url).into(bidImg)
            when (bid.is_end){
                "0" -> {
                    if (bid.bid_price < bid.item_price){ //?????? ??????
                        bidState.text = "????????????"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.bidFail, null))
                    } else { //?????? ?????? ?????????
                        bidState.text = "????????????"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.itemOnSale, null))
                    }
                    bidView.setOnClickListener {
                        var intent = Intent(this@MyBids, ItemDetail::class.java)
                        intent.putExtra("type", 1)
                        intent.putExtra("item_id", bid.item_id)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                    }
                }
                "1" -> {
                    if (bid.bid_price == bid.item_price){
                        bidState.text = "????????????"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.bidFinish, null))
                        bidView.setOnClickListener {
                            var intent = Intent(this@MyBids, ItemDetail::class.java)
                            intent.putExtra("type", 2)
                            intent.putExtra("bid_type", 1) //???????????? ???????????? ???
                            intent.putExtra("item_id", bid.item_id)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            startActivity(intent)
                        }
                    } else {
                        bidState.text = "????????????"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.bidFail, null))
                        bidView.setOnClickListener {
                            var intent = Intent(this@MyBids, ItemDetail::class.java)
                            intent.putExtra("type", 2)
                            intent.putExtra("bid_type", 2) //?????? ?????????, ????????? ??????
                            intent.putExtra("item_id", bid.item_id)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            startActivity(intent)
                        }
                    }
                    
                }
                "2" -> {
                    bidState.text = "????????????"
                    var bgShape : GradientDrawable = bidState.background as GradientDrawable
                    bgShape.setColor(resources.getColor(R.color.itemExpired, null))
                }
                "3" -> {
                    if (bid.bid_price == bid.item_price) {
                        bidState.text = "???????????? ??????"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.itemReview, null))
                        bidView.setOnClickListener {
                            var intent = Intent(this@MyBids, ItemDetail::class.java)
                            intent.putExtra("type", 2)
                            intent.putExtra("bid_type", 3) //???????????? ?????? ?????????
                            intent.putExtra("item_id", bid.item_id)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            startActivity(intent)
                        }
                    } else {
                        bidState.text = "????????????"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.bidFail, null))
                        bidView.setOnClickListener {
                            var intent = Intent(this@MyBids, ItemDetail::class.java)
                            intent.putExtra("type", 2)
                            intent.putExtra("bid_type", 2) //?????? ?????????, ????????? ??????
                            intent.putExtra("item_id", bid.item_id)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            startActivity(intent)
                        }
                    }


                }
                "4" -> {
                    if (bid.bid_price == bid.item_price){
                        bidState.text = "????????????"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.itemFinish, null))
                    } else {
                        bidState.text = "????????????"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.bidFail, null))
                    }
                    bidView.setOnClickListener {
                        var intent = Intent(this@MyBids, ItemDetail::class.java)
                        intent.putExtra("type", 2)
                        intent.putExtra("bid_type", 2) //?????? ?????????, ????????? ??????
                        intent.putExtra("item_id", bid.item_id)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                    }

                }
            }

            return bidView
        }

    }
    
    
    // ???????????? ???????????? ??????
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
    // ???????????? ?????? ??? ??????????????? ??????
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
    override fun onRestart() {
        super.onRestart()
        finish() //????????? ??????
        overridePendingTransition(0, 0) //????????? ?????? ?????????
        val intent = getIntent() //?????????
        intent.putExtra("type", 1)
        startActivity(intent) //???????????? ??????
        overridePendingTransition(0, 0)
    }
}