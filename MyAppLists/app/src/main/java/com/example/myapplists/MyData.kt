package com.example.engquiz

import android.graphics.drawable.Drawable
import java.io.Serializable

data class MyData(var appLabel:String, var appClass:String, var appPack:String, var appIcon: Drawable):Serializable {
}