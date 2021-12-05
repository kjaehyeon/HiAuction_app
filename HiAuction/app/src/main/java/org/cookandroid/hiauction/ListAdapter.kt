package org.cookandroid.hiauction

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ListAdapter(val context: Context, val ItemList: ArrayList<Item>) : BaseAdapter()
{
    override fun getCount(): Int {
        return ItemList.size
    }
    override fun getItem(position: Int): Any {
        return ItemList[position]
    }
    override fun getItemId(position: Int): Long {
        return 0
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View = LayoutInflater.from(context).inflate(R.layout.card, null)
        val id : Int
        val name = view.findViewById<TextView>(R.id.Item_name)
        val location = view.findViewById<TextView>(R.id.Item_location)
        val bidprice = view.findViewById<TextView>(R.id.Bidprice)
        val imprice = view.findViewById<TextView>(R.id.Imprice)
        val date = view.findViewById<TextView>(R.id.Regdate)
        id = ItemList[position].id
        name.text = ItemList[position].name
        location.text = ItemList[position].location
        bidprice.text = "현재입찰가 " + ItemList[position].bidprice
        imprice.text = "즉시구매가 " + ItemList[position].imprice
        date.text = ItemList[position].date.toString() + "일전"
        return view;
    }
}

