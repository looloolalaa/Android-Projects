package com.example.engquiz

import java.io.Serializable

data class MyData(var word:String, var mean:String, var check:Boolean=false):Serializable {
}