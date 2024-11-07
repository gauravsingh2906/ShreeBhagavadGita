package com.android.example.shreebhagwatgita.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.example.shreebhagwatgita.Adapters.AdapterSavedVerses
import com.android.example.shreebhagwatgita.Adapters.AdapterVerses
import com.android.example.shreebhagwatgita.R
import com.android.example.shreebhagwatgita.dataSource.room.SavedVerse
import com.android.example.shreebhagwatgita.databinding.FragmentSavedVersesBinding
import com.android.example.shreebhagwatgita.viewModel.MainViewModel


class SavedVersesFragment : Fragment() {
    private lateinit var binding: FragmentSavedVersesBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapterVerses: AdapterSavedVerses

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedVersesBinding.inflate(layoutInflater)


        getVerseFromRoom()

        return binding.root
    }

    private fun getVerseFromRoom() {

        viewModel.getAllEnglishVerse().observe(viewLifecycleOwner) {

            adapterVerses = AdapterSavedVerses(::onVerseItemClicked)
            binding.rvVerses.adapter = adapterVerses

            adapterVerses.differ.submitList(it)
            binding.shimmer.visibility = View.GONE



        }
    }

    fun onVerseItemClicked(verse:SavedVerse) {
       val bundle=Bundle()
        bundle.putBoolean("showRoomData",true)
        bundle.putInt("chapterNum", verse.chapter_number)
        bundle.putInt("verseNum", verse.verse_number)
        findNavController().navigate(R.id.action_savedVersesFragment_to_versesDetailFragment,bundle)
    }


}