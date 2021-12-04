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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate

class MyBids : AppCompatActivity() {
    var bidListResponse:BidListResponse? = null
    @RequiresApi(Build.VERSION_CODES.O)
//    var bidsArr = arrayListOf<BidData>(
//        BidData(1, "최신 맥북 프로", 2500000, "","0", "대현동", 1, 2600000, "2021-06-21"),
//        BidData(1, "최신 맥북 프로", 2500000, "","1", "대현동", 1, 2600000, "2021-06-21"),
//        BidData(1, "최신 맥북 프로", 2500000, "","2", "대현동", 1, 2600000, "2021-06-21"),
//        BidData(1, "최신 맥북 프로", 2500000, "","3", "대현동", 1, 2600000, "2021-06-21"),
//        BidData(1, "최신 맥북 프로", 2500000, "","4", "대현동", 1, 2600000, "2021-06-21"),
//    )

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
        Log.d("로그", bidsArr.toString())
        bidService.getBidItems(user_id!!).enqueue(object: Callback<BidListResponse> {
            override fun onFailure(call: Call<BidListResponse>, t: Throwable) {
                t.message?.let { Log.e("BIDREQUSET", it) }
                var dialog = AlertDialog.Builder(this@MyBids)
                dialog.setTitle("에러")
                dialog.setMessage("호출실패했습니다.")
                dialog.show()
            }

            override fun onResponse(call: Call<BidListResponse>, response: Response<BidListResponse>) {
                bidListResponse = response.body()
                Log.d("BIDREQUSET","bids : "+bidListResponse?.bid_list)
                //var dialog = AlertDialog.Builder(this@MyBids)
                //dialog.setMessage(bidListResponse?.bid_list?.get(1)?.item_name.toString())
                bidsArr = bidListResponse?.bid_list ?: ArrayList()
                //Log.i("BidsArr", bidsArr.toString())
                //dialog.show()
                Log.d("is_end", bidsArr!!.get(1).is_end)
                var myBidList = findViewById<ListView>(R.id.listMyBids)
                var bidAdapter = MyBidListViewAdapter(this@MyBids, bidsArr!!)
                myBidList.adapter = bidAdapter
            }
        })
        Log.d("로그", bidsArr.toString())
//        var myBidList = findViewById<ListView>(R.id.listMyBids)
//        var bidAdapter = bidsArr?.let { MyBidListViewAdapter(this, it) }
//        myBidList.adapter = bidAdapter

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
            //TODO("이미지 넣는 거 구현해야함")
            bidTitle.text = bid.item_name
            bidAddress.text = bid.address
            bidDate.text = bid.bid_create_date
            bidPrice.text = "입찰가 " + bid.bid_price.toString() + " 원"
            Log.d("is_end", bid.is_end)
            //TODO("bid state 구분해서 if/else로 코드 구현해야함")
            when (bid.is_end){
                "0" -> {
//                    bidState.text = "판매중"
//                    bidState.setBackgroundResource(R.drawable.border_onsale)
//                    bidState.setPadding(10, 5, 10, 5)
                    if (bid.bid_price < bid.item_price){ //낙찰 실패
                        bidState.text = "낙찰실패"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.bidFail, null))
                    } else { //현재 내가 최고가
                        bidState.text = "낙찰유력"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.itemOnSale, null))
                    }
                }
                "1" -> {
                    //TODO("bid price랑 item currentprice 비교해서 낙찰완료 구현해야함")
//                    bidState.setBackgroundResource(R.drawable.border_bidfinish)
//                    bidState.setPadding(10, 5, 10, 5)
                    if (bid.bid_price == bid.item_price){
                        bidState.text = "낙찰완료"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.bidFinish, null))
                    } else {
                        bidState.text = "낙찰실패"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.bidFail, null))
                    }
                }
                "2" -> {
                    bidState.text = "기간만료"
                    var bgShape : GradientDrawable = bidState.background as GradientDrawable
                    bgShape.setColor(resources.getColor(R.color.itemExpired, null))
                }
                "3" -> {
                    if (bid.bid_price == bid.item_price) {
                        bidState.text = "후기등록 필요"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.itemReview, null))
                    } else {
                        bidState.text = "낙찰실패"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.bidFail, null))
                    }

                }
                "4" -> {

                    //TODO("bid price랑 item currentprice 비교해서 낙찰완료 구현해야함")
                    if (bid.bid_price == bid.item_price){
                        bidState.text = "거래완료"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.itemFinish, null))
                    } else {
                        bidState.text = "낙찰실패"
                        var bgShape : GradientDrawable = bidState.background as GradientDrawable
                        bgShape.setColor(resources.getColor(R.color.bidFail, null))
                    }

                }
            }

            return bidView
        }

    }
    
    
    // 액션바에 뒤로가기 버튼
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
    // 뒤로가기 했을 때 애니메이션 없앰
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}