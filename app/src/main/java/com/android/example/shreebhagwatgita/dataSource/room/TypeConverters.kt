package com.android.example.shreebhagwatgita.dataSource.room

import androidx.room.TypeConverter
import com.android.example.shreebhagwatgita.Commentary
import com.android.example.shreebhagwatgita.Translation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppTypeConverters {

    @TypeConverter
    fun fromListToString(list: List<String>):String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToList(string: String):List<String> {
        return Gson().fromJson(string,object :TypeToken<List<String>>(){}.type)
    }

    @TypeConverter
    fun fromTransToString(list: List<Translation>):String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToTrans(string: String):List<Translation> {
        return Gson().fromJson(string,object :TypeToken<List<Translation>>(){}.type)
    }

    @TypeConverter
    fun fromCommeToString(list: List<Commentary>):String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToComme(string: String):List<Commentary> {
        return Gson().fromJson(string,object :TypeToken<List<Commentary>>(){}.type)
    }

}