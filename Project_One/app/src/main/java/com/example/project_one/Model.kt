package com.example.project_one

class Model(itemName: String, itemWeight: String, itemPrice: String){
    private var itemName:String
    private var itemWeight:String
    private var itemPrice:String

    init{
        this.itemName = itemName
        this.itemWeight = itemWeight
        this.itemPrice = itemPrice
    }

    fun getItemName():String {
        return itemName;
    }
    fun setItemName(itemName: String) {
        this.itemName = itemName;
    }
    fun getItemWeight():String {
        return itemWeight;
    }
    fun setItemWeight(itemWeight: String) {
        this.itemWeight = itemWeight;
    }
    fun getItemPrice():String {
        return itemPrice;
    }
    fun setItemPrice(itemPrice: String) {
        this.itemPrice = itemPrice;
    }
}