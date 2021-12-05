package org.cookandroid.hiauction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView

class ItemList : AppCompatActivity() {
    var ItemList = arrayListOf<Item>( //고치기
        Item(1,"Auction2", "대현동", "165,000", "140,000",1),
        Item(2,"Auction3", "대현동", "123,000","100,000",2)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itemlist)

        var edtSearch = findViewById<EditText>(R.id.edtSearch) //검색창
        var Adapter = ListAdapter(this, ItemList)
        var card = findViewById<CardView>(R.id.cardview)
        var btnCategory = findViewById<Button>(R.id.btnCategory) //카테고리 설정 버튼
        var btnWrite = findViewById<ImageButton>(R.id.btnWrite) //물품 등록 버튼
        var Location = findViewById<TextView>(R.id.Location) //사용자 주소

        var search = edtSearch.toString() // 검색 내용
        var categoryintent = Intent(applicationContext, Category::class.java)
        var regintent = Intent(applicationContext, ItemRegister::class.java)

        if(categoryintent.hasExtra("category")){
            btnCategory.text = categoryintent.getStringExtra("category")
        }

        btnCategory.setOnClickListener{
            startActivity(categoryintent)
        }

        btnWrite.setOnClickListener{
            startActivity(regintent)
        }

        val lv = findViewById<View>(R.id.lv) as ListView
        lv.adapter = Adapter
    }
}