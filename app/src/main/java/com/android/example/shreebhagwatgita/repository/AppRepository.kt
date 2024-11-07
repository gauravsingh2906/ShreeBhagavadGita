package com.android.example.shreebhagwatgita.repository

import android.bluetooth.BluetoothHidDevice
import androidx.lifecycle.LiveData
import com.android.example.shreebhagwatgita.Models.ChaptersItem
import com.android.example.shreebhagwatgita.dataSource.ApiUtilities
import com.android.example.shreebhagwatgita.dataSource.SharedPrefernces.sharedPreferencesManager
import com.android.example.shreebhagwatgita.dataSource.room.SavedChapters
import com.android.example.shreebhagwatgita.dataSource.room.SavedChaptersDao
import com.android.example.shreebhagwatgita.dataSource.room.SavedVerse
import com.android.example.shreebhagwatgita.dataSource.room.SavedVersesDao
import com.android.example.shreebhagwatgita.versesItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository(val savedChaptersDao: SavedChaptersDao,val savedVersesDao: SavedVersesDao,val sharedPreferencesManager: sharedPreferencesManager) {

     fun getAllChapter(): Flow<List<ChaptersItem>> = callbackFlow {

         val callback = object : Callback<List<ChaptersItem>> {
             override fun onResponse(call: Call<List<ChaptersItem>>, response: Response<List<ChaptersItem>>) {


                 if(response.isSuccessful&&response.body()!=null) {
                     trySend(response.body()!!)
                     close()
                 }

             }

             override fun onFailure(p0: Call<List<ChaptersItem>>, t: Throwable) {
                 close(t)
             }

         }

         ApiUtilities.api.getAllChapter().enqueue(callback)
         awaitClose {  }
     }

    fun getVerses(chapterNumber:Int) :Flow<List<versesItem>> = callbackFlow {
        val callback = object :Callback<List<versesItem>>{
            override fun onResponse(call: Call<List<versesItem>>, response: Response<List<versesItem>>) {
                if(response.isSuccessful&&response.body()!=null) {
                    trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(p0: Call<List<versesItem>>, t: Throwable) {
                close(t)
            }

        }
        ApiUtilities.api.getVerses(chapterNumber).enqueue(callback)
        awaitClose {  }
    }

    fun getAParticularVerse(chapterNumber: Int,verseNumber:Int):Flow<versesItem> = callbackFlow {
        val callBack = object :Callback<versesItem> {
            override fun onResponse(p0: Call<versesItem>, response: Response<versesItem>) {
                if(response.isSuccessful&&response.body()!=null) {
                    trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(p0: Call<versesItem>, t: Throwable) {
                close(t)
            }

        }
        ApiUtilities.api.getAParticularVerse(chapterNumber,verseNumber).enqueue(callBack)
        awaitClose {  }
    }

    // saved chapters
    suspend fun insertChapters(savedChapters: SavedChapters)= savedChaptersDao.insertChapters(savedChapters)

    fun getSavedChapters(): LiveData<List<SavedChapters>> = savedChaptersDao.getSavedChapters()
    fun getAParticularChapter(chapter_number:Int):LiveData<SavedChapters> = savedChaptersDao.getAParticularChapter(chapter_number)
    suspend  fun deleteChapter(id:Int)=savedChaptersDao.deleteChapter(id)

    // saved verses

    suspend fun insertEnglishChapters(versesInEnglish: SavedVerse) = savedVersesDao.insertEnglishChapters(versesInEnglish)

    fun getAllEnglishVerse() : LiveData<List<SavedVerse>> = savedVersesDao.getAllEnglishVerse()


    fun getParticularVerse(chapter_number: Int,verseNumber:Int):LiveData<SavedVerse> = savedVersesDao.getParticularVerse(chapter_number,verseNumber)

   suspend  fun deleteAParticularVerse(chapter_number:Int,verseNumber: Int) = savedVersesDao.deleteAParticularVerse(chapter_number,verseNumber)

   // saved chapters in sp

    fun deleteSavedChapterFromSP(key: String)=sharedPreferencesManager.deleteSavedChapterFromSP(key)
    fun getAllSavedChapters():Set<String> = sharedPreferencesManager.getAllSavedChapters()
    fun putSavedChapterSP(key:String,value:Int) = sharedPreferencesManager.putSavedChapterSP(key,value)

    // for verses
    fun getAllSavedVerses():Set<String> = sharedPreferencesManager.getAllSavedVerses()
    fun putSavedVerseSP(key:String,value:Int) = sharedPreferencesManager.putSavedVerseSP(key, value)
    fun deleteSavedVersesFromSP(key: String) = sharedPreferencesManager.deleteSavedVersesFromSP(key)

}