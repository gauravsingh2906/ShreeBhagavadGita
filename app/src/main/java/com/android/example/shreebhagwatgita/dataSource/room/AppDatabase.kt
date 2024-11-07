package com.android.example.shreebhagwatgita.dataSource.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SavedChapters::class,SavedVerse::class], version = 2, exportSchema = false)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase:RoomDatabase() {


    abstract fun SavedChaptersDao():SavedChaptersDao
    abstract fun SavedVersesDao():SavedVersesDao



    companion object {
        @Volatile //here the background threads will get the updated value of this instance that why i use volatile annotation
        var INSTANCE:AppDatabase?=null

        fun getDatabaseInstance(context: Context):AppDatabase?{
            val tempInstance = INSTANCE
            if(INSTANCE!=null ) return tempInstance

            synchronized(this) {
                val roomDb = Room.databaseBuilder(context,AppDatabase::class.java,"AppDatabase").fallbackToDestructiveMigration().build()
                INSTANCE=roomDb
                return roomDb
            }

        }


    }


}