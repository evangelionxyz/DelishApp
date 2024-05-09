package com.delishstudio.delish.activities.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.R
import com.delishstudio.delish.activities.SetupShop
import com.delishstudio.delish.activities.profile.SetupAddressActivity
import com.delishstudio.delish.databinding.RcSavedAddressListBinding
import com.delishstudio.delish.model.AddressModel
import com.delishstudio.delish.model.UserManager
import com.delishstudio.delish.system.DatabaseStrRef
import com.google.firebase.database.FirebaseDatabase

class SavedAddressListAdapter(var addressList: ArrayList<AddressModel>): RecyclerView.Adapter<SavedAddressListAdapter.AddressHolder>() {

    inner class AddressHolder(binding: RcSavedAddressListBinding): RecyclerView.ViewHolder(binding.root) {
        val label: TextView = binding.txtAddressLabel
        val recName: TextView = binding.txtReceipentName
        val phone: TextView = binding.txtReceipentPhone
        val completeAddress: TextView = binding.txtCompleteAddress
        val btEdit: CardView = binding.btEdit
        val btTrash: ImageView = binding.btTrash
        val mainFrame: LinearLayout = binding.mainFrame

        init {
            setupButtons()
            updateBackgroundResource()
        }

        private fun setupButtons() {
            val addressListRef = FirebaseDatabase.getInstance()
                .getReference("${DatabaseStrRef.USERS}/${UserManager.Main.userId}/AddressList")
            val userRef = FirebaseDatabase.getInstance()
                .getReference("${DatabaseStrRef.USERS}/${UserManager.Main.userId}")

            mainFrame.setOnClickListener {
                val position = bindingAdapterPosition
                val prevSelectedIndex = UserManager.Main.mainAddressIndex
                UserManager.Main.mainAddressIndex = position

                if(prevSelectedIndex >= 0)
                    updateBackgroundResource(prevSelectedIndex)

                updateUserLatLng(position)
                updateBackgroundResource(position)
                userRef.child("mainAddressIndex").setValue(UserManager.Main.mainAddressIndex)

                userRef.child("latitude").setValue(UserManager.Main.latitude)
                userRef.child("longitude").setValue(UserManager.Main.longitude)

                for (shop in SetupShop.shops) {
                    shop.updateDistance(UserManager.Main.latitude,
                        UserManager.Main.longitude)
                }
            }

            btEdit.setOnClickListener {
                val intent = Intent(itemView.context, SetupAddressActivity::class.java)
                intent.putExtra("editIdx", bindingAdapterPosition)
                itemView.context.startActivity(intent)
            }

            btTrash.setOnClickListener {
                val id = addressList.get(bindingAdapterPosition).id
                addressList.removeAt(bindingAdapterPosition)

                addressListRef.child(id!!).removeValue()
                UserManager.Main.mainAddressIndex = if(addressList.isEmpty()) -1 else 0

                if(UserManager.Main.mainAddressIndex >= 0) {
                    updateUserLatLng(UserManager.Main.mainAddressIndex)
                } else {
                    UserManager.Main.latitude = 0.0
                    UserManager.Main.longitude = 0.0
                }

                userRef.child("latitude").setValue(UserManager.Main.latitude)
                userRef.child("longitude").setValue(UserManager.Main.longitude)

                userRef.child("mainAddressIndex")
                    .setValue(UserManager.Main.mainAddressIndex)
                notifyItemRemoved(bindingAdapterPosition)

                for (shop in SetupShop.shops) {
                    shop.updateDistance(UserManager.Main.latitude,
                    UserManager.Main.longitude)
                }
            }
        }

        private fun updateBackgroundResource(position: Int = bindingAdapterPosition) {
            val isSelected = position == UserManager.Main.mainAddressIndex
            val bgRes = if (isSelected) R.drawable.bg_rd_light_green_with_stroke else R.drawable.bg_rd_white_with_stroke
            mainFrame.setBackgroundResource(bgRes)
        }

        private fun updateUserLatLng(index: Int) {
            UserManager.Main.latitude = addressList.get(index).latitude
            UserManager.Main.longitude = addressList.get(index).longitude
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressHolder {
        val binding = RcSavedAddressListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressHolder(binding)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: AddressHolder, position: Int) {
        val currentAddress = addressList[position]

        holder.label.text = currentAddress.label
        holder.recName.text = currentAddress.receipentName
        holder.phone.text = currentAddress.receipentPhone
        holder.completeAddress.text = currentAddress.completeAddress
    }
}