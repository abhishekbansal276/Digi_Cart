package com.example.project_one

class AllCartModel {
    private lateinit var allProducts:String
    private lateinit var totalPrice:String
    private lateinit var totalWeight:String
    private lateinit var payOrNot:String
    private lateinit var id:String

    constructor(){}

    constructor( allProducts: String, totalPrice: String, totalWeight:String, payOrNot:String, id:String) {
        this.allProducts = allProducts
        this.totalPrice = totalPrice
        this.totalWeight = totalWeight
        this.payOrNot = payOrNot
        this.id  =id
    }

    fun getAllProducts():String{
        return allProducts
    }

    fun setAllProducts(allProducts:String){
        this.allProducts = allProducts
    }

    fun getTotalPrice():String{
        return totalPrice
    }

    fun setTotalPrice(totalPrice:String){
        this.totalPrice = totalPrice
    }

    fun getTotalWeight():String{
        return totalWeight
    }

    fun setTotalWeight(totalWeight:String){
        this.totalWeight = totalWeight
    }

    fun getPayOrNot():String{
        return payOrNot
    }

    fun setPayOrNot(payOrNot:String){
        this.payOrNot = payOrNot
    }

    fun getId():String{
        return id
    }

    fun setId(id:String){
        this.id = id
    }
}