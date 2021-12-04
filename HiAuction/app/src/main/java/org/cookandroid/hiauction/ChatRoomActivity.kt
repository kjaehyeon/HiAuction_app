package org.cookandroid.hiauction

import android.content.Context
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.cookandroid.hiauction.fragment.ChatRoomAdapter

class ChatRoomActivity : AppCompatActivity() {
    lateinit var chatAdapter: ChatAdapter
    val datas = mutableListOf<Chatdata>()
    lateinit var rv_chatlist : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        rv_chatlist = findViewById(R.id.chatlist)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initRecycler()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
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
        chatAdapter = ChatAdapter()
        rv_chatlist.adapter = chatAdapter

        datas.apply {
            add(Chatdata(chat_id = 1, s_id="aaa", r_id="bbb", room_id = 1, reg_date = "오후 9:12",
            content="안녕하세요", type=1))
            add(Chatdata(chat_id = 1, s_id="aaa", r_id="bbb", room_id = 1, reg_date = "오후 9:12",
                content="안녕하세요", type=2))
            add(Chatdata(chat_id = 1, s_id="aaa", r_id="bbb", room_id = 1, reg_date = "오후 9:12",
                content="안녕하세요", type=3))

            chatAdapter.datas = datas
            chatAdapter.notifyDataSetChanged()
        }

    }
}
data class Chatdata(
    val chat_id : Int,
    val s_id : String,
    val r_id : String,
    val room_id : Int,
    val reg_date : String,
    val content : String,
    val type : Int
)
class ChatAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var datas = mutableListOf<Chatdata>()

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
            Glide.with(itemView).load("https://avatars.dicebear.com/api/big-smile/"+"옥션이1"+".png").into(profile)
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