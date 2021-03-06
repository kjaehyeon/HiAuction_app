package org.cookandroid.hiauction

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.cookandroid.hiauction.fragment.NaviHomeFragment

class Category : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var btnCategory0 = findViewById<Button>(R.id.btnCategory0)
        var btnCategory1 = findViewById<LinearLayout>(R.id.btnCategory1)
        var btnCategory2 = findViewById<LinearLayout>(R.id.btnCategory2)
        var btnCategory3 = findViewById<LinearLayout>(R.id.btnCategory3)
        var btnCategory4 = findViewById<LinearLayout>(R.id.btnCategory4)
        var btnCategory5 = findViewById<LinearLayout>(R.id.btnCategory5)
        var btnCategory6 = findViewById<LinearLayout>(R.id.btnCategory6)
        var btnCategory7 = findViewById<LinearLayout>(R.id.btnCategory7)
        var btnCategory8 = findViewById<LinearLayout>(R.id.btnCategory8)
        var btnCategory9 = findViewById<LinearLayout>(R.id.btnCategory9)
        var btnCategory10 = findViewById<LinearLayout>(R.id.btnCategory10)
        var btnCategory11 = findViewById<LinearLayout>(R.id.btnCategory11)
        var btnCategory12 = findViewById<LinearLayout>(R.id.btnCategory12)
        var btnCategory13 = findViewById<LinearLayout>(R.id.btnCategory13)
        var btnCategory14 = findViewById<LinearLayout>(R.id.btnCategory14)
        var btnCategory15 = findViewById<LinearLayout>(R.id.btnCategory15)

        var select = String()
        var outintent = Intent(applicationContext, MainActivity::class.java)//???????????? ?????????
        btnCategory0.setOnClickListener {
            select = "?????? ????????????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",0)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory1.setOnClickListener{
            select = "????????? ??????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",1)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory2.setOnClickListener{
            select = "????????????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",2)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory3.setOnClickListener{
            select = "??????/????????????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",3)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory4.setOnClickListener{
            select = "?????????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",4)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory5.setOnClickListener{
            select = "??????/????????????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",5)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory6.setOnClickListener{
            select = "????????????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",6)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory7.setOnClickListener{
            select = "?????????/??????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",7)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory8.setOnClickListener{
            select = "????????????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",8)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory9.setOnClickListener{
            select = "????????????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",9)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory10.setOnClickListener{
            select = "????????????/??????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",10)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory11.setOnClickListener{
            select = "??????/??????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",11)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory12.setOnClickListener{
            select = "??????/??????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",12)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory13.setOnClickListener{
            select = "????????????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",13)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory14.setOnClickListener{
            select = "??????/??????/??????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",14)
            setResult(Activity.RESULT_OK, outintent)
            finish()
        }
        btnCategory15.setOnClickListener{
            select = "??????"
            outintent.putExtra("category",select)
            outintent.putExtra("category_id",15)
            setResult(Activity.RESULT_OK, outintent)
            finish()
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