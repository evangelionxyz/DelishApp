package com.delishstudio.delish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.delishstudio.delish.databinding.ActivityMainBinding
import com.delishstudio.delish.system.ActivityUtils
import com.delishstudio.delish.fragments.ProfileFragment
import com.delishstudio.delish.fragments.HomeFragment
import com.delishstudio.delish.fragments.MysteryBoxFragment
import com.delishstudio.delish.fragments.TransactionFragment
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mBottomNavigationBar: AnimatedBottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        bottomNavigation()

        if (savedInstanceState == null) {
            ActivityUtils.navigateToFragment(this, HomeFragment())
        }
    }

    private fun bottomNavigation() {
        mBottomNavigationBar = mBinding.bottomNavigation

        mBottomNavigationBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(i: Int, lastTab: AnimatedBottomBar.Tab?, nI: Int, nTab: AnimatedBottomBar.Tab) {
                when (nTab.id) {
                    R.id.navigation_home -> {
                        ActivityUtils.navigateToFragment(this@MainActivity, HomeFragment())
                    }
                    R.id.navigation_mystery_box -> {
                        ActivityUtils.navigateToFragment(this@MainActivity, MysteryBoxFragment())
                    }
                    R.id.navigation_transactions -> {
                        ActivityUtils.navigateToFragment(this@MainActivity, TransactionFragment())
                    }
                    R.id.navigation_userprofile -> {
                        ActivityUtils.navigateToFragment(this@MainActivity, ProfileFragment())
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onDestroy()
    }
}