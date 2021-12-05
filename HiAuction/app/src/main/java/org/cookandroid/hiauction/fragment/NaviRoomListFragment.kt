package org.cookandroid.hiauction.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.cookandroid.hiauction.*
import org.cookandroid.hiauction.datas.RoomData
import org.cookandroid.hiauction.interfaces.ChatAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NaviRoomListFragment : Fragment() {
    lateinit var chatroomadapter: ChatRoomAdapter
    var datas = mutableListOf<RoomData>()
    lateinit var mainActivity: MainActivity
    lateinit var rv_roomlist : RecyclerView

    val BASE_URL= "http://192.168.0.17:4000/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(ChatAPI::class.java)
    val id :String? = LoginActivity.prefs.getString("id", null)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_navi_roomlist, container, false)
        setHasOptionsMenu(true)
        mainActivity = context as MainActivity
        rv_roomlist = view.findViewById(R.id.rv_roomlist)


        initRecycler()
        return view
    }
    private fun initRecycler() {
        chatroomadapter = ChatRoomAdapter(mainActivity)
        rv_roomlist.adapter = chatroomadapter

        val callGetChatRooms = api.getChatRooms(id=id!!)
        callGetChatRooms.enqueue(object : Callback<List<RoomData>> {
            override fun onResponse(
                call: Call<List<RoomData>>,
                response: Response<List<RoomData>>
            ) {
                if(response.code() == 200){
                    var iterator : Iterator<RoomData> = response.body()!!.iterator()
                    while(iterator.hasNext()){
                        var data = iterator.next()
                        datas.apply {
                            add(
                                RoomData(other_id=data.other_id, other_name = data.other_name, room_id = data.room_id,
                                item_id = data.item_id, item_name = data.item_name, reg_date = data.reg_date, content = data.content,
                                address = data.address, score = data.score, img_url = data.img_url)
                            )

                            chatroomadapter.datas = datas
                            chatroomadapter.notifyDataSetChanged()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<RoomData>>, t: Throwable) {

            }
        })
    }
}

class ChatRoomAdapter(private val context: Context): RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>(){
    var datas = mutableListOf<RoomData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_recycler,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int = datas.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val username: TextView = itemView.findViewById(R.id.username)
        private val address: TextView = itemView.findViewById(R.id.address)
        private val profile: ImageView = itemView.findViewById(R.id.profile)
        private val date : TextView = itemView.findViewById(R.id.date)
        private val content : TextView = itemView.findViewById(R.id.content)
        private val itemimg : ImageView = itemView.findViewById(R.id.itemimg)

        fun bind(item: RoomData) {
            username.text = item.other_name
            address.text = item.address
            date.text = item.reg_date
            content.text = item.content
            Glide.with(itemView).load("https://avatars.dicebear.com/api/big-smile/"+item.other_name+".png").into(profile)
            //Glide.with(itemView).load(item.img_url).into(item_img)
            Glide.with(itemView).load("https://placeimg.com/128/128/any").into(itemimg)

            itemView.setOnClickListener {
                Intent(context, ChatRoomActivity::class.java).apply {
                    putExtra("room_id", item.room_id)
                    putExtra("item_id", item.item_id)
                    putExtra("item_name", item.item_name)
                    putExtra("address", item.address)
                    putExtra("score", item.score)
                    putExtra("img_url", item.img_url)
                    putExtra("other_name", item.other_name)
                    putExtra("other_id", item.other_id)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }
}

