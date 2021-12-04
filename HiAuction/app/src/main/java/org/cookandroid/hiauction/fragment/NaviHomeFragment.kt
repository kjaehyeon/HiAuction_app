package org.cookandroid.hiauction.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragement_navi_home.*
import okhttp3.internal.notify
import org.cookandroid.hiauction.*
import org.cookandroid.hiauction.ListAdapter

class NaviHomeFragment : Fragment() {
    lateinit var test : TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragement_navi_home, container, false)
        var ItemList = arrayListOf<Item>( //고치기
            Item(1,"Auction2", "대현동", "165,000", "140,000",1),
            Item(2,"Auction3", "대현동", "123,000","100,000",2)
        )
        //test = view.findViewById(R.id.textView)
        //test.text = prefs.getString("id", null)
        var edtSearch = view.findViewById<EditText>(R.id.edtSearch) //검색창
        var Adapter = activity?.let { ListAdapter(it, ItemList) }
        var card = view.findViewById<CardView>(R.id.cardview)
        var btnCategory = view.findViewById<Button>(R.id.btnCategory) //카테고리 설정 버튼
        var btnWrite = view.findViewById<ImageButton>(R.id.btnWrite) //물품 등록 버튼
        var Location = view.findViewById<TextView>(R.id.Location) //사용자 주소

        var search = edtSearch.toString() // 검색 내용

        var regintent = Intent(activity, ItemRegister::class.java)
        var categoryintent = activity?.let {
                it1 -> Intent(it1, Category::class.java)
        }
        btnCategory.setOnClickListener{
            categoryintent!!.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivityForResult(categoryintent,0)
            refreshFragment(this, getParentFragmentManager())
        }

        var string = arguments?.getString("category")
        if (categoryintent != null) {
            if(categoryintent.hasExtra("category")){
                btnCategory.setText(string)
            }
        }
        btnWrite.setOnClickListener{
            startActivity(regintent)
        }

        val lv = view.findViewById<View>(R.id.lv) as ListView
        lv.adapter = Adapter
        lv.setOnItemClickListener { adapterView, view, i, l ->
            val clickedItem = ItemList[i].id
            val intent = Intent(activity,ItemDetail::class.java)
            intent.putExtra("Id",clickedItem)
            startActivity(intent)
        }
        return view
    }
    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
        var ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==RESULT_OK){
            if (data != null) {
                btnCategory.text = data.getStringExtra("category")
            }
        }
    }
}