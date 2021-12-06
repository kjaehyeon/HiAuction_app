package org.cookandroid.hiauction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.cookandroid.hiauction.datas.Chatdata
import org.cookandroid.hiauction.interfaces.ChatAPI
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatRoomActivity : AppCompatActivity() {
    lateinit var chatAdapter: ChatAdapter
    val datas = mutableListOf<Chatdata>()
    lateinit var rv_chatlist : RecyclerView
    lateinit var mSocket: Socket
    lateinit var sendbutton : Button
    lateinit var textForSend : EditText
    lateinit var tv_logo : TextView
    val id = LoginActivity.prefs.getString("id", null)
    var previous : Int = 1

    lateinit var other_id : String
    lateinit var other_name : String
    var item_id : Int? = null
    lateinit var address : String
    lateinit var item_name : String
    lateinit var img_url : String
    var score : Float? = null
    var room_id : Int? = null

    val BASE_URL= "http://192.168.22.48:4000/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(ChatAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val intent = intent
        other_id = intent.getStringExtra("other_id").toString()
        other_name = intent.getStringExtra("other_name").toString()
        item_id = intent.getIntExtra("item_id", 0)
        address = intent.getStringExtra("address").toString()
        item_name = intent.getStringExtra("item_name").toString()
        score = intent.getFloatExtra("score", 0f)
        room_id = intent.getIntExtra("room_id", 0)
        img_url = intent.getStringExtra("img_url").toString()


        rv_chatlist = findViewById(R.id.chatlist)
        sendbutton = findViewById(R.id.sendbutton)
        textForSend = findViewById(R.id.textforsend)
        tv_logo = findViewById(R.id.tv_logo)
        tv_logo.text = other_name

        mSocket = SocketApplication.get()
        mSocket.connect()
        mSocket.emit("join_room", room_id)

        sendbutton.setOnClickListener{
            val jsonObject : JSONObject = JSONObject()
            jsonObject.put("room_id", room_id)
            jsonObject.put("content", textForSend.text.toString())
            jsonObject.put("sender_id", id)
            jsonObject.put("receiver_id", other_id)
            mSocket.emit("message",jsonObject)
            textForSend.setText("")
        }

        mSocket.on("new Message", onNewMessage)
        initRecycler()
    }

    var onNewMessage : Emitter.Listener = Emitter.Listener {
        runOnUiThread {
            var reg_date : String= it[1].toString()
            var strs : List<String> = reg_date.split(' ')
            var hours : List<String> = strs[1].split(':')
            var ampm : String  = if(hours[0].toInt() < 12)
                "오전"
            else
                "오후"
            if(it[0].equals(id)){
                previous = 1
                datas.apply{
                    add(
                        Chatdata(sender_id= it[0].toString(),
                        reg_date = ampm+" "+hours[0]+":"+hours[1],
                        content=it[2].toString(), type=1)
                    )
                    chatAdapter.datas = datas
                    chatAdapter.notifyDataSetChanged()
                }
            }else if(!it[0].equals(id) && previous == 1){
                previous = 2
                datas.apply{
                    add(
                        Chatdata(sender_id= it[0].toString(), reg_date =ampm+" "+hours[0]+":"+hours[1],
                        content=it[2].toString(), type=2)
                    )
                    chatAdapter.datas = datas
                    chatAdapter.notifyDataSetChanged()
                }
            }else{
                previous = 3
                datas.apply{
                    add(
                        Chatdata(sender_id= it[0].toString(), reg_date = ampm+" "+hours[0]+":"+hours[1],
                        content=it[2].toString(), type=3)
                    )
                    chatAdapter.datas = datas
                    chatAdapter.notifyDataSetChanged()
                }
            }
            rv_chatlist.scrollToPosition(chatAdapter.itemCount-1)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                mSocket.disconnect()
                var intent = Intent(applicationContext, LoginActivity::class.java)
                intent.putExtra("result", "ok")
                setResult(RESULT_OK, intent)
                finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
    private fun initRecycler() {
        chatAdapter = ChatAdapter(other_name)
        rv_chatlist.adapter = chatAdapter

        val callGetChatRooms = api.getChatlist(room_id=room_id!!)
        callGetChatRooms.enqueue(object : Callback<List<Chatdata>> {
            override fun onResponse(
                call: Call<List<Chatdata>>,
                response: Response<List<Chatdata>>
            ) {
                if(response.code() == 200){
                    var iterator : Iterator<Chatdata> = response.body()!!.iterator()
                    while(iterator.hasNext()){
                        var data : Chatdata = iterator.next()
                        var strs : List<String> = data.reg_date.split(' ')
                        var hours : List<String> = strs[1].split(':')
                        var ampm : String  = if(hours[0].toInt() < 12)
                            "오전"
                        else
                            "오후"
                        if(data.sender_id.equals(id)){
                            previous = 1
                            datas.apply {
                                add(
                                    Chatdata(sender_id= data.sender_id, reg_date = ampm+" "+hours[0]+":"+hours[1],
                                    content=data.content, type=1)
                                )
                                chatAdapter.datas = datas
                                chatAdapter.notifyDataSetChanged()
                            }
                        }else if(!data.sender_id.equals(id) && previous == 1){
                            previous = 2
                            datas.apply {
                                add(
                                    Chatdata(sender_id= data.sender_id, reg_date = ampm+" "+hours[0]+":"+hours[1],
                                    content=data.content, type=2)
                                )
                                chatAdapter.datas = datas
                                chatAdapter.notifyDataSetChanged()
                            }
                        }else{
                            previous = 3
                            datas.apply {
                                add(
                                    Chatdata(sender_id= data.sender_id, reg_date = ampm+" "+hours[0]+":"+hours[1],
                                    content=data.content, type=3)
                                )
                                chatAdapter.datas = datas
                                chatAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                    rv_chatlist.scrollToPosition(chatAdapter.itemCount-1)
                }
            }
            override fun onFailure(call: Call<List<Chatdata>>, t: Throwable) {

            }
        })
    }
    override fun onBackPressed() {
        mSocket.disconnect()
        super.onBackPressed()
    }
}

class ChatAdapter(name : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var datas = mutableListOf<Chatdata>()
    val other_name = name
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        val view : View?
        return when(viewType) {
            1 -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.my_message,
                    parent,
                    false
                )
                MyMessageViewHolder(view)
            }
            2 -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.your_message_profile,
                    parent,
                    false
                )
                YourMessageProfileViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.your_message,
                    parent,
                    false
                )
                YourMessageViewHolder(view)
            }
        }
    }
    override fun getItemCount(): Int = datas.size

    override fun getItemViewType(position: Int): Int {
        return datas[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(datas[position].type) {
            1 -> {
                (holder as MyMessageViewHolder).bind(datas[position])
                holder.setIsRecyclable(false)
            }
            2 -> {
                (holder as YourMessageProfileViewHolder).bind(datas[position])
                holder.setIsRecyclable(false)
            }
            else -> {
                (holder as YourMessageViewHolder).bind(datas[position])
                holder.setIsRecyclable(false)
            }
        }
    }

    inner class MyMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val content: TextView = view.findViewById(R.id.content)
        private val date: TextView = view.findViewById(R.id.date)
        fun bind(item: Chatdata) {
            content.text = item.content
            date.text = item.reg_date

        }
    }
    inner class YourMessageProfileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val content: TextView = view.findViewById(R.id.content)
        private val date: TextView = view.findViewById(R.id.date)
        private val profile : ImageView = view.findViewById(R.id.profile)

        fun bind(item: Chatdata) {
            content.text = item.content
            date.text = item.reg_date
            Glide.with(itemView).load("https://avatars.dicebear.com/api/big-smile/$other_name.png").into(profile)
        }
    }

    inner class YourMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val content: TextView = view.findViewById(R.id.content)
        private val date: TextView = view.findViewById(R.id.date)

        fun bind(item: Chatdata) {
            content.text = item.content
            date.text = item.reg_date
        }
    }
}