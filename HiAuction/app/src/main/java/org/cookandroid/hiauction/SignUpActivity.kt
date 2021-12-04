package org.cookandroid.hiauction

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity() {
    lateinit var id : EditText
    lateinit var pw : EditText
    lateinit var email : EditText
    lateinit var tel : EditText
    lateinit var name : EditText
    lateinit var description : EditText
    lateinit var mytown : Button
    lateinit var submit : Button

    val BASE_URL= "http://192.168.0.17:4000/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(LoginAPI::class.java)
    var addressList : List<Int>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        id = findViewById(R.id.id)
        pw = findViewById(R.id.pw)
        email = findViewById(R.id.email)
        tel = findViewById(R.id.tel)
        name = findViewById(R.id.name)
        description = findViewById(R.id.description)
        mytown = findViewById(R.id.mytown)
        submit = findViewById(R.id.submit)

        mytown.setOnClickListener {
            var intent = Intent(applicationContext, MyTownActivity::class.java)
            startActivityForResult(intent, 1)
        }

        submit.setOnClickListener {
            if(id.text != null && pw.text != null && email.text != null && tel.text != null
                && name.text != null && description.text != null && !addressList.isNullOrEmpty()){
                val callPostSignup = api.postSignUp(id =id.text.toString(), password=pw.text.toString(), email=email.text.toString(),
                    tel=tel.text.toString(),name=name.text.toString(), description=description.text.toString(), address = addressList!!)
                callPostSignup.enqueue(object : Callback<ResultLogin> {
                    override fun onResponse(
                        call: Call<ResultLogin>,
                        response: Response<ResultLogin>
                    ) {
                        if(response.isSuccessful) {
                            LoginActivity.prefs.edit().putString("id", response.body()?.id).apply()
                            LoginActivity.prefs.edit().putString("name", response.body()?.name).apply()
                            var intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        }
                        else {
                            when (response.code()) {
                                400 -> {
                                    val dlg: AlertDialog.Builder = AlertDialog.Builder(this@SignUpActivity)
                                    dlg.setTitle("Message") //제목
                                    dlg.setMessage("아이디와 비밀번호를 확인해주세요.") // 메시지
                                    dlg.setPositiveButton("닫기",null)
                                    dlg.show()
                                }
                                500 ->{
                                    val dlg: AlertDialog.Builder = AlertDialog.Builder(this@SignUpActivity)
                                    dlg.setTitle("Message") //제목
                                    dlg.setMessage("죄송합니다. 다시 시도해 주세요.") // 메시지
                                    dlg.setPositiveButton("닫기",null)
                                    dlg.show()
                                }
                            }
                        }
                    }
                    override fun onFailure(call: Call<ResultLogin>, t: Throwable) {
                        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@SignUpActivity)
                        dlg.setTitle("Message") //제목
                        dlg.setMessage("죄송합니다. 다시 시도해 주세요.") // 메시지
                        dlg.setPositiveButton("닫기", null)
                        dlg.show()
                    }
                })
            }else{
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this@SignUpActivity)
                dlg.setTitle("Message") //제목
                dlg.setMessage("양식을 모두 완성해 주세요") // 메시지
                dlg.setPositiveButton("닫기", null)
                dlg.show()
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                var intent = Intent(applicationContext, LoginActivity::class.java)
                intent.putExtra("result", "ok")
                setResult(RESULT_OK, intent)
                finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
        }
    }
}