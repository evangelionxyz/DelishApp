package com.delishstudio.delish.activities.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.delishstudio.delish.databinding.ActivityChangeLanguageBinding
import com.delishstudio.delish.model.LanguagePref
import com.delishstudio.delish.model.UserManager

class ChangeLanguageActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityChangeLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityChangeLanguageBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btBack.setOnClickListener{
            super.onBackPressedDispatcher.onBackPressed()
        }

        mBinding.btEnglish.setOnClickListener {
            UserManager.Main.languagePref = LanguagePref.ENGLISH
            UserManager.Update()
            super.onBackPressedDispatcher.onBackPressed()
        }

        mBinding.btBahasaIndonesia.setOnClickListener {
            UserManager.Main.languagePref = LanguagePref.BAHASA
            UserManager.Update()
            super.onBackPressedDispatcher.onBackPressed()
        }


    }


}