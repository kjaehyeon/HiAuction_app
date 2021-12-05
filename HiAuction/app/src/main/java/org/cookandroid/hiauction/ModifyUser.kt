package org.cookandroid.hiauction

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.cookandroid.hiauction.interfaces.ModifyUserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.cookandroid.hiauction.LoginActivity.Companion.prefs
import org.cookandroid.hiauction.fragment.ModifyUserResponse

class ModifyUser : AppCompatActivity() {
    var modifyUserResponse: ModifyUserResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modify_user)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //회원정보수정
        var btnModifyUser = findViewById<Button>(R.id.btnModifyUser)
        btnModifyUser.setOnClickListener {
            var currentPass = findViewById<TextView>(R.id.edtCurrentPass)
            var new_pass = findViewById<TextView>(R.id.edtPass)
            var confirm_pass = findViewById<TextView>(R.id.edtPassConfirm)
            var email = findViewById<TextView>(R.id.edtEmail)
            var describe = findViewById<TextView>(R.id.edtDescription)
            if (!new_pass.text.toString().equals(confirm_pass.text.toString())){
                var dlg = AlertDialog.Builder(this)
                dlg.setTitle("회원정보 수정 오류")
                dlg.setMessage("비밀번호가 일치하지 않습니다")
                dlg.setNegativeButton("확인", null)
                dlg.show()
            } else {
                var retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.0.17:4000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                var modifyUserService: ModifyUserService = retrofit.create(ModifyUserService::class.java)
                //@TO_DO("user id preference에서 받아와야함")
                var user_id:String? = prefs.getString("id", null)
                modifyUserService.modifyUserInfo(user_id!!, currentPass.text.toString(), new_pass.text.toString(), email.text.toString(), describe.text.toString()).enqueue(object: Callback<ModifyUserResponse> {
                    override fun onFailure(call: Call<ModifyUserResponse>, t: Throwable) {0
                        t.message?.let { Log.e("BIDREQUSET", it) }
                        var dialog = AlertDialog.Builder(this@ModifyUser)
                        dialog.setTitle("에러")
                        dialog.setMessage("호출실패했습니다.")
                        dialog.show()
                    }

                    override fun onResponse(call: Call<ModifyUserResponse>, response: Response<ModifyUserResponse>) {
                        modifyUserResponse = response.body()
                        var message = modifyUserResponse?.message
                        if (response.code() == 400){
                            var dialog = AlertDialog.Builder(this@ModifyUser)
                            dialog.setTitle("회원정보 수정 오류")
                            dialog.setMessage("현재 비밀번호가 틀립니다")
                            dialog.setNegativeButton("확인", null)
                            dialog.show()
                        } else if (response.code() == 500) {
                            var dialog = AlertDialog.Builder(this@ModifyUser)
                            dialog.setTitle("회원정보 수정 오류")
                            dialog.setMessage("오류가 발생했습니다")
                            dialog.setNegativeButton("확인", null)
                            dialog.show()
                        } else if (response.code() == 200) {
                            var dialog = AlertDialog.Builder(this@ModifyUser)
                            dialog.setTitle("회원정보 수정")
                            dialog.setMessage("수정했습니다")
                            dialog.setNegativeButton("확인" ){ dialog, which ->
                                finish()
                            }
                            dialog.show()
                        }
                    }
                })
            }
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