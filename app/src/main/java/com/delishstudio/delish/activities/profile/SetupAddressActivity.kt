package com.delishstudio.delish.activities.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.delishstudio.delish.databinding.ActivitySetupAddressBinding
import com.delishstudio.delish.model.AddressModel
import com.delishstudio.delish.model.UserManager
import com.delishstudio.delish.system.DatabaseStrRef
import com.delishstudio.delish.system.Utils
import com.delishstudio.delish.system.LocationUtils
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase

class SetupAddressActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySetupAddressBinding
    private lateinit var mCurrentLatLng: LatLng

    companion object {
        val MAP_PINPOINT_REQUEST_CODE: Int = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySetupAddressBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setup()
    }

    private fun setup() {
        val editIdx = intent.getIntExtra("editIdx", -1)

        if(editIdx != -1 && UserManager.Main.addressList.isNotEmpty()) {
            val address = UserManager.Main.addressList.get(editIdx)
            mBinding.etLabel.setText(address.label)
            mBinding.etReceipentName.setText(address.receipentName)
            mBinding.etReceipentPhone.setText(address.receipentPhone)
            mBinding.etAddressDetails.setText(address.addressDetails)
            mBinding.txtPinpointAddress.setText(address.completeAddress)
            mCurrentLatLng = LatLng(address.latitude, address.longitude)

            mBinding.txtHeaderTitle.text = "Ubah Alamat"
            mBinding.txtSave.text = "Simpan"
        }

        mBinding.btBack.setOnClickListener{
            super.onBackPressedDispatcher.onBackPressed()
        }

        mBinding.btEditPinpoint.setOnClickListener{
            val intent = Intent(this, MapPinpointActivity::class.java)
            if(editIdx != -1 && UserManager.Main.addressList.isNotEmpty()) {
                val address = UserManager.Main.addressList.get(editIdx)
                val latLng = LatLng(address.latitude, address.longitude)
                intent.putExtra("userLatLng", latLng)
            }
            startActivityForResult(intent, MAP_PINPOINT_REQUEST_CODE)
        }

        mBinding.btSave.setOnClickListener {
            val label = mBinding.etLabel.text.toString()
            val name = mBinding.etReceipentName.text.toString()
            val phone = mBinding.etReceipentPhone.text.toString()
            val addressDetails = mBinding.etAddressDetails.text.toString()
            val pinPointAddress = mBinding.txtPinpointAddress.text.toString()

            if (pinPointAddress.isNotEmpty() && label.isNotEmpty()
                && name.isNotEmpty() && phone.isNotEmpty() && addressDetails.isNotEmpty()) {
                if(editIdx != -1) {
                    val address = UserManager.Main.addressList.get(editIdx)
                    address.label = label
                    address.receipentName = name
                    address.receipentPhone = phone
                    address.addressDetails = addressDetails
                    address.completeAddress = pinPointAddress
                    address.latitude = mCurrentLatLng.latitude
                    address.longitude = mCurrentLatLng.longitude
                }else {
                    val address = AddressModel(label, name, phone, addressDetails)
                    address.completeAddress = pinPointAddress
                    address.latitude = mCurrentLatLng.latitude
                    address.longitude = mCurrentLatLng.longitude
                    UserManager.Main.addressList.add(address)
                }

                val addressListRef = FirebaseDatabase.getInstance()
                    .getReference("${DatabaseStrRef.USERS}/${UserManager.Main.userId}/AddressList")

                for(adr in UserManager.Main.addressList) {
                    if(adr.id == null)
                        adr.id = addressListRef.push().key ?: " "
                    addressListRef.child(adr.id!!).setValue(adr)
                }

                val intent = Intent(this, SavedAddressActivity::class.java)
                startActivity(intent)
                finish()
            }
            else  {
                Utils.LongToastText(this, "Mohon lengkapi alamat")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MAP_PINPOINT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val latLng = data?.getParcelableExtra<LatLng>("selectedAddress")
            if (latLng != null) {
                val address = LocationUtils.getFullAddress(this, latLng)
                mBinding.txtPinpointAddress.text = address
                mCurrentLatLng = latLng
            }
        }
    }
}