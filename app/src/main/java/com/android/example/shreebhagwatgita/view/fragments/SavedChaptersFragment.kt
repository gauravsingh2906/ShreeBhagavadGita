package com.android.example.shreebhagwatgita.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.example.shreebhagwatgita.Adapters.AdapterChapters
import com.android.example.shreebhagwatgita.Models.ChaptersItem
import com.android.example.shreebhagwatgita.R
import com.android.example.shreebhagwatgita.databinding.FragmentSavedChaptersBinding
import com.android.example.shreebhagwatgita.viewModel.MainViewModel


class SavedChaptersFragment : Fragment() {
    private lateinit var binding: FragmentSavedChaptersBinding
    private lateinit var adapterChapters: AdapterChapters
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedChaptersBinding.inflate(layoutInflater)


        getSavedChapters()


        return binding.root
    }

    private fun getSavedChapters() {
        viewModel.getSavedChapters().observe(viewLifecycleOwner) {
            val chaptersList = arrayListOf<ChaptersItem>()
            for (i in it) {
                val chaptersItem = ChaptersItem(
                    i.chapter_number,
                    i.chapter_summary,
                    i.chapter_summary_hindi,
                    i.id,
                    i.name,
                    i.name_meaning,
                    i.name_translated,
                    i.name_transliterated,
                    i.slug,
                    i.verses_count
                )
                chaptersList.add(chaptersItem)
            }

            if(chaptersList.isEmpty()) {
                binding.shimmer.visibility=View.GONE
                binding.rvChapters.visibility=View.GONE
                binding.tvShowingMessage.visibility=View.VISIBLE
            }

            adapterChapters= AdapterChapters(
                ::onChapterIvClicked,
                ::onFavouriteClicked,
                false,
                ::onFavouriteFilled,
                viewModel
            )
            binding.rvChapters.adapter=adapterChapters
            adapterChapters.differ.submitList(chaptersList)

            binding.shimmer.visibility=View.GONE
            binding.rvChapters.visibility=View.VISIBLE

        }
    }

   fun onChapterIvClicked(chaptersItem: ChaptersItem) {
       val bundle=Bundle()
       //here we are fetching data from room
       bundle.putInt("chapterNumber",chaptersItem.chapter_number)
       bundle.putBoolean("showRoomData",true)

       findNavController().navigate(R.id.action_savedChaptersFragment_to_versesFragment,bundle)
   }

    fun onFavouriteClicked(chaptersItem: ChaptersItem) {

    }

    fun onFavouriteFilled(chaptersItem: ChaptersItem) {

    }
}