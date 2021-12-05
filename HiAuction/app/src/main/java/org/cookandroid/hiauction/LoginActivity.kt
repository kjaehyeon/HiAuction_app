package org.cookandroid.hiauction

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.cookandroid.hiauction.interfaces.LoginAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    lateinit var login : Button
    lateinit var signup : Button
    lateinit var idtext : EditText
    lateinit var pwtext : EditText
    companion object {
        lateinit var prefs : SharedPreferences
        lateinit var addresses : List<String>
    }

    val BASE_URL= "http://192.168.0.17:4000/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(LoginAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = applicationContext.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login = findViewById(R.id.login)
        signup = findViewById(R.id.signup)
        idtext = findViewById(R.id.id)
        pwtext = findViewById(R.id.pw)

        login.setOnClickListener {
            val callPostLogin = api.postLogin(idtext.text.toString(), pwtext.text.toString())
            callPostLogin.enqueue(object : Callback<ResultLogin> {
                override fun onResponse(
                    call: Call<ResultLogin>,
                    response: Response<ResultLogin>
                ) {
                    if(response.isSuccessful) {
                        prefs.edit().putString("id", response.body()?.id).apply()
                        prefs.edit().putString("name", response.body()?.name).apply()
                        addresses = response.body()!!.address
                        var intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        when (response.code()) {
                            400 -> {
                                val dlg: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
                                dlg.setTitle("Message") //제목
                                dlg.setMessage("아이디와 비밀번호를 확인해주세요.") // 메시지
                                dlg.setPositiveButton("닫기",null)
                                dlg.show()
                            }
                            500 ->{
                                val dlg: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
                                dlg.setTitle("Message") //제목
                                dlg.setMessage("죄송합니다. 다시 시도해 주세요.") // 메시지
                                dlg.setPositiveButton("닫기",null)
                                dlg.show()
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<ResultLogin>, t: Throwable) {
                    val dlg: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
                    dlg.setTitle("Message") //제목
                    dlg.setMessage("죄송합니다. 다시 시도해 주세요.") // 메시지
                    dlg.setPositiveButton("닫기", null)
                    dlg.show()
                }
            })
        }

        signup.setOnClickListener {
            var intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            Toast.makeText(applicationContext, "회원가입 성공", Toast.LENGTH_SHORT).show()
        }
    }
}