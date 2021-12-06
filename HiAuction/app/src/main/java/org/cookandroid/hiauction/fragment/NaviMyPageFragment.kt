package org.cookandroid.hiauction.fragment


import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import org.cookandroid.hiauction.*
import org.cookandroid.hiauction.LoginActivity.Companion.addresses
import org.cookandroid.hiauction.datas.DeleteUserResponse
import org.cookandroid.hiauction.interfaces.ModifyUserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class NaviMyPageFragment : Fragment() {
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity = context as MainActivity
        var view = inflater.inflate(R.layout.mypage_main, container, false)
        setHasOptionsMenu(true)
        var user_id:String? = LoginActivity.prefs.getString("id", null)
        var user_name:String? = LoginActivity.prefs.getString("name", null)
        var addressCat:String = ""
        for (address in addresses)
        {
            addressCat += address
            addressCat += " | "
        }
        var userName = view.findViewById<TextView>(R.id.userName)
        userName.text = user_name
        var userAddress = view.findViewById<TextView>(R.id.userAddress)
        userAddress.text = addressCat
        var userId = view.findViewById<TextView>(R.id.userId)
        userId.text = "@" + user_id
        var btnModifyUser = view.findViewById<RelativeLayout>(R.id.btnModifyUser)
        btnModifyUser.setOnClickListener {
            var intent = activity?.let {
                    it1 -> Intent(it1, ModifyUser::class.java)
            }
            intent!!.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        var imguser = view.findViewById<ImageView>(R.id.imgUser)
        Glide.with(mainActivity).load("https://avatars.dicebear.com/api/big-smile/"+user_name.toString()+".png").into(imguser)

        var btnMyBids = view.findViewById<RelativeLayout>(R.id.btnMyBids)
        btnMyBids.setOnClickListener {
            var intent = activity?.let {
                    it1 -> Intent(it1, MyBids::class.java)
            }
            intent!!.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        var btnMyItems = view.findViewById<RelativeLayout>(R.id.btnMyItems)
        btnMyItems.setOnClickListener {
            var intent = activity?.let {
                it1 -> Intent(it1, MyItems::class.java)
            }
            intent!!.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }


        var btnLogout = view.findViewById<RelativeLayout>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            var dlg = getActivity()?.let { it1 -> AlertDialog.Builder(it1) }
            dlg!!.setTitle("로그아웃")
            dlg.setMessage("정말 로그아웃 하시겠습니까?")
            //dlg.setIcon()
            dlg.setNegativeButton("취소", null)
            dlg.setPositiveButton("확인"){ dialog, which ->
                try {
                    var editor = LoginActivity.prefs.edit()
                    editor.clear()
                    editor.commit()
                    mainActivity.finish()
                } catch (e:Exception){
                    var dialog = activity?.let { it1 -> AlertDialog.Builder(it1) }
                    dialog!!.setTitle("로그아웃 오류")
                    dialog.setMessage("로그아웃 중 오류가 발생했습니다")
                    dialog.setPositiveButton("확인", null)
                    dialog.show()
                }

            }
            dlg.show()
        }


        //회원탈퇴
        var btnQuit = view.findViewById<RelativeLayout>(R.id.btnQuit)
        btnQuit.setOnClickListener {
            var dlg = activity?.let { it1 -> AlertDialog.Builder(it1) }
            dlg!!.setTitle("회원 탈퇴")
            dlg.setMessage("정말 탈퇴 하시겠습니까?\n탈퇴하시면 관련된 정보가 모두 삭제됩니다")
            //dlg.setIcon()
            dlg.setNegativeButton("취소", null)
            dlg.setPositiveButton("확인") { dialog, which ->
                var deleteUserResponse: DeleteUserResponse? = null
                var retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.0.17:4000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                var modifyUserService: ModifyUserService = retrofit.create(ModifyUserService::class.java)
                var user_id:String? = LoginActivity.prefs.getString("id", null)
                modifyUserService.deleteUser(user_id!!).enqueue(object:
                    Callback<DeleteUserResponse> {
                    override fun onFailure(call: Call<DeleteUserResponse>, t: Throwable) {0
                        t.message?.let { Log.e("BIDREQUSET", it) }
                        var dialog = getActivity()?.let { it1 -> AlertDialog.Builder(it1) }
                        dialog!!.setTitle("에러")
                        dialog.setMessage("호출실패했습니다.")
                        dialog.show()
                    }

                    override fun onResponse(call: Call<DeleteUserResponse>, response: Response<DeleteUserResponse>) {
                        deleteUserResponse = response.body()
                        var message = deleteUserResponse?.message
                        if (response.code() == 500) {
                            var dialog = getActivity()?.let { it1 -> AlertDialog.Builder(it1) }
                            dialog!!.setTitle("회원탈퇴 오류")
                            dialog.setMessage("오류가 발생했습니다")
                            dialog.setNegativeButton("확인", null)
                            dialog.show()
                        } else if (response.code() == 200) {
                            var dialog = getActivity()?.let { it1 -> AlertDialog.Builder(it1) }
                            dialog!!.setTitle("회원 탈퇴")
                            dialog.setMessage("탈퇴했습니다")
                            dialog.setNegativeButton("확인" ){ dialog, which ->
                                var editor = LoginActivity.prefs.edit()
                                editor.clear()
                                editor.commit()
                                mainActivity.finish()
                            }
                            dialog.show()
                        }
                    }
                })
            }
            dlg.show()
        }
        return view
    }
    override fun onResume() {
        fragmentManager?.let { refreshFragment(this, it) }
        return super.onResume()
    }
    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
        var ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }

}