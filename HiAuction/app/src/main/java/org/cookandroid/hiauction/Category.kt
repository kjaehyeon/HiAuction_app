package org.cookandroid.hiauction

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.cookandroid.hiauction.fragment.NaviHomeFragment

class Category : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category)
        setSupportActionBar(toolbar)
        var btnCategory1 = findViewById<ImageButton>(R.id.btnCategory1)
        var btnCategory2 = findViewById<ImageButton>(R.id.btnCategory2)
        var btnCategory3 = findViewById<ImageButton>(R.id.btnCategory3)
        var btnCategory4 = findViewById<ImageButton>(R.id.btnCategory4)
        var btnCategory5 = findViewById<ImageButton>(R.id.btnCategory5)
        var btnCategory6 = findViewById<ImageButton>(R.id.btnCategory6)
        var btnCategory7 = findViewById<ImageButton>(R.id.btnCategory7)
        var btnCategory8 = findViewById<ImageButton>(R.id.btnCategory8)
        var btnCategory9 = findViewById<ImageButton>(R.id.btnCategory9)
        var btnCategory10 = findViewById<ImageButton>(R.id.btnCategory10)
        var btnCategory11 = findViewById<ImageButton>(R.id.btnCategory11)
        var btnCategory12 = findViewById<ImageButton>(R.id.btnCategory12)
        var btnCategory13 = findViewById<ImageButton>(R.id.btnCategory13)
        var btnCategory14 = findViewById<ImageButton>(R.id.btnCategory14)
        var btnCategory15 = findViewById<ImageButton>(R.id.btnCategory15)

        var select = String()
        var outintent = Intent(applicationContext, MainActivity::class.java)//액티비티 고치기
        btnCategory1.setOnClickListener{
            select = "디지털 기기"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",1)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory2.setOnClickListener{
            select = "생활가전"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",2)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory3.setOnClickListener{
            select = "가구/인테리어"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",3)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory4.setOnClickListener{
            select = "유아동"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",4)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory5.setOnClickListener{
            select = "생활/가공식품"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",5)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory6.setOnClickListener{
            select = "유아도서"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",6)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory7.setOnClickListener{
            select = "스포츠/레저"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",7)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory8.setOnClickListener{
            select = "여성잡화"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",8)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory9.setOnClickListener{
            select = "여성의류"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",9)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory10.setOnClickListener{
            select = "남성패션/잡화"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",10)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory11.setOnClickListener{
            select = "게임/취미"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",11)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory12.setOnClickListener{
            select = "뷰티/미용"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",12)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory13.setOnClickListener{
            select = "반려동물"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",13)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory14.setOnClickListener{
            select = "도서/티켓/음반"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",14)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory15.setOnClickListener{
            select = "식물"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",15)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
    }
}