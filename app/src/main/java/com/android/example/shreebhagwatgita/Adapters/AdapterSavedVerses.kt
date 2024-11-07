package com.android.example.shreebhagwatgita.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.example.shreebhagwatgita.dataSource.room.SavedVerse
import com.android.example.shreebhagwatgita.databinding.ItemViewVersesBinding

class AdapterSavedVerses(val onVerseItemClicked: (SavedVerse) -> Unit) :
    RecyclerView.Adapter<AdapterSavedVerses.SavedVerseViewHolder>() {


    inner class SavedVerseViewHolder(val binding: ItemViewVersesBinding): RecyclerView.ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<SavedVerse>(){
        override fun areItemsTheSame(oldItem: SavedVerse, newItem: SavedVerse): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: SavedVerse, newItem: SavedVerse): Boolean {
            return oldItem==newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedVerseViewHolder {
        val binding = ItemViewVersesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SavedVerseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SavedVerseViewHolder, position: Int) {
        val verses = differ.currentList[position]

        holder.binding.tvVerseNumber.text = "Verse ${verses.chapter_number}.${verses.verse_number}"
        holder.binding.tvVerse.text = verses.translations[0].description




        holder.binding.ll.setOnClickListener {
             onVerseItemClicked(verses)
        }


    }


}