package com.android.example.shreebhagwatgita.dataSource.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.example.shreebhagwatgita.Commentary
import com.android.example.shreebhagwatgita.Translation

@Dao
interface SavedChaptersDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChapters(savedChapters: SavedChapters)

  @Query("SELECT * FROM SavedChapters")
  fun getSavedChapters() : LiveData<List<SavedChapters>>

  @Query("DELETE FROM SavedChapters WHERE id=:id")
  suspend fun deleteChapter(id:Int)

  @Query("SELECT * FROM SavedChapters WHERE chapter_number = :chapter_number")
  fun getAParticularChapter(chapter_number:Int):LiveData<SavedChapters>
}


@Dao
interface SavedVersesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnglishChapters(versesInEnglish: SavedVerse)

    @Query("SELECT * FROM SavedVerses")
    fun getAllEnglishVerse() : LiveData<List<SavedVerse>>

    @Query("SELECT * FROM SavedVerses WHERE chapter_number=:chapter_number AND verse_number=:verseNumber")
    fun getParticularVerse(chapter_number: Int,verseNumber:Int):LiveData<SavedVerse>

    @Query("DELETE  FROM SavedVerses WHERE chapter_number = :chapter_number AND verse_number=:verseNumber")
    suspend fun deleteAParticularVerse(chapter_number:Int,verseNumber: Int)


}