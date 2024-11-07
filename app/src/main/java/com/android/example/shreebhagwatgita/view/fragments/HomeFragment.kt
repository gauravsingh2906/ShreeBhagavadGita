package com.android.example.shreebhagwatgita.view.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.example.shreebhagwatgita.Adapters.AdapterChapters
import com.android.example.shreebhagwatgita.Models.ChaptersItem
import com.android.example.shreebhagwatgita.NetworkManager
import com.android.example.shreebhagwatgita.R
import com.android.example.shreebhagwatgita.dataSource.room.SavedChapters
import com.android.example.shreebhagwatgita.databinding.FragmentHomeBinding
import com.android.example.shreebhagwatgita.viewModel.MainViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    private val viewModel:MainViewModel by viewModels()
    private lateinit var adapter:AdapterChapters

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding= FragmentHomeBinding.inflate(layoutInflater)


        checkInternet()
        setStatusBarColor()



        binding.ivSettings.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }




        return binding.root
    }

    private fun showVerserseOfTheDay() {
        val chapterNumber = (1..18).random()
        val verseNumber = (1..20).random()


        lifecycleScope.launch {
            viewModel.getAParticularVerse(chapterNumber,verseNumber).collect{

                for(i in it.translations){
                    if(i.language=="english" ){
                        binding.tvVerseOfTheDay.text=i.description
                        break
                    }
                }


            }
        }

    }

    private fun checkInternet() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner){
            if(it==true) {
                binding.shimmer.visibility=View.VISIBLE
                binding.rvChapters.visibility=View.VISIBLE
                binding.tvShowingMessage.visibility=View.GONE

                getAllChapter()
                showVerserseOfTheDay()
            } else{


                binding.shimmer.visibility=View.GONE
                binding.rvChapters.visibility=View.GONE
                binding.tvShowingMessage.visibility=View.VISIBLE
            }
        }
    }

    private fun getAllChapter() { // in flow we use collect
        lifecycleScope.launch {
            viewModel.getAllChapter().collect{
               adapter=AdapterChapters(::onChapterIVClicked, ::onFavouriteClicked, true,::onFavouriteFilled,viewModel)
                binding.rvChapters.adapter=adapter
                adapter.differ.submitList(it)
                binding.shimmer.visibility=View.GONE
            }
        }
    }

    fun onFavouriteClicked(chaptersItem: ChaptersItem) {

            lifecycleScope.launch {
                viewModel.putSavedChapterSP(chaptersItem.chapter_number.toString(),chaptersItem.id)
                viewModel.getVerses(chaptersItem.chapter_number).collect{

                    val verseList = arrayListOf<String>()

                    for(currentVerse in it) {

                        for(verses in currentVerse.translations){
                            if(verses.language=="english") {
                                verseList.add(verses.description)
                                break
                            }
                        }
                    }
                val savedChapters = SavedChapters(
                    chapter_number = chaptersItem.chapter_number,
                    chapter_summary = chaptersItem.chapter_summary,
                    chapter_summary_hindi = chaptersItem.chapter_summary_hindi,
                    id=chaptersItem.id,
                    name = chaptersItem.name,
                    name_meaning = chaptersItem.name_meaning,
                    name_translated = chaptersItem.name_translated,
                    name_transliterated = chaptersItem.name_transliterated,
                    slug = chaptersItem.slug,
                    verses_count = chaptersItem.verses_count,
                    verses = verseList)
                    viewModel.insertChapters(savedChapters)
                }

            }

    }

   private fun onFavouriteFilled(chaptersItem: ChaptersItem) {
       lifecycleScope.launch {
           viewModel.deleteSavedChapterFromSP(chaptersItem.chapter_number.toString())
           viewModel.deleteChapter(chaptersItem.id)
       }

   }

    private fun onChapterIVClicked(chapter:ChaptersItem) {

        val bundle=Bundle()
        bundle.putInt("chapterNumber",chapter.chapter_number)
        bundle.putString("chapterTitle",chapter.name_translated)
        bundle.putString("chapterDesc",chapter.chapter_summary)
        bundle.putInt("verseCount",chapter.verses_count)
        findNavController().navigate(R.id.action_homeFragment_to_versesFragment,bundle)
        }







private fun setStatusBarColor() {
    activity?.window?.apply {
        val statusBarColors = ContextCompat.getColor(requireContext(), R.color.white)
        statusBarColor = statusBarColors
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

    }
}
}