package com.android.example.shreebhagwatgita.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.example.shreebhagwatgita.Models.ChaptersItem
import com.android.example.shreebhagwatgita.databinding.ItemViewChaptersBinding
import com.android.example.shreebhagwatgita.viewModel.MainViewModel

class AdapterChapters(
    val onChapterIVClicked: (ChaptersItem) -> Unit,
    val onFavouriteClicked: (ChaptersItem) -> Unit,
    val showSaveButton: Boolean,
    val onFavouriteFilled: (ChaptersItem) -> Unit,
    val  viewModel: MainViewModel
) :RecyclerView.Adapter<AdapterChapters.ChaptersViewHolder>() {


    inner class ChaptersViewHolder(val binding:ItemViewChaptersBinding):RecyclerView.ViewHolder(binding.root)


    val diffUtil = object :DiffUtil.ItemCallback<ChaptersItem>(){
        override fun areItemsTheSame(oldItem: ChaptersItem, newItem: ChaptersItem): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: ChaptersItem, newItem: ChaptersItem): Boolean {
           return oldItem==newItem
        }

    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChaptersViewHolder {
        val binding = ItemViewChaptersBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChaptersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ChaptersViewHolder, position: Int) {
       val chapter = differ.currentList[position]

        holder.binding.apply {
            tvChapterNumber.text="Chapter ${chapter.chapter_number}"
            tvChapterTitle.text=chapter.name_translated
            tvChapterDescription.text=chapter.chapter_summary
            tvVersesCount.text=chapter.verses_count.toString()
        }

        val keys = viewModel.getAllSavedChapters()
        Log.d("TAG",keys.toString())

        if(!showSaveButton) {
            holder.binding.apply {
                ivFavorite.visibility=View.GONE
                ivFavoriteFilled.visibility=View.GONE
            }
        }
        else{
            if(keys.contains(chapter.chapter_number.toString())) {
                holder.binding.apply {
                    ivFavorite.visibility=View.GONE
                    ivFavoriteFilled.visibility=View.VISIBLE
                }
            } else{
                holder.binding.apply {
                    ivFavorite.visibility=View.VISIBLE
                    ivFavoriteFilled.visibility=View.GONE
                }
            }
        }

        holder.binding.ll.setOnClickListener {
            onChapterIVClicked(chapter)
        }

        holder.binding.apply {
            ivFavorite.setOnClickListener {
                ivFavoriteFilled.visibility=View.VISIBLE
                ivFavorite.visibility=View.GONE
                onFavouriteClicked(chapter)
            }
            ivFavoriteFilled.setOnClickListener {
                ivFavorite.visibility=View.VISIBLE
                ivFavoriteFilled.visibility=View.GONE

                onFavouriteFilled(chapter)

            }
        }


    }


}