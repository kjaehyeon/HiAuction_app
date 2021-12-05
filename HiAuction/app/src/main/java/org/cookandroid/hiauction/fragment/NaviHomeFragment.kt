package org.cookandroid.hiauction.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragement_navi_home.*
import org.cookandroid.hiauction.*
import org.cookandroid.hiauction.ListAdapter
import org.cookandroid.hiauction.datas.*
import org.cookandroid.hiauction.interfaces.ItemListService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NaviHomeFragment : Fragment() {
    lateinit var test : TextView
    var category_id: Int = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragement_navi_home, container, false)

        /*var ItemList = arrayListOf<Item>( //고치기
            Item(1,"Auction2", "대현동", "165,000", "140,000",1),
            Item(2,"Auction3", "대현동", "123,000","100,000",2)
        )*/
        //test = view.findViewById(R.id.textView)
        //test.text = prefs.getString("id", null)
        var edtSearch = view.findViewById<EditText>(R.id.edtSearch) //검색창
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

        btnWrite.setOnClickListener{
            startActivity(regintent)
        }
        var itemArr : ArrayList<ItemListData>? = null
        val lv = view.findViewById<View>(R.id.lv) as ListView
        lv.setOnItemClickListener { adapterView, view, i, l ->
            val clickedItem = itemArr?.get(i)?.item_id
            val intent = Intent(activity,ItemDetail::class.java)
            intent.putExtra("item_id",clickedItem)
            intent.putExtra("type",1)
            startActivity(intent)
        }
        var retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.17:4000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var itemListService: ItemListService = retrofit.create(ItemListService::class.java)

        var address : String = "동인동"
        var itemListResponse: ListResponse? = null
        itemListService.getItemList(category_id!! ,address).enqueue(object: Callback<ListResponse> {
            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                t.message?.let { Log.e("BIDREQUSET", it) }
                var dialog = activity?.let { AlertDialog.Builder(it) }
                if (dialog != null) {
                    dialog.setTitle("에러")
                }
                if (dialog != null) {
                    dialog.setMessage("호출실패했습니다.")
                }
                if (dialog != null) {
                    dialog.show()
                }
            }
            override fun onResponse(call: Call<ListResponse>, response: Response<ListResponse>) {
                itemListResponse = response.body()
                Log.i("efe",response.code().toString())
                itemArr = itemListResponse?.item_list ?: ArrayList()
                var itemAdapter = activity?.let { ListAdapter(it, itemArr!!) }
                lv.adapter = itemAdapter
            }
        })
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==RESULT_OK){
            if (data != null) {
                btnCategory.text = data.getStringExtra("category")
                category_id = data!!.getIntExtra("category_id",0)
            }
        }
    }
}