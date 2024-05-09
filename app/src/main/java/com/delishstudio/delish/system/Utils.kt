package com.delishstudio.delish.system

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.delishstudio.delish.R
import com.delishstudio.delish.activities.SetupShop
import com.delishstudio.delish.model.ShopModel
import java.text.NumberFormat
import java.util.Locale

class Utils {
    companion object {

        fun getShop(id: String): ShopModel? {
            for (shop in SetupShop.shops) {
                if (shop.id == id) {
                    return shop
                }
            }
            return null
        }

        fun LongToastText(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
        fun ShortToastText(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun idFormatedCurrency(value: Int): String {
            val formatter = NumberFormat.getInstance(Locale.getDefault())
            return "Rp${formatter.format(value)}"
        }
    }
}

class DatabaseStrRef {
    companion object {
        val USERS: String = "Users"
        val VOUCHERS: String = "Vouchers"
        val SHOPS: String = "Shops"
        val EMAIL: String = "email"
    }
}

class ActivityUtils: AppCompatActivity() {
    companion object {
        fun navigateToFragment(activity: AppCompatActivity, fragment: Fragment) {
            activity.supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
        }
    }
}