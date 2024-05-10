package com.delishstudio.delish.activities.checkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.delishstudio.delish.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btBack.setOnClickListener {
            super.onBackPressedDispatcher.onBackPressed()
        }
    }
}