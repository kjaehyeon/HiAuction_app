package org.cookandroid.hiauction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.cookandroid.hiauction.fragment.NaviRoomListFragment
import org.cookandroid.hiauction.fragment.NaviHomeFragment
import org.cookandroid.hiauction.fragment.NaviMyPageFragment

class MainActivity : AppCompatActivity() {

    private val fl: FrameLayout by lazy {
        findViewById(R.id.fl_con)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bnv_main = findViewById<BottomNavigationView>(R.id.bnv_main)

        bnv_main.setOnItemSelectedListener { item->changeFragement(
            when(item.itemId){
                R.id.nav_home -> {
                    NaviHomeFragment()
                }
                R.id.nav_chat ->{
                    NaviRoomListFragment()
                }
                else-> {
                    NaviMyPageFragment()
                }
            }
        )
        true
        }
        bnv_main.selectedItemId = R.id.nav_home
    }
    private fun changeFragement(fragment : Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_con, fragment)
            .commit()
    }
}