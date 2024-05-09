package com.delishstudio.delish.activities.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.delishstudio.delish.R
import com.delishstudio.delish.databinding.ActivityPaymentMethodBinding
import com.delishstudio.delish.databinding.LayoutPaymentMethodListBinding
import com.delishstudio.delish.model.PaymentMethod
import com.delishstudio.delish.model.UserManager

class PaymentMethodActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityPaymentMethodBinding
    private lateinit var mPaymentBinding: LayoutPaymentMethodListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPaymentMethodBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        supportActionBar?.hide()
        setContentView(mBinding.root)

        mPaymentBinding = mBinding.lyPaymentMethod

        updateButtons()
        onSetupButtons()
    }

    private fun updateButtons() {
        when(UserManager.Main.paymentMethod) {
            PaymentMethod.BCA -> {
                mPaymentBinding.txtBcaDefault.text = "Default"
                mPaymentBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_selected)
            }
            PaymentMethod.BNI -> {
                mPaymentBinding.txtBniDefault.text = "Default"
                mPaymentBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_selected)
            }
            PaymentMethod.GOPAY -> {
                mPaymentBinding.txtGopayDefault.text = "Default"
                mPaymentBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_selected)
            }
            PaymentMethod.LINKAJA -> {
                mPaymentBinding.txtLinkAjaDefault.text = "Default"
                mPaymentBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_selected)
            }
            PaymentMethod.MANDIRI -> {
                mPaymentBinding.txtMandiriDefault.text = "Default"
                mPaymentBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_selected)
            }
            PaymentMethod.SHOPEEPAY -> {
                mPaymentBinding.txtShopeePayDefault.text = "Default"
                mPaymentBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_selected)
            }
            PaymentMethod.QRIS -> {
                mPaymentBinding.txtQrisDefault.text = "Default"
                mPaymentBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_selected)
            }
            PaymentMethod.NONE -> {
                mPaymentBinding.txtQrisDefault.text = ""
                mPaymentBinding.txtBniDefault.text = ""
                mPaymentBinding.txtGopayDefault.text = ""
                mPaymentBinding.txtLinkAjaDefault.text = ""
                mPaymentBinding.txtShopeePayDefault.text = ""
                mPaymentBinding.txtMandiriDefault.text = ""
                mPaymentBinding.txtBcaDefault.text = ""
                mPaymentBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_not_selected)
                mPaymentBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_not_selected)
                mPaymentBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_not_selected)
                mPaymentBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_not_selected)
                mPaymentBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_not_selected)
                mPaymentBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_not_selected)
                mPaymentBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_not_selected)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        UserManager.Update()
    }

    private fun onSetupButtons() {
        mBinding.btBack.setOnClickListener{
            UserManager.Update()
            super.onBackPressed()
        }

        mPaymentBinding.btBca.setOnClickListener{
            UserManager.Main.paymentMethod = PaymentMethod.BCA
            updateButtons()
            mPaymentBinding.txtBniDefault.text = ""
            mPaymentBinding.txtGopayDefault.text = ""
            mPaymentBinding.txtQrisDefault.text = ""
            mPaymentBinding.txtLinkAjaDefault.text = ""
            mPaymentBinding.txtShopeePayDefault.text = ""
            mPaymentBinding.txtMandiriDefault.text = ""
            mPaymentBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }
        mPaymentBinding.btGopay.setOnClickListener{
            UserManager.Main.paymentMethod = PaymentMethod.GOPAY
            updateButtons()
            mPaymentBinding.txtBniDefault.text = ""
            mPaymentBinding.txtQrisDefault.text = ""
            mPaymentBinding.txtLinkAjaDefault.text = ""
            mPaymentBinding.txtShopeePayDefault.text = ""
            mPaymentBinding.txtMandiriDefault.text = ""
            mPaymentBinding.txtBcaDefault.text = ""
            mPaymentBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }
        mPaymentBinding.btBni.setOnClickListener{
            UserManager.Main.paymentMethod = PaymentMethod.BNI
            updateButtons()
            mPaymentBinding.txtGopayDefault.text = ""
            mPaymentBinding.txtQrisDefault.text = ""
            mPaymentBinding.txtLinkAjaDefault.text = ""
            mPaymentBinding.txtShopeePayDefault.text = ""
            mPaymentBinding.txtMandiriDefault.text = ""
            mPaymentBinding.txtBcaDefault.text = ""
            mPaymentBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }
        mPaymentBinding.btQris.setOnClickListener{
            UserManager.Main.paymentMethod = PaymentMethod.QRIS
            updateButtons()
            mPaymentBinding.txtBniDefault.text = ""
            mPaymentBinding.txtGopayDefault.text = ""
            mPaymentBinding.txtLinkAjaDefault.text = ""
            mPaymentBinding.txtShopeePayDefault.text = ""
            mPaymentBinding.txtMandiriDefault.text = ""
            mPaymentBinding.txtBcaDefault.text = ""
            mPaymentBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }
        mPaymentBinding.btLinkAja.setOnClickListener{
            UserManager.Main.paymentMethod = PaymentMethod.LINKAJA
            updateButtons()
            mPaymentBinding.txtBniDefault.text = ""
            mPaymentBinding.txtGopayDefault.text = ""
            mPaymentBinding.txtQrisDefault.text = ""
            mPaymentBinding.txtShopeePayDefault.text = ""
            mPaymentBinding.txtMandiriDefault.text = ""
            mPaymentBinding.txtBcaDefault.text = ""
            mPaymentBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }
        mPaymentBinding.btShopeePay.setOnClickListener{
            UserManager.Main.paymentMethod = PaymentMethod.SHOPEEPAY
            updateButtons()
            mPaymentBinding.txtBniDefault.text = ""
            mPaymentBinding.txtGopayDefault.text = ""
            mPaymentBinding.txtQrisDefault.text = ""
            mPaymentBinding.txtLinkAjaDefault.text = ""
            mPaymentBinding.txtMandiriDefault.text = ""
            mPaymentBinding.txtBcaDefault.text = ""
            mPaymentBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }
        mPaymentBinding.btMandiri.setOnClickListener{
            UserManager.Main.paymentMethod = PaymentMethod.MANDIRI
            updateButtons()
            mPaymentBinding.txtBniDefault.text = ""
            mPaymentBinding.txtGopayDefault.text = ""
            mPaymentBinding.txtQrisDefault.text = ""
            mPaymentBinding.txtLinkAjaDefault.text = ""
            mPaymentBinding.txtShopeePayDefault.text = ""
            mPaymentBinding.txtBcaDefault.text = ""
            mPaymentBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }
    }
}
