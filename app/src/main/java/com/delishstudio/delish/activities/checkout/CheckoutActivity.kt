package com.delishstudio.delish.activities.checkout

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.R
import com.delishstudio.delish.activities.adapters.CheckoutListAdapter
import com.delishstudio.delish.activities.profile.SavedAddressActivity
import com.delishstudio.delish.databinding.ActivityCheckoutBinding
import com.delishstudio.delish.databinding.LayoutChangeAddressBottomSheetBinding
import com.delishstudio.delish.databinding.LayoutPaymentMethodBottomSheetBinding
import com.delishstudio.delish.databinding.LayoutPaymentMethodListBinding
import com.delishstudio.delish.databinding.LayoutSelectCourierBinding
import com.delishstudio.delish.model.DeliveryCourier
import com.delishstudio.delish.model.PaymentMethod
import com.delishstudio.delish.model.UserManager
import com.delishstudio.delish.system.LocationUtils
import com.delishstudio.delish.system.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class CheckoutActivity : AppCompatActivity(), CheckoutListAdapter.OnUpdateListener {
    private lateinit var mBinding: ActivityCheckoutBinding
    private lateinit var mPaymentIncludedLayoutBinding: LayoutPaymentMethodListBinding
    private lateinit var mPaymentBottomSheetBinding: LayoutPaymentMethodBottomSheetBinding
    private lateinit var mCourierBinding: LayoutSelectCourierBinding
    private lateinit var mAddressBinding: LayoutChangeAddressBottomSheetBinding
    private lateinit var mPaymentDialog: Dialog
    private lateinit var mCourierDialog: Dialog
    private lateinit var mAddressDialog: Dialog
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: CheckoutListAdapter
    private lateinit var mCurrentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val mPermissionCode: Int = 101

    private var isDialogShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        supportActionBar?.hide()
        mBinding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mPaymentDialog = Dialog(this)
        mPaymentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mPaymentBottomSheetBinding = LayoutPaymentMethodBottomSheetBinding.inflate(layoutInflater)
        mPaymentDialog.setContentView(mPaymentBottomSheetBinding.root)
        mPaymentIncludedLayoutBinding = mPaymentBottomSheetBinding.lyPaymentMethod

        mCourierDialog = Dialog(this)
        mCourierDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mCourierBinding = LayoutSelectCourierBinding.inflate(layoutInflater)
        mCourierDialog.setContentView(mCourierBinding.root)

        mAddressDialog = Dialog(this)
        mAddressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mAddressBinding = LayoutChangeAddressBottomSheetBinding.inflate(layoutInflater)
        mAddressDialog.setContentView(mAddressBinding.root)

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this)

        fetchLocation()

        setupAdapter()
        onSetupButtons()
        updatePaymentMethod()
        updateCourier()

        mBinding.txtUserName.text = UserManager.Main.userName
        mBinding.checkoutUserPhoneNumber.text = UserManager.Main.phone
        mBinding.txtUserAddress.text = UserManager.Main.getMainAddress()
        mBinding.txtTotalCost.text = Utils.idFormatedCurrency(UserManager.Main.transaction.totalCost)

        val tr = UserManager.Main.transaction
        if (tr.voucher != null) {
            if (tr.totalCost > tr.voucher!!.minSpend) {

                var disc = tr.totalCost
                if (disc > tr.voucher!!.maxSpend) {
                    disc = tr.voucher!!.maxSpend
                }

                val rate = tr.voucher!!.discountPercent.toFloat() / 100.0f
                tr.discountInPrice = (disc.toFloat() * rate).toInt()

                mBinding.txtTotalDiscountCost.text = "${Utils.idFormatedCurrency(tr.totalCost-tr.discountInPrice)}"
                mBinding.txtVoucherCode.text = tr.voucher!!.code
                mBinding.txtVoucherDiscount.text = "Kamu hemat ${Utils.idFormatedCurrency(tr.discountInPrice)}"
                mBinding.btCheckoutUseVoucher.text = "Voucher Digunakan"
                mBinding.txtTotalCost.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }

    private fun setupAdapter() {

        mRecyclerView = mBinding.rcCheckoutOrderedFood
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        mAdapter = CheckoutListAdapter()
        mAdapter.setOnUpdateListener(this)
        mRecyclerView.adapter = mAdapter
    }

    private fun onSetupButtons() {

        mBinding.btDeliveryMode.setOnClickListener{
            val density = resources.displayMetrics.density

            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            layoutParams.topMargin = (8 * density).toInt()
            mBinding.txtChooseCourier.layoutParams = layoutParams

            mBinding.btChooseCourier.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT

            val height = (30 * density).toInt()
            mBinding.btChooseCourier.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
            mBinding.btChooseCourier.layoutParams.height = height

            mBinding.btPickupMode.setTextColor(getColor(R.color.black_erie))
            mBinding.btPickupMode.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            mBinding.btDeliveryMode.setTextColor(getColor(R.color.white))
            mBinding.btDeliveryMode.backgroundTintList = ContextCompat.getColorStateList(this, R.color.dark_green)

            UserManager.Main.transaction.pickup = false
        }

        mBinding.btPickupMode.setOnClickListener{
            val layoutParams = LinearLayout.LayoutParams(0, 0)
            layoutParams.setMargins(0, 0, 0, 0)

            mBinding.txtChooseCourier.layoutParams = layoutParams
            mBinding.btChooseCourier.layoutParams = layoutParams

            mBinding.btPickupMode.setTextColor(getColor(R.color.white))
            mBinding.btPickupMode.backgroundTintList = ContextCompat.getColorStateList(this, R.color.dark_green)
            mBinding.btDeliveryMode.setTextColor(getColor(R.color.black_erie))
            mBinding.btDeliveryMode.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)

            UserManager.Main.transaction.pickup = true
        }

        mBinding.btOrderNow.setOnClickListener {
            setupPesanSekarangBtn()
        }
        mBinding.btChangeAddress.setOnClickListener {
            showChangeAddressDialog()
        }
        mBinding.btChoosePayment.setOnClickListener {
            showPaymentMethodDialog()
        }
        mBinding.btChooseCourier.setOnClickListener() {
            showChooseCourierDialog()
        }
        mBinding.btCheckoutUseVoucher.setOnClickListener {
            val intent = Intent(this, VoucherActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupPesanSekarangBtn() {

        if(UserManager.Main.transaction.foodList.isEmpty()) {
            Utils.ShortToastText(this, "Tambahkan belanja dahulu")
            return
        }

        if (UserManager.Main.courierMethod == DeliveryCourier.NONE || UserManager.Main.paymentMethod == PaymentMethod.NONE) {
            Utils.ShortToastText(this, "Pilih kurir/pembayaran dahulu")
            return
        }

        val tr = UserManager.Main.transaction

        if (tr.pickup) {
            tr.deliveryCost = 0
        } else {
            val shop = Utils.getShop(tr.foodList[0].shopId)
            tr.deliveryCost = shop!!.deliveryCost
        }

        val intent = Intent(this, OrderStatusActivity::class.java)
        startActivity(intent)
    }

    override fun onListUpdate() {
        val totalCostTextView: TextView = mBinding.txtTotalCost
        totalCostTextView.text = Utils.idFormatedCurrency(UserManager.Main.transaction.totalCost)
    }

    private fun showChangeAddressDialog() {
        if(isDialogShown) {
            return
        }

        mAddressBinding.btCurrentLocation.setOnClickListener {
            val address = LocationUtils.getFullAddress(this,
                LatLng(mCurrentLocation.latitude, mCurrentLocation.longitude))
            mBinding.txtUserAddress.text = address
            mAddressDialog.dismiss()
        }

        mAddressBinding.btSavedAddress.setOnClickListener {
            val intent = Intent(this, SavedAddressActivity::class.java)
            startActivity(intent)
        }

        mAddressDialog.setOnDismissListener{
            isDialogShown = false
        }

        mAddressDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mAddressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAddressDialog.window?.attributes?.windowAnimations = R.style.BottomSheetAnimation
        mAddressDialog.window?.setGravity(Gravity.BOTTOM)
        mAddressDialog.show()

        isDialogShown = true
    }

    private fun updatePaymentMethod() {
        when(UserManager.Main.paymentMethod) {
            PaymentMethod.BCA -> {
                mPaymentIncludedLayoutBinding.txtBcaDefault.text = "Default"
                mPaymentIncludedLayoutBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_selected)
                mBinding.btChoosePayment.text = "BCA"
            }
            PaymentMethod.BNI -> {
                mPaymentIncludedLayoutBinding.txtBniDefault.text = "Default"
                mPaymentIncludedLayoutBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_selected)
                mBinding.btChoosePayment.text = "BNI"
            }
            PaymentMethod.GOPAY -> {
                mPaymentIncludedLayoutBinding.txtGopayDefault.text = "Default"
                mPaymentIncludedLayoutBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_selected)
                mBinding.btChoosePayment.text = "Gopay"
            }
            PaymentMethod.LINKAJA -> {
                mPaymentIncludedLayoutBinding.txtLinkAjaDefault.text = "Default"
                mPaymentIncludedLayoutBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_selected)
                mBinding.btChoosePayment.text = "LinkAja"
            }
            PaymentMethod.MANDIRI -> {
                mPaymentIncludedLayoutBinding.txtMandiriDefault.text = "Default"
                mPaymentIncludedLayoutBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_selected)
                mBinding.btChoosePayment.text = "Mandiri"
            }
            PaymentMethod.SHOPEEPAY -> {
                mPaymentIncludedLayoutBinding.txtShopeePayDefault.text = "Default"
                mPaymentIncludedLayoutBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_selected)
                mBinding.btChoosePayment.text = "ShopeePay"
            }
            PaymentMethod.QRIS -> {
                mPaymentIncludedLayoutBinding.txtQrisDefault.text = "Default"
                mPaymentIncludedLayoutBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_selected)
                mBinding.btChoosePayment.text = "Qris"
            }
            PaymentMethod.NONE -> {
                mPaymentIncludedLayoutBinding.txtQrisDefault.text = ""
                mPaymentIncludedLayoutBinding.txtBniDefault.text = ""
                mPaymentIncludedLayoutBinding.txtGopayDefault.text = ""
                mPaymentIncludedLayoutBinding.txtLinkAjaDefault.text = ""
                mPaymentIncludedLayoutBinding.txtShopeePayDefault.text = ""
                mPaymentIncludedLayoutBinding.txtMandiriDefault.text = ""
                mPaymentIncludedLayoutBinding.txtBcaDefault.text = ""
                mPaymentIncludedLayoutBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_not_selected)
                mPaymentIncludedLayoutBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_not_selected)
                mPaymentIncludedLayoutBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_not_selected)
                mPaymentIncludedLayoutBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_not_selected)
                mPaymentIncludedLayoutBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_not_selected)
                mPaymentIncludedLayoutBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_not_selected)
                mPaymentIncludedLayoutBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_not_selected)
                mBinding.btChoosePayment.text = "Pilih Pembayaran"
            }
        }
    }

    private fun showPaymentMethodDialog() {

        if(isDialogShown) {
            return
        }

        mPaymentIncludedLayoutBinding.btBca.setOnClickListener{
            UserManager.Main.paymentMethod = PaymentMethod.BCA
            updatePaymentMethod()
            mPaymentIncludedLayoutBinding.txtBniDefault.text = ""
            mPaymentIncludedLayoutBinding.txtGopayDefault.text = ""
            mPaymentIncludedLayoutBinding.txtQrisDefault.text = ""
            mPaymentIncludedLayoutBinding.txtLinkAjaDefault.text = ""
            mPaymentIncludedLayoutBinding.txtShopeePayDefault.text = ""
            mPaymentIncludedLayoutBinding.txtMandiriDefault.text = ""
            mPaymentIncludedLayoutBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }
        mPaymentIncludedLayoutBinding.btGopay.setOnClickListener{
            UserManager.Main.paymentMethod = PaymentMethod.GOPAY
            updatePaymentMethod()
            mPaymentIncludedLayoutBinding.txtBniDefault.text = ""
            mPaymentIncludedLayoutBinding.txtQrisDefault.text = ""
            mPaymentIncludedLayoutBinding.txtLinkAjaDefault.text = ""
            mPaymentIncludedLayoutBinding.txtShopeePayDefault.text = ""
            mPaymentIncludedLayoutBinding.txtMandiriDefault.text = ""
            mPaymentIncludedLayoutBinding.txtBcaDefault.text = ""
            mPaymentIncludedLayoutBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }
        mPaymentIncludedLayoutBinding.btBni.setOnClickListener{
            UserManager.Main.paymentMethod = PaymentMethod.BNI
            updatePaymentMethod()
            mPaymentIncludedLayoutBinding.txtGopayDefault.text = ""
            mPaymentIncludedLayoutBinding.txtQrisDefault.text = ""
            mPaymentIncludedLayoutBinding.txtLinkAjaDefault.text = ""
            mPaymentIncludedLayoutBinding.txtShopeePayDefault.text = ""
            mPaymentIncludedLayoutBinding.txtMandiriDefault.text = ""
            mPaymentIncludedLayoutBinding.txtBcaDefault.text = ""
            mPaymentIncludedLayoutBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }
        mPaymentIncludedLayoutBinding.btQris.setOnClickListener{
            UserManager.Main.paymentMethod = PaymentMethod.QRIS
            updatePaymentMethod()
            mPaymentIncludedLayoutBinding.txtBniDefault.text = ""
            mPaymentIncludedLayoutBinding.txtGopayDefault.text = ""
            mPaymentIncludedLayoutBinding.txtLinkAjaDefault.text = ""
            mPaymentIncludedLayoutBinding.txtShopeePayDefault.text = ""
            mPaymentIncludedLayoutBinding.txtMandiriDefault.text = ""
            mPaymentIncludedLayoutBinding.txtBcaDefault.text = ""
            mPaymentIncludedLayoutBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }
        mPaymentIncludedLayoutBinding.btLinkAja.setOnClickListener{
            UserManager.Main.paymentMethod = PaymentMethod.LINKAJA
            updatePaymentMethod()
            mPaymentIncludedLayoutBinding.txtBniDefault.text = ""
            mPaymentIncludedLayoutBinding.txtGopayDefault.text = ""
            mPaymentIncludedLayoutBinding.txtQrisDefault.text = ""
            mPaymentIncludedLayoutBinding.txtShopeePayDefault.text = ""
            mPaymentIncludedLayoutBinding.txtMandiriDefault.text = ""
            mPaymentIncludedLayoutBinding.txtBcaDefault.text = ""
            mPaymentIncludedLayoutBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }
        mPaymentIncludedLayoutBinding.btShopeePay.setOnClickListener{
            UserManager.Main.paymentMethod = PaymentMethod.SHOPEEPAY
            updatePaymentMethod()
            mPaymentIncludedLayoutBinding.txtBniDefault.text = ""
            mPaymentIncludedLayoutBinding.txtGopayDefault.text = ""
            mPaymentIncludedLayoutBinding.txtQrisDefault.text = ""
            mPaymentIncludedLayoutBinding.txtLinkAjaDefault.text = ""
            mPaymentIncludedLayoutBinding.txtMandiriDefault.text = ""
            mPaymentIncludedLayoutBinding.txtBcaDefault.text = ""
            mPaymentIncludedLayoutBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgMandiriSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }
        mPaymentIncludedLayoutBinding.btMandiri.setOnClickListener{
            UserManager.Main.paymentMethod = PaymentMethod.MANDIRI
            updatePaymentMethod()
            mPaymentIncludedLayoutBinding.txtBniDefault.text = ""
            mPaymentIncludedLayoutBinding.txtGopayDefault.text = ""
            mPaymentIncludedLayoutBinding.txtQrisDefault.text = ""
            mPaymentIncludedLayoutBinding.txtLinkAjaDefault.text = ""
            mPaymentIncludedLayoutBinding.txtShopeePayDefault.text = ""
            mPaymentIncludedLayoutBinding.txtBcaDefault.text = ""
            mPaymentIncludedLayoutBinding.imgShopeePaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgLinkAjaSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgQrisSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgBniSelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgGopaySelected.setBackgroundResource(R.drawable.bt_not_selected)
            mPaymentIncludedLayoutBinding.imgBcaSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }

        mPaymentDialog.setOnDismissListener{
            isDialogShown = false
        }

        mPaymentDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mPaymentDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mPaymentDialog.window?.attributes?.windowAnimations = R.style.BottomSheetAnimation
        mPaymentDialog.window?.setGravity(Gravity.BOTTOM)

        mPaymentDialog.show()
        isDialogShown = true
    }

    private fun updateCourier() {
        when (UserManager.Main.courierMethod) {
            DeliveryCourier.GOSEND-> {
                mCourierBinding.txtCourierGosendDefault.text = "Default"
                mCourierBinding.imgGosendSelected.setBackgroundResource(R.drawable.bt_selected)
                mBinding.btChooseCourier.text = "Gosend"
            }
            DeliveryCourier.GRABEXPRESS-> {
                mCourierBinding.txtCourierGrabDefault.text = "Default"
                mCourierBinding.imgGrabSelected.setBackgroundResource(R.drawable.bt_selected)
                mBinding.btChooseCourier.text = "Grab Express"
            }
            DeliveryCourier.NONE-> {
                mCourierBinding.txtCourierGosendDefault.text = ""
                mCourierBinding.imgGosendSelected.setBackgroundResource(R.drawable.bt_not_selected)
                mCourierBinding.txtCourierGrabDefault.text = ""
                mCourierBinding.imgGrabSelected.setBackgroundResource(R.drawable.bt_not_selected)
                mBinding.btChooseCourier.text = "Pilih Kurir"
            }
        }

        UserManager.Main.transaction.courier = UserManager.Main.courierMethod
    }

    private fun showChooseCourierDialog() {

        if(isDialogShown) {
            return
        }

        updateCourier()

        mCourierBinding.btGosend.setOnClickListener {
            UserManager.Main.courierMethod = DeliveryCourier.GOSEND
            updateCourier()
            mCourierBinding.txtCourierGrabDefault.text = ""
            mCourierBinding.imgGrabSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }

        mCourierBinding.btGrab.setOnClickListener {
            UserManager.Main.courierMethod = DeliveryCourier.GRABEXPRESS
            updateCourier()
            mCourierBinding.txtCourierGosendDefault.text = ""
            mCourierBinding.imgGosendSelected.setBackgroundResource(R.drawable.bt_not_selected)
        }

        mCourierBinding.btConfirm.setOnClickListener{
            mCourierDialog.dismiss()
        }

        mCourierDialog.setOnDismissListener{
            isDialogShown = false
        }

        mCourierDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mCourierDialog.window?.setGravity(Gravity.CENTER)
        mCourierDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mCourierDialog.show()
        isDialogShown = true
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                mPermissionCode
            )
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                mCurrentLocation = location
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            mPermissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation()
                }
            }
        }
    }
}
