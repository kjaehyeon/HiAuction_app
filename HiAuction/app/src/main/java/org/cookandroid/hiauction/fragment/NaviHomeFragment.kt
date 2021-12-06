package org.cookandroid.hiauction.fragment

import android.app.Activity
import android.app.Activity.INPUT_METHOD_SERVICE
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*

import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragement_navi_home.*
import org.cookandroid.hiauction.*
import org.cookandroid.hiauction.ListAdapter
import org.cookandroid.hiauction.LoginActivity.Companion.addresses
import org.cookandroid.hiauction.datas.*
import org.cookandroid.hiauction.interfaces.ItemListService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class NaviHomeFragment : Fragment() {
    lateinit var test : TextView
    lateinit var address : String
    var category_id: Int = 0
    var itemArr : ArrayList<ItemListData>? = null
    lateinit var itemAdapter:ListAdapter
    lateinit var edtSearch:EditText
    lateinit var itemListService: ItemListService
    lateinit var lv:ListView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragement_navi_home, container, false)
        edtSearch = view.findViewById<EditText>(R.id.edtSearch)//검색창
        var card = view.findViewById<CardView>(R.id.cardview)
        var spinner = view.findViewById<Spinner>(R.id.Location)
        var adapter: ArrayAdapter<String>
        adapter = ArrayAdapter(requireActivity(),  R.layout.spinner_item, addresses)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter
        var btnCategory = view.findViewById<Button>(R.id.btnCategory) //카테고리 설정 버튼
        var btnWrite = view.findViewById<ImageView>(R.id.btnWrite) //물품 등록 버튼
        var Location = view.findViewById<Spinner>(R.id.Location) //사용자 주소
        var retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.17:4000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        itemListService = retrofit.create(ItemListService::class.java)
        //var search = edtSearch.toString() // 검색 내용


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

        lv = view.findViewById<View>(R.id.lv) as ListView
        lv.setOnItemClickListener { adapterView, view, i, l ->
            val clickedItem = itemArr?.get(i)?.item_id
            val intent = Intent(activity,ItemDetail::class.java)
            intent.putExtra("item_id",clickedItem)
            intent.putExtra("type",1)
            startActivity(intent)
        }
        address = addresses[0]
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>, arg1: View, arg2: Int, arg3: Long) {
                address = addresses[arg2]
                var itemListResponse: ListResponse? = null
                itemListService.getItemList(category_id!! ,address).enqueue(object: Callback<ListResponse> {
                    override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                        t.message?.let { Log.e("BIDREQUSET", it) }
                        var dialog = activity?.let { AlertDialog.Builder(it) }
                        dialog!!.setTitle("에러")
                        dialog.setMessage("호출실패했습니다.")
                        dialog.show()
                    }
                    override fun onResponse(call: Call<ListResponse>, response: Response<ListResponse>) {
                        itemListResponse = response.body()
                        itemArr = itemListResponse?.item_list ?: ArrayList()
                        itemAdapter = activity?.let { ListAdapter(it, itemArr!!) }!!
                        lv.adapter = itemAdapter
                    }
                })
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {

            }

        }
        var itemListResponse: ListResponse? = null
        itemListService.getItemList(category_id!! ,address).enqueue(object: Callback<ListResponse> {
            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                t.message?.let { Log.e("BIDREQUSET", it) }
                var dialog = activity?.let { AlertDialog.Builder(it) }
                dialog!!.setTitle("에러")
                dialog.setMessage("호출실패했습니다.")
                dialog.show()
            }
            override fun onResponse(call: Call<ListResponse>, response: Response<ListResponse>) {
                itemListResponse = response.body()
                Log.i("프로젝트트",response.code().toString())
                itemArr = itemListResponse?.item_list ?: ArrayList()
                Log.i("프로젝트트",itemArr.toString())
                itemAdapter = activity?.let { ListAdapter(it, itemArr!!) }!!
                lv.adapter = itemAdapter
            }
        })
        return view
    }

    override fun onResume() {
        //fragmentManager?.let { refreshFragment(this, it) }
        super.onResume()
        edtSearch.setOnEditorActionListener { textView, action, keyEvent ->
            Log.i("프로젝트", "검색바")
            var handled = false
            Log.i("프로젝트", action.toString())
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                Log.i("프로젝트", "검색 이벤트 발생")
                var searchData = edtSearch.text.toString()
                var address = "동인동"
                var itemListResponse: ListResponse? = null
                itemListService.getItemList(category_id!!, address, searchData)
                    .enqueue(object : Callback<ListResponse> {
                        override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                            t.message?.let { Log.e("BIDREQUSET", it) }
                            var dialog = activity?.let { AlertDialog.Builder(it) }
                            dialog!!.setTitle("에러")
                            dialog.setMessage("호출실패했습니다.")
                            dialog.show()
                        }

                        override fun onResponse(
                            call: Call<ListResponse>,
                            response: Response<ListResponse>
                        ) {
                            itemListResponse = response.body()
                            Log.i("프로젝트", response.code().toString())
                            itemArr!!.clear()
                            itemArr!!.addAll(itemListResponse?.item_list ?: ArrayList())
                            Log.i("프로젝트", itemArr.toString())
                            itemAdapter.notifyDataSetChanged()
                            val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            inputMethodManager!!.hideSoftInputFromWindow(edtSearch.windowToken, 0)
                            //lv.adapter = itemAdapter;
                            //var ft = parentFragmentManager.beginTransaction()
                            //ft!!.detach(this@NaviHomeFragment).attach(this@NaviHomeFragment).commit()
                        }
                    })
                handled = true
            }
            handled
        }
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
                var itemListResponse: ListResponse? = null
                Log.i("category",category_id.toString())
                itemListService.getItemList(category_id!! ,address).enqueue(object: Callback<ListResponse> {
                    override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                        t.message?.let { Log.e("BIDREQUSET", it) }
                        var dialog = activity?.let { AlertDialog.Builder(it) }
                        dialog!!.setTitle("에러")
                        dialog.setMessage("호출실패했습니다.")
                        dialog.show()
                    }
                    override fun onResponse(call: Call<ListResponse>, response: Response<ListResponse>) {
                        itemListResponse = response.body()
                        Log.i("프로젝트트",response.code().toString())
                        itemArr = itemListResponse?.item_list ?: ArrayList()
                        Log.i("프로젝트트",itemArr.toString())
                        itemAdapter = activity?.let { ListAdapter(it, itemArr!!) }!!
                        lv.adapter = itemAdapter
                    }
                })
            }
        }
    }
}