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

class MyItems : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    var itemsArr = arrayListOf<BidData>(
        BidData("최신 맥북 프로", "대현동", 2500000, LocalDate.now()),
        BidData("아이폰13 프로", "복현동", 1100000, LocalDate.now()),
        BidData("갤럭시s21", "산격동", 780000, LocalDate.now()),
        BidData("갤럭시 워치4", "침산동", 180000, LocalDate.now()),
        BidData("애플 워치6", "대현동", 200000, LocalDate.now())
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myitems)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var myItemList = findViewById<ListView>(R.id.listMyItems)
        var bidAdapter = MyItemListViewAdapter(this, itemsArr)
        myItemList.adapter = bidAdapter

    }

    inner class MyItemListViewAdapter(var context: Context, var itemList: ArrayList<BidData>): BaseAdapter() {
        override fun getCount(): Int {
            return itemList.size
        }

        override fun getItem(position: Int): Any {
            return itemList[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

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
            itemTitle.text = item.title
            itemAddress.text = item.address
            itemDate.text = item.create_date.toString()
            itemPrice.text = item.price.toString() + " 원"
            //TODO("bid state 구분해서 if/else로 코드 구현해야함")


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