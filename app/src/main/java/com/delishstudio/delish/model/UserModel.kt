package com.delishstudio.delish.model

class UserModel(
    var userName: String = "",
    var phone: String = "",
    var email: String = "",
    var userId: String = ""
) {
    var transaction= TransactionModel()
    var transactionList: ArrayList<TransactionModel> =  ArrayList()
    var addressList: ArrayList<AddressModel> = ArrayList()
    var paymentMethod: PaymentMethod = PaymentMethod.GOPAY
    var courierMethod: DeliveryCourier = DeliveryCourier.NONE
    var languagePref: LanguagePref = LanguagePref.BAHASA
    var profileImageUrl: String = ""
    var rewardPoints: Int = 0
    var password: String = ""

    var mainAddressIndex: Int = -1

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    fun getMainAddress(): String? {
        if(addressList.isNotEmpty() && mainAddressIndex != -1) {
            return addressList.get(mainAddressIndex).completeAddress
        }
        return ""
    }
}