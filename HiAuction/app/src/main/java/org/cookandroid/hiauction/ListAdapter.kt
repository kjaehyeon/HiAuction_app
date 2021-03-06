package org.cookandroid.hiauction

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.cookandroid.hiauction.datas.ItemListData

class ListAdapter(val context: Context, val itemArr: ArrayList<ItemListData>) : BaseAdapter()
{
    override fun getCount(): Int {
        return itemArr.size
    }
    override fun getItem(position: Int): Any {
        return itemArr[position]
    }
    override fun getItemId(position: Int): Long {
        return 0
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        view = LayoutInflater.from(context).inflate(R.layout.card, null)
        val name = view.findViewById<TextView>(R.id.Item_name)
        val bidprice = view.findViewById<TextView>(R.id.Bidprice)
        val imprice = view.findViewById<TextView>(R.id.Imprice)
        val location = view.findViewById<TextView>(R.id.Item_location)
        val date = view.findViewById<TextView>(R.id.Regdate)
        val img = view.findViewById<ImageView>(R.id.Itemimage)
        name.text = itemArr[position].item_name
        bidprice.text = "현재입찰가 " + itemArr[position].current_price
        imprice.text = "즉시구매가 " + itemArr[position].immediate_price
        date.text = itemArr[position].created_date.toString()
        location.text = itemArr[position].address
        lateinit var mainActivity: MainActivity
        mainActivity = context as MainActivity
        Glide.with(mainActivity).load(itemArr[position].img_url).into(img)
        return view;
    }
}


