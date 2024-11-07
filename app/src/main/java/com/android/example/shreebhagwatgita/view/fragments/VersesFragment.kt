package com.android.example.shreebhagwatgita.view.fragments

import android.os.Build
import android.os.Bundle
import android.speech.tts.UtteranceProgressListener
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.example.shreebhagwatgita.Adapters.AdapterVerses
import com.android.example.shreebhagwatgita.NetworkManager
import com.android.example.shreebhagwatgita.R
import com.android.example.shreebhagwatgita.TextToSpeechManager
import com.android.example.shreebhagwatgita.databinding.FragmentVersesBinding
import com.android.example.shreebhagwatgita.viewModel.MainViewModel
import kotlinx.coroutines.launch


class VersesFragment : Fragment() {
    private lateinit var binding: FragmentVersesBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var textToSpeechManager: TextToSpeechManager
    private lateinit var adapterVerses: AdapterVerses
    private var chapterNumber = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVersesBinding.inflate(layoutInflater)

        setStatusBarColor()
        textToSpeechManager=TextToSpeechManager(requireActivity())
       onPlayClick()
        onPauseClicked()

        onReadMoreClicked()
        setUtteranceProgressListener()

        getRoomData()


        getAndSetChapterDetail()
        fabSetUp()

        return binding.root
    }

    private fun setUtteranceProgressListener() {
        textToSpeechManager.setUtteranceProgressListener(object:UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                activity?.runOnUiThread {
                    binding.Progress.visibility=View.GONE
                    binding.ivPause.visibility=View.VISIBLE
                }

            }

            override fun onDone(utteranceId: String?) {
                activity?.runOnUiThread {
                    binding.ivPause.visibility=View.GONE
                    binding.ivPlay.visibility=View.VISIBLE
                    Toast.makeText(context, "Completed", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onError(utteranceId: String?) {

            }

        })
    }

    private fun onPauseClicked() {
        binding.ivPause.setOnClickListener {
            binding.ivPause.visibility=View.GONE
            binding.ivPlay.visibility=View.VISIBLE
            textToSpeechManager.stop(false)
    }
    }

    private fun onPlayClick() {
        binding.ivPlay.setOnClickListener {
            binding.Progress.visibility=View.VISIBLE
            binding.ivPlay.visibility=View.GONE
            textToSpeechManager.speak(binding.tvChapterDescription.text.toString())
        }
    }

    private fun getRoomData() {
        val bundle = arguments
        val showDataFromRoom = bundle?.getBoolean("showRoomData", false)

        if (showDataFromRoom == true) {
            getDataFromRoom()
        } else {
            checkInternet()
        }
    }

    private fun checkInternet() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.tvShowingMessage.visibility = View.GONE
                binding.shimmer.visibility = View.VISIBLE
                binding.rvVerses.visibility = View.VISIBLE
                getAllVerses()
            } else {
                binding.tvShowingMessage.visibility = View.VISIBLE
                binding.shimmer.visibility = View.GONE
                binding.rvVerses.visibility = View.GONE
                binding.backgroundImage.visibility = View.GONE
                binding.tvTitle.visibility = View.GONE
                binding.tvChapterNumber.visibility = View.GONE
                binding.tvChapterDescription.visibility = View.GONE
                binding.tvSeeMore.visibility = View.GONE
                binding.tvNumberOfVerses.visibility = View.GONE
                binding.tvVerses.visibility = View.GONE
            }
        }
    }

    private fun getDataFromRoom() {
        val bundle=arguments
        if (bundle != null) {
            chapterNumber=bundle.getInt("chapterNumber")
        }
        viewModel.getAParticularChapter(chapterNumber).observe(viewLifecycleOwner) {

            binding.tvChapterNumber.text = "Chapter ${it.chapter_number}"
            binding.tvTitle.text = it.name_translated
            binding.tvChapterDescription.text = it.chapter_summary
            binding.tvNumberOfVerses.text = it.verses_count.toString()
            showListInAdapter(it.verses!!,false)
        }
    }

    private fun fabSetUp() {
        binding.rvVerses.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()


                if (dy > 20 && firstVisibleItemPosition > 0) {
                    binding.fab.visibility = View.VISIBLE
                } else if (dy < -20 && firstVisibleItemPosition == 0) {
                    binding.fab.visibility = View.GONE
                }
            }
        })
        binding.fab.setOnClickListener {
            binding.rvVerses.smoothScrollToPosition(0)
        }
    }

    private fun onReadMoreClicked() {
        var isExpanded = false
        binding.tvSeeMore.setOnClickListener {
            if (!isExpanded) {
                binding.tvChapterDescription.maxLines = 50
                isExpanded = true
                binding.tvSeeMore.text = "Read Less"
            } else {
                binding.tvChapterDescription.maxLines = 4
                isExpanded = false
                binding.tvSeeMore.text = "Read More..."
            }
        }
    }

    fun getAndSetChapterDetail() {
        val bundle = arguments
        chapterNumber = bundle?.getInt("chapterNumber")!!
        binding.tvChapterNumber.text = "Chapter ${chapterNumber}"
        binding.tvTitle.text = bundle.getString("chapterTitle")
        binding.tvChapterDescription.text = bundle.getString("chapterDesc")
        binding.tvNumberOfVerses.text = bundle.getInt("verseCount").toString()

    }

    private fun getAllVerses() {
        lifecycleScope.launch {
            val bundle = arguments
            chapterNumber = bundle?.getInt("chapterNumber")!!
            viewModel.getVerses(chapterNumber).collect {

                val verseList = arrayListOf<String>()

                for (currentVerse in it) {

                    for (verses in currentVerse.translations) {
                        if (verses.language == "english") {
                            verseList.add(verses.description)
                            break
                        }
                    }
                }
                showListInAdapter(verseList,true)
            }
        }
    }

    private fun showListInAdapter(verseList: List<String>,onClick:Boolean) {

        adapterVerses = AdapterVerses(::onVerseItemClicked,onClick)
        binding.rvVerses.adapter = adapterVerses

        adapterVerses.differ.submitList(verseList)
        binding.shimmer.visibility = View.GONE
    }

    private fun onVerseItemClicked(verse: String, verseNumber: Int) {
        val bundle = Bundle()
        bundle.putInt("chapterNum", chapterNumber)
        bundle.putInt("verseNum", verseNumber)
        findNavController().navigate(R.id.action_versesFragment_to_versesDetailFragment, bundle)
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

    override fun onDestroy() {
        super.onDestroy()
        textToSpeechManager.stop(true)
    }


}