package com.example.docscanner.core.util

import android.content.Context
import android.content.SharedPreferences
import androidx.activity.ComponentActivity


class PreferenceWrapper(val activity: ComponentActivity) {

    private var sharedPreferences: SharedPreferences? = null

    init {
        sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
    }

    fun getString(key:String): String{
        return sharedPreferences?.getString(key, "") ?: ""
    }

    fun putString(key: String, value: String){
        sharedPreferences?.let {
            with(it.edit()) {
                putString(key, value)
                apply()
            }
        }
    }

    fun putInt(key: String , value: Int){
        sharedPreferences?.let {
            with(it.edit()){
                putInt(key, value)
                apply()
            }
        }
    }

    fun getInt(key: String): Int{
        return sharedPreferences?.getInt(key, -1) ?: -1
    }
}