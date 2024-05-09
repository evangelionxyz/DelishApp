package com.delishstudio.delish.model

class VoucherModel(var code: String = "",
                   var discountPercent: Int = 0,
                   var minSpend: Int = 0,
                   var maxSpend: Int = 0) {

    var id: String = ""
}