package org.cookandroid.hiauction.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_navi_roomlist.*
import org.cookandroid.hiauction.ChatRoomActivity
import org.cookandroid.hiauction.MainActivity
import org.cookandroid.hiauction.R
import org.cookandroid.hiauction.RoomData

class NaviRoomListFragment : Fragment() {
    lateinit var chatroomadapter: ChatRoomAdapter
    val datas = mutableListOf<RoomData>()
    lateinit var mainActivity: MainActivity
    lateinit var rv_roomlist : RecyclerView

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

        datas.apply {
            add(RoomData(Username = "옥션이1", Room_id = 1, It_id = 1, It_name = "노트북",Reg_data = "11월 26일",
            Content = "안녕하세요", Address_name = "동인동",Avg_score = 4.8f, Item_img_url = "url"))
            add(RoomData(Username = "옥션이1", Room_id = 1, It_id = 1, It_name = "노트북",Reg_data = "11월 26일",
                Content = "안녕하세요", Address_name = "동인동",Avg_score = 4.8f, Item_img_url = "url"))

            chatroomadapter.datas = datas
            chatroomadapter.notifyDataSetChanged()
        }
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
            username.text = item.Username
            address.text = item.Address_name
            date.text = item.Reg_data
            content.text = item.Content
            Glide.with(itemView).load("https://avatars.dicebear.com/api/big-smile/"+item.Username+".png").into(profile)
            //Glide.with(itemView).load(item.Item_img_url).into(item_img)
            Glide.with(itemView).load("https://placeimg.com/128/128/any").into(itemimg)

            itemView.setOnClickListener {
                Intent(context, ChatRoomActivity::class.java).apply {
                    putExtra("room_id", item.Room_id)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }
}

