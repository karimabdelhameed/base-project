package com.custom.bottomSheet

open class BottomSheetModel {
    //constructor(var id: Int = 0, var data: String = "", var isSelected: Boolean = false)
    var selectionID: Int? = 0
    var text: String? = ""
    var selected: Boolean? = false

    fun initialize(selectionID: Int, text: String?) {
        this.selectionID = selectionID
        this.text = text
    }
}