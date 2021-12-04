package org.cookandroid.hiauction.fragment


import android.content.Intent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import org.cookandroid.hiauction.*

class NaviMyPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.mypage_main, container, false)
        setHasOptionsMenu(true)

        var btnModifyUser = view.findViewById<RelativeLayout>(R.id.btnModifyUser)
        btnModifyUser.setOnClickListener {
            var intent = activity?.let {
                    it1 -> Intent(it1, ModifyUser::class.java)
            }
            intent!!.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

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
            dlg.setPositiveButton("확인", null)
            dlg.show()
        }

        var btnQuit = view.findViewById<RelativeLayout>(R.id.btnQuit)
        btnQuit.setOnClickListener {
            var dlg = activity?.let { it1 -> AlertDialog.Builder(it1) }
            dlg!!.setTitle("회원 탈퇴")
            dlg.setMessage("정말 탈퇴 하시겠습니까?\n탈퇴하시면 관련된 정보가 모두 삭제됩니다")
            //dlg.setIcon()
            dlg.setNegativeButton("취소", null)
            dlg.setPositiveButton("확인", null)
            dlg.show()
        }
        return view
    }

}