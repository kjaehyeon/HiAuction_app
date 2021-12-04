package org.cookandroid.hiauction.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_navi_roomlist.*
import org.cookandroid.hiauction.R

class NaviRoomListFragment : Fragment() {
    lateinit var chatroomadapter: ChatRoomAdapter
    val datas = mutableListOf<RoomData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_navi_roomlist, container, false)
        setHasOptionsMenu(true)

        initRecycler()
        return view
    }
    private fun initRecycler() {
        chatroomadapter = ChatRoomAdapter()
        rv_roomlist.adapter = chatroomadapter

        datas.apply {
            add(RoomData(Username = "옥션이1", Room_id = 1, It_id = 1, It_name = "노트북",Reg_data = "11월 26일",
            Content = "안녕하세요", Address_name = "동인동",Avg_score = 4.8f, Item_img_url = "url"))

            chatroomadapter.datas = datas
            chatroomadapter.notifyDataSetChanged()
        }
    }
}

data class RoomData(
    val Username : String,
    val Room_id : Int,
    val It_id : Int,
    val It_name : String,
    val Reg_data : String,
    val Content : String,
    val Address_name : String,
    val Avg_score : Float,
    val Item_img_url : String
)

class ChatRoomAdapter(private val context: Context): RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>(){
    var datas = mutableListOf<RoomData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.room_recycler,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int = datas.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val username: TextView = itemView.findViewById(R.id.username)
        private val address: TextView = itemView.findViewById(R.id.address)
        private val profile: ImageView = itemView.findViewById(R.id.profile)
        private val date : TextView = itemView.findViewById(R.id.date)
        private val content : TextView = itemView.findViewById(R.id.content)
        private val item_img : ImageView = itemView.findViewById(R.id.item_img)

        fun bind(item: RoomData) {
            username.text = item.Username
            address.text = item.Address_name
            date.text = item.Reg_data
            content.text = item.Content
            Glide.with(itemView).load("https://avatars.dicebear.com/api/identicon/"+item.Username+".svg").into(profile)
            //Glide.with(itemView).load(item.Item_img_url).into(item_img)
            Glide.with(itemView).load("https://placeimg.com/128/128/any").into(item_img)
        }
    }
}
