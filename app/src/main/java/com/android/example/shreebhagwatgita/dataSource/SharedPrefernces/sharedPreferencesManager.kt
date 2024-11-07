package com.android.example.shreebhagwatgita.dataSource.SharedPrefernces

import android.content.Context
import android.content.SharedPreferences

class sharedPreferencesManager(val context: Context) {


    val sharedPreferences:SharedPreferences by lazy {
        context.getSharedPreferences("saved_chapters",Context.MODE_PRIVATE)
    }

    fun getAllSavedChapters():Set<String>{
       return sharedPreferences.all.keys
    }

    fun putSavedChapterSP(key:String,value:Int) {
        sharedPreferences.edit().putInt(key,value).apply()
    }

    fun deleteSavedChapterFromSP(key: String){
        sharedPreferences.edit().remove(key).apply()
    }

    fun getAllSavedVerses():Set<String>{
        return sharedPreferences.all.keys
    }

    fun putSavedVerseSP(key:String,value:Int) {
        sharedPreferences.edit().putInt(key,value).apply()
    }
    fun deleteSavedVersesFromSP(key: String){
        sharedPreferences.edit().remove(key).apply()
    }
}