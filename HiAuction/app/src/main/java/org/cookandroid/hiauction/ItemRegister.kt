package org.cookandroid.hiauction


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.item_detail.*
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
import java.util.*
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.Exception


class ItemRegister : AppCompatActivity(){
    var address : List<String> = addresses
    var categorylist = arrayOf("카테고리 선택","디지털 기기","생활가전","가구/인테리어","유아동","생활/가공식품","유아도서","스포츠/레저",
        "여성잡화","여성의류","남성패션/잡화","게임/취미","뷰티/미용","반려동물","도서/티켓/음반","식물")

    lateinit var imgbtn : ImageButton
    lateinit var categorySpinner: Spinner
    lateinit var addressSpinner: Spinner
    lateinit var title : EditText
    lateinit var description : EditText
    lateinit var start_price : EditText
    lateinit var immediate_price : EditText
    lateinit var min_bid_unit : EditText
    lateinit var datebtn : Button
    lateinit var submit : Button
    lateinit var selected_date : TextView
    val id : String = prefs.getString("id", null).toString()

    val BASE_URL= "http://192.168.0.17:4000/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(ItemService::class.java)

    var selected_category : Int = 0
    lateinit var selected_address : String
    lateinit var imageuri : Uri
    lateinit var bitmap : Bitmap

    @RequiresApi(Build.VERSION_CODES.N)
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

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selected_category = 0
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position>0)
                    selected_category = position
            }
        }
        addressSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selected_address = ""
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selected_address = address[position]
            }
        }

        imgbtn.setOnClickListener{
            var intent = Intent()
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            intent.setType("image/*"); intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,101);

        }

        datebtn.setOnClickListener{
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)
            DatePickerDialog(this@ItemRegister,
                { view, year, month, dayOfMonth ->
                    selected_date.text= "${year}-${month + 1}-${dayOfMonth}"

                },year,month,date).show()
        }

        submit.setOnClickListener{
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()

            if(title.text.isNotBlank() && description.text.isNotBlank() && start_price.text.isNotBlank() &&
                    immediate_price.text.isNotBlank() && min_bid_unit.text.isNotBlank() && selected_date.text.isNotBlank() &&
                    selected_category!=0 && selected_address.isNotBlank()){

                val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), byteArray)

                var user_id : RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), id)
                var title_req : RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), title.text.toString())
                var description_req : RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), description.text.toString())
                var start_price_req : Int = start_price.text.toString().toInt()
                var immediate_price_req : Int = immediate_price.text.toString().toInt()
                var min_bid_unit_req : Int = min_bid_unit.text.toString().toInt()
                var expired_date_req : RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), selected_date.text.toString())
                var category_req : Int = selected_category
                var address_req : RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), selected_address)
                var img : MultipartBody.Part = MultipartBody.Part.createFormData("img_file",
                    "$title_req.PNG", requestFile)

                val postItemRegister_req = api.postItem(user_id = user_id, item_name=title_req, description=description_req,
                    start_price = start_price_req, immediate_price = immediate_price_req, min_bid_unit = min_bid_unit_req, expired_date = expired_date_req,
                    category_id = category_req, address = address_req, img_file=img)
                postItemRegister_req.enqueue(object : Callback<ItemRegisterResult> {
                    override fun onResponse(
                        call: Call<ItemRegisterResult>,
                        response: Response<ItemRegisterResult>
                    ) {
                        println(response.code())
                        if(response.isSuccessful) {
                            Toast.makeText(this@ItemRegister, "아이템 등록 성공", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        else {
                            val dlg: AlertDialog.Builder = AlertDialog.Builder(this@ItemRegister)
                            dlg.setTitle("Message") //제목
                            dlg.setMessage("다시 시도해 주세요.") // 메시지
                            dlg.setPositiveButton("닫기",null)
                            dlg.show()
                        }
                    }
                    override fun onFailure(call: Call<ItemRegisterResult>, t: Throwable) {
                        println(call.toString())
                        println(t.message)
                        println(t.stackTrace)
                        println(t.localizedMessage)
                        Toast.makeText(this@ItemRegister,"등록 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            }else{
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this@ItemRegister)
                dlg.setTitle("Message") //제목
                dlg.setMessage("양식을 모두 정확히 입력해 주세요.") // 메시지
                dlg.setPositiveButton("닫기",null)
                dlg.show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 101 && resultCode == RESULT_OK){
            try{
                val ins : InputStream? = getContentResolver().openInputStream(data!!.data!!)
                bitmap = BitmapFactory.decodeStream(ins)
                ins?.close();
                imgbtn.setImageBitmap(bitmap)
            }catch (e: Exception){
                e.printStackTrace();
            }
        }else if(requestCode == 101 && resultCode == RESULT_CANCELED)
        {
            Toast.makeText(this,"취소", Toast.LENGTH_SHORT).show()
        }

    }

}