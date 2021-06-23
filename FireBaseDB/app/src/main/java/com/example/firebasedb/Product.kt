package com.example.firebasedb

data class Product(var pid:Int, var pName:String, var pQuantity:Int) {
    constructor():this(0,"noInfo",0)
}