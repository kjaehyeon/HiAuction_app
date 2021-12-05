package org.cookandroid.hiauction

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
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
        val view : View = LayoutInflater.from(context).inflate(R.layout.card, null)
        val item_name = view.findViewById<TextView>(R.id.Item_name)
        val bidprice = view.findViewById<TextView>(R.id.Bidprice)
        val imprice = view.findViewById<TextView>(R.id.Imprice)
        val location = view.findViewById<TextView>(R.id.Location)
        val date = view.findViewById<TextView>(R.id.Regdate)
        item_name.text = itemArr[position].name
        location.text = 
        bidprice.text = "현재입찰가 " + itemArr[position].current_price
        imprice.text = "즉시구매가 " + itemArr[position].immediate_price
        date.text = itemArr[position].created_date.toString() + "일전"
        return view;
    }
}

