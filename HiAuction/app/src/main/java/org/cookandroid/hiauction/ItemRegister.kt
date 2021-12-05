package org.cookandroid.hiauction

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.itemregister.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.cookandroid.hiauction.LoginActivity.Companion.addresses
import org.cookandroid.hiauction.LoginActivity.Companion.prefs
import org.cookandroid.hiauction.datas.ItemRegisterResult
import org.cookandroid.hiauction.interfaces.ItemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class ItemRegister : AppCompatActivity(){
    var address : List<String> = addresses
    var categorylist = arrayOf("디지털 기기","생활가전","가구/인테리어","유아동","생활/가공식품","유아도서","스포츠/레저",
        "여성잡화","여성의류","남성패션/잡화","게임/취미","뷰티/미용","반려동물","도서/티켓/음반","식물")

    lateinit var imgbtn : ImageButton
    lateinit var categorySpinner: Spinner
    lateinit var addressSpinner: Spinner
    lateinit var title : EditText
    lateinit var description : EditText
    lateinit var start_price : EditText
    lateinit var immediate_price : EditText
    lateinit var min_bid_unit : EditText
    lateinit var datebtn : ImageButton
    lateinit var submit : Button
    lateinit var selected_date : TextView
    val id : String = prefs.getString("id", null).toString()

    val BASE_URL= "http://192.168.0.17:4000/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(ItemService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itemregister)

        imgbtn =findViewById(R.id.imgbtn)
        categorySpinner = findViewById(R.id.categorySpinner)
        addressSpinner = findViewById(R.id.addressSpinner)
        title = findViewById(R.id.title)
        description = findViewById(R.id.description)
        start_price = findViewById(R.id.start_price)
        immediate_price = findViewById(R.id.immediate_price)
        min_bid_unit = findViewById(R.id.min_bid_unit)
        datebtn = findViewById(R.id.datebtn)
        selected_date = findViewById(R.id.selected_date)
        submit = findViewById(R.id.submit)

        var categoryAdapter : ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, categorylist)
        categorySpinner.adapter = categoryAdapter

        var addressAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, address)
        addressSpinner.adapter = addressAdapter

        submit.setOnClickListener{
            val file = File("/storage/emulated/0/Download/Corrections 6.jpg")
            val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

            var user_id : RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), id)
            var title_req : RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), title.text.toString())
            var description_req : RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), description.text.toString())
            var start_price_req : Int = start_price.text.toString().toInt()
            var immediate_price_req : Int = immediate_price.text.toString().toInt()
            var min_bid_unit_req : Int = min_bid_unit.text.toString().toInt()
            var expired_date_req : RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), selected_date.text.toString())
            var category_req : Int = 1
            var address_req : RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), id)
            var img : MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestFile)

            val postItemRegister_req = api.postItem(user_id = user_id, item_name=title_req, description=description_req,
            start_price = start_price_req, immediate_price = immediate_price_req, min_bid_unit = min_bid_unit_req, expired_date = expired_date_req,
            category_id = category_req, address = address_req, image=img)
            postItemRegister_req.enqueue(object : Callback<ItemRegisterResult> {
                override fun onResponse(
                    call: Call<ItemRegisterResult>,
                    response: Response<ItemRegisterResult>
                ) {
                    if(response.isSuccessful) {
                    }
                    else {
                    }
                }

                override fun onFailure(call: Call<ItemRegisterResult>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }



    }
}