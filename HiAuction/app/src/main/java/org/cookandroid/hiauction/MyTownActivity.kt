package org.cookandroid.hiauction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class MyTownActivity : AppCompatActivity() {
    private lateinit var mytownList : ListView
    lateinit var townSubmit : Button

    var townArr = arrayOf("동인동", "삼덕동", "성내1동", "성내2동", "성내3동", "대신동", "남산1~4동", "대봉1~2동", "신암1~5동",
        "신천1~2동", "신천3~4동", "효목1~2동", "도평동", "불로봉무동", "지저동", "동촌동", "방촌동", "해안동", "안심1동", "안심2동", "안심3동", "안심4동", "혁신동", "공산동",
        "내당1동", "내당2~3동", "내당4동", "비산1동", "비산2~3동", "비산4~7동", "평리1~6동", "상중이동", "원대동", "이천동", "봉덕1~3동", "대명1~6동", "대명9~10동", "고성동", "칠성동",
        "침산1~3동", "산격1~4동", "대현동", "복현1~2동", "검단동", "무대조야동", "관문동", "태전1~2동", "구암동", "관음동", "읍내동", "동천동", "노원동", "국우동", "범어1~4동", "만촌1~3동", "수성1가동", "수성2~3가동", "수성4가동", "황금1~2동", "중동", "상동", "파동", "두산동", "지산1~2동", "범물1~2동", "고산1동", "고상2동",
        "고산3동", "성당동", "두류1~3동", "본리동", "감삼동", "죽전동", "장기동", "용산1~2동", "이곡1~2동", "신당동", "월성1~2동", "진천동", "상인1~3동", "도원동", "송현1동", "송현2동",
        "본동", "화원읍", "논공읍", "다사읍", "유가읍", "옥포읍", "현풍읍", "가창면", "하빈면", "구지면")
    var checkedId : ArrayList<Int> = ArrayList()
    var checkedName : ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_town)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        townSubmit = findViewById(R.id.townSubmit)
        mytownList = findViewById(R.id.townlist)
        var adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, townArr)
        mytownList.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        mytownList.adapter = adapter

        townSubmit.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            intent.putIntegerArrayListExtra("addressId", checkedId)
            intent.putStringArrayListExtra("addressName", checkedName)
            setResult(RESULT_OK, intent)
            finish()
        }
        mytownList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if(checkedId.contains(position+1)){
                checkedId.remove(position+1)
                checkedName.remove(parent.getItemAtPosition(position).toString())
            }else{
                checkedId.add(position+1)
                checkedName.add(parent.getItemAtPosition(position).toString())
            }
        }


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                var intent = Intent(applicationContext, LoginActivity::class.java)
                intent.putExtra("result", "ok")
                setResult(RESULT_CANCELED, intent)
                finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}