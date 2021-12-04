package org.cookandroid.hiauction

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.cookandroid.hiauction.interfaces.ItemDetailService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.cookandroid.hiauction.datas.ItemDetailResponse
import org.cookandroid.hiauction.datas.ResponseData


class ItemDetail: AppCompatActivity() {
    var itemDetailResponse: ItemDetailResponse? = null
    var type :Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var intent = intent

        type = intent.getIntExtra("type", 0)
        var itemId = intent.getIntExtra("item_id", -1)
        println("TYPE: $type=================================================================")

        if (itemId != -1) {
            Log.i("프로젝트", itemId.toString())
            var retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.17:4000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            var itemDetailService: ItemDetailService = retrofit.create(ItemDetailService::class.java)

            itemDetailService.getItem(itemId!!).enqueue(object: Callback<ItemDetailResponse> {
                override fun onFailure(call: Call<ItemDetailResponse>, t: Throwable) {
                    t.message?.let { Log.e("ITEMREQUSET", it) }
                    var dialog = AlertDialog.Builder(this@ItemDetail)
                    dialog.setTitle("에러")
                    dialog.setMessage("호출실패했습니다.")
                    dialog.show()
                }

                @RequiresApi(Build.VERSION_CODES.M)
                override fun onResponse(call: Call<ItemDetailResponse>, response: Response<ItemDetailResponse>) {
                    Log.i("프로젝트", response.code().toString())
                    itemDetailResponse = response.body()
                    Log.i("프로젝트", itemDetailResponse!!.item.seller_id)
                    //var dialog = AlertDialog.Builder(this@MyBids)
                    //dialog.setMessage(bidListResponse?.bid_list?.get(1)?.item_name.toString())
                    var item = itemDetailResponse?.item
                    //Log.i("BidsArr", bidsArr.toString())
                    //dialog.show()
                    var seller_name = findViewById<TextView>(R.id.S_name)
                    var address = findViewById<TextView>(R.id.S_location)
                    var seller_rate = findViewById<TextView>(R.id.S_rate)
                    var item_name = findViewById<TextView>(R.id.Item_name)
                    var item_create_date = findViewById<TextView>(R.id.itemCurrentDate)
                    var item_expire_date = findViewById<TextView>(R.id.itemExpireDate)
                    var description = findViewById<TextView>(R.id.Item_des)
                    Log.i("프로젝트", "진행1")
                    seller_name.text = item!!.seller_name
                    Log.i("프로젝트", "진행2")
                    address.text = item.address
                    Log.i("프로젝트", "진행3")
                    seller_rate.text = item.seller_rate.toString()
                    item_name.text = item.item_name
                    Log.i("프로젝트", "진행4")
                    item_create_date.text = "시작일 " + item.create_date
                    item_expire_date.text = "만료일 " + item.expire_date
                    Log.i("프로젝트", "진행5")
                    description.text = item.description
                    Log.i("프로젝트", type.toString())
                    when(type) {
                        //메인페이지에서 상품 상세페이지 접근
                        1 -> {}
                        //마이페이지 내 입찰목록에서 상품 상세페이지 접근
                        2 -> {
                            var bidType = intent.getIntExtra("bid_type", 0)
                            println("ITEM_TYPE : $bidType ===========================================================")
                            when(bidType){
                                // 낙찰완료 + 자기자신이 낙찰 (채팅버튼)
                                1 -> {
                                    var chatBtn = findViewById<Button>(R.id.Imbuy)
                                    chatBtn.setBackgroundColor(resources.getColor(R.color.bidFinish, null))
                                    chatBtn.text = "판매자와 채팅"
                                    chatBtn.setOnClickListener {
                                        // 채팅으로 가야함
                                    }
                                    var divider = findViewById<TextView>(R.id.divider)
                                    divider.visibility = View.GONE
                                    var endItemBtn = findViewById<Button>(R.id.Imbid)
                                    endItemBtn.visibility = View.GONE
                                    var Imprice = findViewById<TextView>(R.id.Imprice)
                                    Imprice.visibility = View.GONE
                                    var Bidprice = findViewById<TextView>(R.id.Bidprice)
                                    Bidprice.text = "낙찰가 " + item.current_price
                                }
                                // 버튼 없애고, 낙찰가만 표시 (된다면 그래프 띄어도 좋을 듯)
                                2 -> {
                                    var reviewBtn = findViewById<Button>(R.id.Imbuy)
                                    reviewBtn.visibility = View.GONE
                                    var endItemBtn = findViewById<Button>(R.id.Imbid)
                                    endItemBtn.visibility = View.GONE
                                    var Imprice = findViewById<TextView>(R.id.Imprice)
                                    Imprice.visibility = View.GONE
                                    var divider = findViewById<TextView>(R.id.divider)
                                    divider.visibility = View.GONE
                                    var Bidprice = findViewById<TextView>(R.id.Bidprice)
                                    Bidprice.text = "낙찰가 " + item.current_price
                                }
                                // 후기등록 필요
                                3 -> {
                                    var endItemBtn = findViewById<Button>(R.id.Imbid)
                                    endItemBtn.visibility = View.GONE
                                    var divider = findViewById<TextView>(R.id.divider)
                                    divider.visibility = View.GONE
                                    var reviewBtn = findViewById<Button>(R.id.Imbuy)
                                    reviewBtn.setBackgroundColor(resources.getColor(R.color.bidFinish, null))
                                    reviewBtn.text = "후기 등록"
                                    reviewBtn.setOnClickListener {
                                        // 후기등록 띄우기
                                        var dlgView = View.inflate(this@ItemDetail, R.layout.rating, null)
                                        var dlg = AlertDialog.Builder(this@ItemDetail)
                                        dlg.setTitle("후기 등록")
                                        dlg.setView(dlgView)
                                        var ad = dlg.create()
                                        var cancelButton = findViewById<Button>(R.id.btnCancelRating)
                                        cancelButton.setOnClickListener {
                                            ad.dismiss()
                                        }
                                        var enrollButton = findViewById<Button>(R.id.btnEnrollRating)
                                        enrollButton.setOnClickListener {
                                            var edtRating = findViewById<TextView>(R.id.edtRating)
                                            var score = findViewById<RatingBar>(R.id.ratingScore)
                                            var rating_score = score.rating
                                            var user_id:String? = LoginActivity.prefs.getString("id", null)
                                            itemDetailService.enrollRating(item.seller_id, user_id!!, rating_score, edtRating.text.toString())
                                                .enqueue(object : Callback<ResponseData> {
                                                    override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                                                        t.message?.let { Log.e("ITEMREQUSET", it) }
                                                        var dialog = AlertDialog.Builder(this@ItemDetail)
                                                        dialog.setTitle("에러")
                                                        dialog.setMessage("호출실패했습니다.")
                                                        dialog.show()
                                                    }
                                                    @RequiresApi(Build.VERSION_CODES.M)
                                                    override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                                                        var responseData = response.body()
                                                        when(response.code()) {
                                                            200 -> {
                                                                reviewBtn.visibility = View.GONE
                                                                var Imprice = findViewById<TextView>(R.id.Imprice)
                                                                Imprice.visibility = View.GONE
                                                                var divider = findViewById<TextView>(R.id.divider)
                                                                divider.visibility = View.GONE
                                                                var Bidprice = findViewById<TextView>(R.id.Bidprice)
                                                                Bidprice.text = "낙찰가 " + item.current_price
                                                            }
                                                            500 -> {
                                                                var dialog = AlertDialog.Builder(this@ItemDetail)
                                                                dialog.setTitle("후기등록 오류")
                                                                dialog.setMessage("내부적으로 오류가 발생하였습니다.\n잠시 후 다시 시도해주세요")
                                                                dialog.show()
                                                            }
                                                        }
                                                    }
                                                })
                                        }
                                        dlg.show()
                                    }
                                }
                            }
                        }
                        //마이페이지 내가 등록한 상품에서 상품 상세페이지 접근
                        3 -> {
                            var itemType = intent.getIntExtra("item_type", 0)
                            Log.i("프로젝트", "itemType: " + itemType.toString())
                            when(itemType){
                                1->{
                                    var chatBtn = findViewById<Button>(R.id.Imbuy)
                                    chatBtn.setBackgroundColor(resources.getColor(R.color.bidFinish, null))
                                    chatBtn.text = "구매자와 채팅"
                                    chatBtn.setOnClickListener {
                                        // 채팅으로 가야함
                                    }
                                    var endItemBtn = findViewById<Button>(R.id.Imbid)
                                    endItemBtn.setBackgroundColor(resources.getColor(R.color.itemFinish, null))
                                    endItemBtn.text = "거래 완료하기"
                                    endItemBtn.setOnClickListener {
                                        // 거래완료 시키기
                                        var dlg = AlertDialog.Builder(this@ItemDetail)
                                        dlg.setTitle("거래완료")
                                        dlg.setMessage("거래를 완료하시겠습니까?\n완료하시면 되돌릴수 없습니다.")
                                        dlg.setNegativeButton("취소", null)
                                        dlg.setPositiveButton("확인"){ dialog, which ->
                                            itemDetailService.completeItem(itemId!!)
                                                .enqueue(object : Callback<ResponseData> {
                                                    override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                                                        t.message?.let { Log.e("ITEMREQUSET", it) }
                                                        var dialog = AlertDialog.Builder(this@ItemDetail)
                                                        dialog.setTitle("에러")
                                                        dialog.setMessage("호출실패했습니다.")
                                                        dialog.show()
                                                    }
                                                    @RequiresApi(Build.VERSION_CODES.M)
                                                    override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                                                        var responseData = response.body()
                                                        when(response.code()) {
                                                            200 -> {
                                                                chatBtn.visibility = View.GONE
                                                                endItemBtn.visibility = View.GONE
                                                                var reviewBtn = findViewById<Button>(R.id.Imbuy)
                                                                reviewBtn.visibility = View.GONE
                                                                var endItemBtn = findViewById<Button>(R.id.Imbid)
                                                                endItemBtn.visibility = View.GONE
                                                                var Imprice = findViewById<TextView>(R.id.Imprice)
                                                                Imprice.visibility = View.GONE
                                                                var divider = findViewById<TextView>(R.id.divider)
                                                                divider.visibility = View.GONE
                                                                var Bidprice = findViewById<TextView>(R.id.Bidprice)
                                                                Bidprice.text = "낙찰가 " + item.current_price
                                                            }
                                                            500 -> {
                                                                var dialog = AlertDialog.Builder(this@ItemDetail)
                                                                dialog.setTitle("거래완료 오류")
                                                                dialog.setMessage("내부적으로 오류가 발생하였습니다.\n잠시 후 다시 시도해주세요")
                                                                dialog.show()
                                                            }
                                                        }
                                                    }
                                                })
                                        }

                                    }
                                } // 채팅버튼, 거래완료 버튼 표시
                                2->{
                                    var endItemBtn = findViewById<Button>(R.id.Imbid)
                                    endItemBtn.visibility = View.GONE
                                    var divider = findViewById<TextView>(R.id.divider)
                                    divider.visibility = View.GONE
                                    var expandDateBtn = findViewById<Button>(R.id.Imbuy)
                                    expandDateBtn.setBackgroundColor(resources.getColor(R.color.itemExpired, null))
                                    expandDateBtn.text = "기간 연장"
                                    expandDateBtn.setOnClickListener {
                                        // 후기등록 띄우기
                                        var dlgView = View.inflate(this@ItemDetail, R.layout.expand_date, null)
                                        var dlg = AlertDialog.Builder(this@ItemDetail)
                                        dlg.setTitle("기간 연장")
                                        dlg.setView(dlgView)
                                        dlg.setNegativeButton("취소", null)
                                        dlg.setPositiveButton("연장") {dialog, which->
                                            var expandDate = findViewById<CalendarView>(R.id.expandDate)
                                            itemDetailService.modifyExpireDate(item.item_id, expandDate.toString())
                                                .enqueue(object : Callback<ResponseData> {
                                                    override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                                                        t.message?.let { Log.e("ITEMREQUSET", it) }
                                                        var dialog = AlertDialog.Builder(this@ItemDetail)
                                                        dialog.setTitle("에러")
                                                        dialog.setMessage("호출실패했습니다.")
                                                        dialog.show()
                                                    }
                                                    @RequiresApi(Build.VERSION_CODES.M)
                                                    override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                                                        var responseData = response.body()
                                                        when(response.code()) {
                                                            200 -> {
                                                                finish() //인텐트 종료
                                                                overridePendingTransition(0, 0) //인텐트 효과 없애기
                                                                val intent = getIntent() //인텐트
                                                                intent.putExtra("type", 1)
                                                                startActivity(intent) //액티비티 열기
                                                                overridePendingTransition(0, 0) //인텐트 효과 없애기
                                                            }
                                                            500 -> {
                                                                var dialog = AlertDialog.Builder(this@ItemDetail)
                                                                dialog.setTitle("기간연장 오류")
                                                                dialog.setMessage("내부적으로 오류가 발생하였습니다.\n잠시 후 다시 시도해주세요")
                                                                dialog.show()
                                                            }
                                                        }
                                                    }
                                                })
                                        }
                                        dlg.show()

                                    }
                                } // 기간연장 버튼 표시
                                3->{}
                            }
                        }
                    }
                }
            })
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

