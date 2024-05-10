package com.delishstudio.delish.activities

import com.delishstudio.delish.model.FoodCategory
import com.delishstudio.delish.model.FoodModel
import com.delishstudio.delish.model.ShopModel
import com.delishstudio.delish.model.VoucherModel
import com.delishstudio.delish.system.DatabaseStrRef
import com.delishstudio.delish.viewmodel.DiscountItemListViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SetupShop {

    companion object {
        var shopsLoaded: Boolean = false
        var vouchersLoaded: Boolean = false

        var shops: ArrayList<ShopModel> = ArrayList()
        var vouchers: ArrayList<VoucherModel> = ArrayList()

        fun setupDummyVouchers() {
            loadAllVouchers { isLoaded ->
                if (!isLoaded) {
                    vouchers.add(VoucherModel("SAYANGBUMI", 15, 30000, 80000))
                    vouchers.add(VoucherModel("PASTIPROMO", 10, 50000, 300000))
                    vouchers.add(VoucherModel("SELAMATKANMAKANAN", 20, 15000, 50000))
                    vouchers.add(VoucherModel("PROMOMELIMPAH", 25, 10000, 300000))
                }

                val voucherRef = FirebaseDatabase.getInstance()
                    .getReference(DatabaseStrRef.VOUCHERS)

                for (voucher in vouchers) {
                    if (voucher.id.isEmpty()) {
                        voucher.id = voucherRef.push().key?: " "
                    }
                    voucherRef.child(voucher.id).setValue(voucher)
                }
            }
        }

        fun setupDummyShops() {

            loadAllShops { isLoaded ->
                if (!isLoaded) {
                    val shopA = ShopModel("Rumah Makan Padang")
                    shopA.latitude = -6.9771376
                    shopA.longitude = 107.6315028

                    shopA.addFood(
                        FoodModel(
                            "Rendang",
                            7,
                            60000,
                            FoodCategory.MAKANAN_BERAT,
                            "Porsi"
                        )
                    )
                    shopA.addFood(
                        FoodModel(
                            "Ayam Goreng",
                            12,
                            5000,
                            FoodCategory.MAKANAN_BERAT,
                            "Porsi"
                        )
                    )
                    shopA.addFood(FoodModel("Sayur", 2, 3000, FoodCategory.VEGAN, "Porsi"))
                    shopA.addFood(FoodModel("Es Teh", 6, 3500, FoodCategory.MINUMAN, "Porsi"))
                    shopA.addFood(FoodModel("Mystery", 8, 3500, FoodCategory.MYSTERY_BOX, "Box"))

                    val shopB = ShopModel("Indomaret")
                    shopB.latitude = -6.9764904
                    shopB.longitude = 107.6326818

                    shopB.addFood(FoodModel("Roti", 3, 5000, FoodCategory.CAMILAN, "Bungkus"))
                    shopB.addFood(FoodModel("CocaCola", 4, 7000, FoodCategory.MINUMAN, "Botol"))
                    shopB.addFood(FoodModel("Good Day", 10, 5000, FoodCategory.MINUMAN, "Botol"))
                    shopB.addFood(
                        FoodModel(
                            "Sambal ABC",
                            10,
                            7000,
                            FoodCategory.BAHAN_MAKANAN,
                            "Botol"
                        )
                    )
                    shopB.addFood(FoodModel("Oreo", 10, 7000, FoodCategory.CAMILAN, "Bungkus"))
                    shopB.addFood(FoodModel("Mystery", 10, 7000, FoodCategory.MYSTERY_BOX, "Box"))

                    val shopC = ShopModel("Resto Jangkar")
                    shopC.latitude = -6.9750274
                    shopC.longitude = 107.6322168

                    shopC.addFood(
                        FoodModel(
                            "Nasi Goreng",
                            6,
                            12000,
                            FoodCategory.MAKANAN_BERAT,
                            "Porsi"
                        )
                    )
                    shopC.addFood(
                        FoodModel(
                            "Babi Guling",
                            12,
                            15000,
                            FoodCategory.NON_HALAL,
                            "Porsi"
                        )
                    )
                    shopC.addFood(
                        FoodModel(
                            "Ayam Geprek",
                            3,
                            10000,
                            FoodCategory.MAKANAN_BERAT,
                            "Porsi"
                        )
                    )
                    shopC.addFood(FoodModel("Softdrink", 6, 5000, FoodCategory.MINUMAN, "Botol"))
                    shopC.addFood(
                        FoodModel(
                            "Ayam Bakar",
                            12,
                            12000,
                            FoodCategory.MAKANAN_BERAT,
                            "Porsi"
                        )
                    )
                    shopC.addFood(
                        FoodModel(
                            "Mie Gacoan",
                            4,
                            10000,
                            FoodCategory.MAKANAN_BERAT,
                            "Porsi"
                        )
                    )
                    shopC.addFood(
                        FoodModel(
                            "Sayur Kangkung Spesial",
                            4,
                            8000,
                            FoodCategory.VEGAN,
                            "Porsi"
                        )
                    )
                    shopC.addFood(FoodModel("Brokoli", 4, 7000, FoodCategory.VEGAN, "Porsi"))
                    shopC.addFood(FoodModel("Mystery", 4, 12000, FoodCategory.MYSTERY_BOX, "Box"))

                    val shopD = ShopModel("KFC Buah batu")
                    shopD.latitude = -6.9662269
                    shopD.longitude = 107.633587

                    shopD.addFood(FoodModel("Mystery", 12, 12000, FoodCategory.MYSTERY_BOX, "Box"))
                    shopD.addFood(
                        FoodModel(
                            "Yakiniku Rice",
                            12,
                            23000,
                            FoodCategory.MAKANAN_BERAT,
                            "Porsi"
                        )
                    )
                    shopD.addFood(FoodModel("Pepsi", 12, 8000, FoodCategory.MINUMAN, "Botol"))
                    shopD.addFood(
                        FoodModel(
                            "Spicy Chicken",
                            12,
                            14000,
                            FoodCategory.MAKANAN_BERAT,
                            "Porsi"
                        )
                    )
                    shopD.addFood(
                        FoodModel(
                            "Chicken Crispy",
                            7,
                            14000,
                            FoodCategory.MAKANAN_BERAT,
                            "Porsi"
                        )
                    )
                    shopD.addFood(
                        FoodModel(
                            "Cheese Burger",
                            3,
                            18000,
                            FoodCategory.CAMILAN,
                            "Porsi"
                        )
                    )

                    val shopE = ShopModel("McDonald's Podomoro")
                    shopE.latitude = -6.9754633
                    shopE.longitude = 107.6272899

                    shopE.addFood(
                        FoodModel(
                            "Burger",
                            3,
                            18000,
                            FoodCategory.MAKANAN_BERAT,
                            "Porsi"
                        )
                    )
                    shopE.addFood(
                        FoodModel(
                            "Kentang Goreng",
                            6,
                            9000,
                            FoodCategory.CAMILAN,
                            "Porsi"
                        )
                    )
                    shopE.addFood(FoodModel("Softdrink", 14, 5000, FoodCategory.MINUMAN, "Botol"))
                    shopE.addFood(
                        FoodModel(
                            "Big Mac",
                            10,
                            30000,
                            FoodCategory.MAKANAN_BERAT,
                            "Porsi"
                        )
                    )
                    shopE.addFood(
                        FoodModel(
                            "Ayam Gulai",
                            9,
                            25000,
                            FoodCategory.MAKANAN_BERAT,
                            "Porsi"
                        )
                    )

                    val foodA = FoodModel("Donat", 5, 5000, FoodCategory.CAMILAN)
                    foodA.discount = true
                    shopA.addFood(foodA)

                    val foodB = FoodModel("Burger", 7, 13000, FoodCategory.MAKANAN_BERAT)
                    foodB.discount = true
                    shopA.addFood(foodB)

                    val foodC = FoodModel("Bir", 12, 20000, FoodCategory.MINUMAN)
                    foodC.discount = true
                    shopA.addFood(foodC)

                    val foodD = FoodModel("Roti", 12, 8000, FoodCategory.CAMILAN)
                    foodD.discount = true
                    shopA.addFood(foodD)

                    shopA.rewardPoints = 10
                    shopA.deliveryCost = 12000
                    shopB.rewardPoints = 3
                    shopB.deliveryCost = 7000
                    shopC.rewardPoints = 12
                    shopC.deliveryCost = 17000
                    shopD.rewardPoints = 11
                    shopD.deliveryCost = 23000
                    shopE.rewardPoints = 0
                    shopE.deliveryCost = 11000

                    shops.add(shopA);
                    shops.add(shopB);
                    shops.add(shopC);
                    shops.add(shopD);
                    shops.add(shopE);

                    val shopRef = FirebaseDatabase.getInstance().getReference(DatabaseStrRef.SHOPS)

                    for (shop in shops) {
                        if (shop.id.isEmpty())
                            shop.id = shopRef.push().key?: " "

                        for (food in shop.foodList) {
                            if(food.id.isEmpty())
                                food.id = shopRef.push().key?: " "

                            food.shopId = shop.id
                            food.rating = 4.5
                        }

                        shopRef.child(shop.id).setValue(shop)
                    }
                    shopsLoaded = true
                }
            }
        }

        fun loadAllShops(callback: (Boolean) -> Unit) {
            if (!shopsLoaded) {
                val userListRef = FirebaseDatabase.getInstance().getReference(DatabaseStrRef.SHOPS)
                val query = userListRef.orderByKey()
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val shopsList = mutableListOf<ShopModel>()
                        for (snapshot in dataSnapshot.children) {
                            val shop = snapshot.getValue(ShopModel::class.java)
                            shop?.let {
                                shopsList.add(it)
                            }
                        }
                        shops.clear()
                        shops.addAll(shopsList)

                        if (shopsList.isNotEmpty()) {
                            DiscountItemListViewModel.foodList.clear()
                            for (food in shopsList[0].foodList) {
                                if (food.discount) {
                                    DiscountItemListViewModel.foodList.add(food)
                                }
                            }
                        }

                        callback(shopsList.isNotEmpty())
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callback(false)
                    }
                })



            } else {
                callback(true)
            }
        }

        private fun loadAllVouchers(callback: (Boolean) -> Unit) {
            if (!vouchersLoaded) {
                val userListRef = FirebaseDatabase.getInstance().getReference(DatabaseStrRef.VOUCHERS)
                val query = userListRef.orderByKey()
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val voucherList = mutableListOf<VoucherModel>()
                        for (snapshot in dataSnapshot.children) {
                            val voucher = snapshot.getValue(VoucherModel::class.java)
                            voucher?.let {
                                voucherList.add(it)
                            }
                        }
                        vouchers.clear()
                        vouchers.addAll(voucherList)
                        callback(voucherList.isNotEmpty())
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callback(false)
                    }
                })
            } else {
                callback(true)
            }
        }

    }
}
