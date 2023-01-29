package com.example.project_one

class Model1(name:String, price: String, imgId: Int){
    private var price:String
    private var name:String
    private var imgId:Int

    init{
        this.price = price
        this.name = name
        this.imgId = imgId
    }

    fun getPrice():String {
        return price
    }
    fun setPrice(price: String) {
        this.price = price
    }

    fun getImgId(): Int {
        return imgId
    }
    fun setImgId(imgId: Int) {
        this.imgId = imgId
    }

    fun getName():String {
        return name
    }
    fun setName(name: String) {
        this.name = name
    }
}