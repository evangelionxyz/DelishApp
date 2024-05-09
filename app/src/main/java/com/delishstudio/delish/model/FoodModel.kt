package com.delishstudio.delish.model

data class FoodModel(
    var name: String = "",
    var quantity: Int = 0,
    var price: Int = 0,
    var cat: FoodCategory = FoodCategory.MAKANAN_BERAT,
    var unit: String = "Porsi"
) {
    var rating: Double = 0.0
    var quaUnit: String = unit
    var buyQuantity: Int = 0
    var rewardPoints: Int = 0
    var imgSrc: String = ""
    var imgId: Int = -1
    var distance: Double = 0.0
    var orderMsgToRestaurant: String = ""
    var id: String = ""
    var shopId: String = ""



    fun getCategoryString(): String {
        return when(cat){
            FoodCategory.MAKANAN_BERAT -> "Makanan Berat"
            FoodCategory.NON_HALAL     -> "Non Halal"
            FoodCategory.CAMILAN       -> "Camilan"
            FoodCategory.MINUMAN       -> "Minuman"
            FoodCategory.BAHAN_MAKANAN -> "Bahan Makanan"
            FoodCategory.VEGAN         -> "Vegan"
            FoodCategory.MYSTERY_BOX   -> "Mystery Box"
        }
    }


}
