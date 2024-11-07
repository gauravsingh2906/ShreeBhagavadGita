package com.android.example.shreebhagwatgita.viewModel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.example.shreebhagwatgita.Models.ChaptersItem
import com.android.example.shreebhagwatgita.dataSource.SharedPrefernces.sharedPreferencesManager
import com.android.example.shreebhagwatgita.dataSource.room.AppDatabase
import com.android.example.shreebhagwatgita.dataSource.room.SavedChapters
import com.android.example.shreebhagwatgita.dataSource.room.SavedVerse
import com.android.example.shreebhagwatgita.repository.AppRepository
import com.android.example.shreebhagwatgita.versesItem
import kotlinx.coroutines.flow.Flow

class MainViewModel(application: Application): AndroidViewModel(application) {

    val savedChaptersDao= AppDatabase.getDatabaseInstance(application)?.SavedChaptersDao()
    val savedVersesDao= AppDatabase.getDatabaseInstance(application)?.SavedVersesDao()
    val sharedPreferencesManager = sharedPreferencesManager(application)
    val appRepository = AppRepository(savedChaptersDao!!,savedVersesDao!!,sharedPreferencesManager)


    fun getAllChapter(): Flow<List<ChaptersItem>> = appRepository.getAllChapter()

    fun getVerses(chapterNumber:Int) :Flow<List<versesItem>> = appRepository.getVerses(chapterNumber)

    fun getAParticularVerse(chapterNumber: Int,verseNumber:Int):Flow<versesItem> = appRepository.getAParticularVerse(chapterNumber,verseNumber)
    suspend fun deleteChapter(id:Int)=appRepository.deleteChapter(id)
   // saved chapters

    suspend fun insertChapters(savedChapters: SavedChapters)= appRepository.insertChapters(savedChapters)

    fun getSavedChapters(): LiveData<List<SavedChapters>> = appRepository.getSavedChapters() // this is our live data so we need to observer here
    fun getAParticularChapter(chapter_number:Int):LiveData<SavedChapters> = appRepository.getAParticularChapter(chapter_number)


    // saved verses

    suspend fun insertEnglishChapters(versesInEnglish: SavedVerse) = appRepository.insertEnglishChapters(versesInEnglish)

    fun getAllEnglishVerse() : LiveData<List<SavedVerse>> = appRepository.getAllEnglishVerse()


    fun getParticularVerse(chapter_number: Int,verseNumber:Int):LiveData<SavedVerse> = appRepository.getParticularVerse(chapter_number,verseNumber)

   suspend   fun deleteAParticularVerse(chapter_number:Int,verseNumber: Int) = appRepository.deleteAParticularVerse(chapter_number,verseNumber)


    // saved chapters in sp

        fun deleteSavedChapterFromSP(key: String)=appRepository.deleteSavedChapterFromSP(key)
        fun getAllSavedChapters():Set<String> = appRepository.getAllSavedChapters()
        fun putSavedChapterSP(key:String,value:Int) = appRepository.putSavedChapterSP(key,value)


    fun getAllSavedVerses():Set<String> = appRepository.getAllSavedVerses()
    fun putSavedVerseSP(key:String,value:Int) = appRepository.putSavedVerseSP(key, value)
    fun deleteSavedVersesFromSP(key: String) = appRepository.deleteSavedVersesFromSP(key)
}