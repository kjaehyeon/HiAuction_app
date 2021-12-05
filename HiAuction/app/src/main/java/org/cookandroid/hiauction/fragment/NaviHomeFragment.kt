package org.cookandroid.hiauction.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.cookandroid.hiauction.LoginActivity.Companion.addresses
import org.cookandroid.hiauction.LoginActivity.Companion.prefs
import org.cookandroid.hiauction.R

class NaviHomeFragment : Fragment() {
    lateinit var test : TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragement_navi_home, container, false)
        test = view.findViewById(R.id.textView)
        test.text = prefs.getString("id", null)
        println(addresses[0])
        return view
    }
}