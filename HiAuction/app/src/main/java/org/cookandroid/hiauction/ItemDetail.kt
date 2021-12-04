package org.cookandroid.hiauction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ItemDetail: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail)

        var intent = intent

        var type = intent.getIntExtra("type", 0)

        when(type) {
            1 -> {} //메인페이지에서 상품 상세페이지 접근
            2 -> {} //마이페이지에서 상품 상세페이지 접근
        }
    }
}

