package com.android.example.shreebhagwatgita.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.example.shreebhagwatgita.databinding.ItemViewVersesBinding

class AdapterVerses(val onVerseItemClicked: (String, Int) -> Unit,val onClick: Boolean) :RecyclerView.Adapter<AdapterVerses.VerseViewHolder>() {


    inner class VerseViewHolder(val binding:ItemViewVersesBinding):RecyclerView.ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerseViewHolder {
       val binding = ItemViewVersesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VerseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: VerseViewHolder, position: Int) {
        val verses = differ.currentList[position]

        holder.binding.tvVerseNumber.text = "Verse ${position + 1}"
        holder.binding.tvVerse.text = verses

        if(onClick) {
            holder.binding.ivNext.visibility=View.VISIBLE
        } else{
            holder.binding.ivNext.visibility=View.GONE
        }


            holder.binding.ll.setOnClickListener {
                if (onClick) onVerseItemClicked(verses, position + 1)
            }


    }


}