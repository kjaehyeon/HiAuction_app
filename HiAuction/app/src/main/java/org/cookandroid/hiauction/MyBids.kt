package org.cookandroid.hiauction

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.time.LocalDate

class MyBids : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    var bidsArr = arrayListOf<BidData>(
        BidData("최신 맥북 프로", "대현동", 2500000, LocalDate.now()),
        BidData("아이폰13 프로", "복현동", 1100000, LocalDate.now()),
        BidData("갤럭시s21", "산격동", 780000, LocalDate.now()),
        BidData("갤럭시 워치4", "침산동", 180000, LocalDate.now()),
        BidData("애플 워치6", "대현동", 200000, LocalDate.now())
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mybids)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        var myBidList = findViewById<ListView>(R.id.listMyBids)
        var bidAdapter = MyBidListViewAdapter(this, bidsArr)
        myBidList.adapter = bidAdapter

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
            bidTitle.text = bid.title
            bidAddress.text = bid.address
            bidDate.text = bid.create_date.toString()
            bidPrice.text = bid.price.toString() + " 원"
            //TODO("bid state 구분해서 if/else로 코드 구현해야함")


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