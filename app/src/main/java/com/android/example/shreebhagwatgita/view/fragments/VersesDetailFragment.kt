package com.android.example.shreebhagwatgita.view.fragments

import android.os.Bundle
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.android.example.shreebhagwatgita.Adapters.AdapterVerses
import com.android.example.shreebhagwatgita.Commentary
import com.android.example.shreebhagwatgita.NetworkManager
import com.android.example.shreebhagwatgita.R
import com.android.example.shreebhagwatgita.TextToSpeechManager
import com.android.example.shreebhagwatgita.Translation
import com.android.example.shreebhagwatgita.dataSource.room.SavedVerse
import com.android.example.shreebhagwatgita.databinding.FragmentVersesDetailBinding
import com.android.example.shreebhagwatgita.versesItem
import com.android.example.shreebhagwatgita.viewModel.MainViewModel
import kotlinx.coroutines.launch


class VersesDetailFragment : Fragment() {

    private lateinit var binding: FragmentVersesDetailBinding
    private val viewModel: MainViewModel by viewModels()
    private var verseDetail= MutableLiveData<versesItem>()
    private lateinit var textToSpeechManage: TextToSpeechManager
    private lateinit var textToSpeechManager: TextToSpeechManager
    private var chapterNum = 0
    private var verseNum = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVersesDetailBinding.inflate(layoutInflater)

        getAndSetChapterVerseNum()
        onSaveVerse()

        getData()
        textToSpeechManage=TextToSpeechManager(requireActivity())
        textToSpeechManager=TextToSpeechManager(requireActivity())
        onPlayClick()
        onPlayClick1()
        onPauseClicked()
        onPauseClicked1()
        setUtteranceProgressListener()
        setUtteranceProgressListener1()

        return binding.root
    }

    private fun onPauseClicked1() {
        binding.ivPause1.setOnClickListener {
            binding.ivPause1.visibility=View.GONE
            binding.ivPlay1.visibility=View.VISIBLE
            textToSpeechManager.stop(false)
        }
    }

    private fun onPlayClick1() {
        binding.ivPlay1.setOnClickListener {
            binding.Progress1.visibility=View.VISIBLE
            binding.ivPlay1.visibility=View.GONE
            textToSpeechManager.speak(binding.tvText.text.toString())
        }
    }

    private fun onPauseClicked() {
        binding.ivPause.setOnClickListener {
            binding.ivPause.visibility=View.GONE
            binding.ivPlay.visibility=View.VISIBLE
            textToSpeechManage.stop(false)
        }
    }

    private fun onPlayClick() {
        binding.ivPlay.setOnClickListener {
            binding.Progress.visibility=View.VISIBLE
            binding.ivPlay.visibility=View.GONE
            textToSpeechManage.speak(binding.tvtext1.text.toString())
        }
    }

    private fun setUtteranceProgressListener1() {
        textToSpeechManager.setUtteranceProgressListener(object: UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                activity?.runOnUiThread {
                    binding.Progress1.visibility=View.GONE
                    binding.ivPause1.visibility=View.VISIBLE
                }

            }

            override fun onDone(utteranceId: String?) {
                activity?.runOnUiThread {
                    binding.ivPause1.visibility=View.GONE
                    binding.ivPlay1.visibility=View.VISIBLE

                }

            }

            override fun onError(utteranceId: String?) {

            }

        })
    }

    private fun setUtteranceProgressListener() {
        textToSpeechManage.setUtteranceProgressListener(object: UtteranceProgressListener() {
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
                  //  Toast.makeText(context, "Completed", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onError(utteranceId: String?) {

            }

        })
    }

    private fun getData() {
        val bundle=arguments
        val showRoomData=bundle?.getBoolean("showRoomData",false)

        if(showRoomData==true) {
            binding.ivFavouriteVerse.visibility=View.GONE
            binding.ivFavouriteVerseFilled.visibility=View.GONE

            viewModel.getParticularVerse(chapterNum,verseNum).observe(viewLifecycleOwner) {

                binding.tvVerseText.text = it.text
                binding.tvTransliterationIfEnglish.text = it.transliteration
                binding.tvWordIfEnglish.text = it.word_meanings

                val englishTranslationList = arrayListOf<Translation>()
                for (i in it.translations) {
                    if (i.language == "english") {
                        englishTranslationList.add(i)
                    }
                }

                val englishTranslationListSize = englishTranslationList.size
                if (englishTranslationList.isNotEmpty()) {
                    binding.tvAuthorName.text = englishTranslationList[0].author_name
                    binding.tvText.text = englishTranslationList[0].description
                    if (englishTranslationListSize == 1) {
                        binding.fabTranslationRight.visibility = View.GONE
                    }
                    var i = 0;
                    binding.fabTranslationRight.setOnClickListener {
                        if (i < englishTranslationListSize - 1) {
                            i++
                            binding.tvAuthorName.text = englishTranslationList[i].author_name
                            binding.tvText.text = englishTranslationList[i].description
                            binding.fabTranslationLeft.visibility = View.VISIBLE

                            if (i == englishTranslationListSize - 1) {
                                binding.fabTranslationRight.visibility = View.GONE
                            }

                        }
                    }

                    binding.fabTranslationLeft.setOnClickListener {
                        if (i > 0) {
                            i--
                            binding.tvAuthorName.text = englishTranslationList[i].author_name
                            binding.tvText.text = englishTranslationList[i].description
                            binding.fabTranslationRight.visibility = View.VISIBLE

                            if (i == 0) {
                                binding.fabTranslationLeft.visibility = View.GONE
                            }

                        }
                    }
                }


                //CommentaryList

                val englishCommentaryList = arrayListOf<Commentary>()
                for (j in it.commentaries) {
                    if (j.language == "sanskrit") {
                        englishCommentaryList.add(j)
                    }
                }

                val englishCommentaryListSize = englishCommentaryList.size
                if (englishCommentaryList.isNotEmpty()) {
                    binding.tvAuthorName1.text = englishCommentaryList[0].author_name
                    binding.tvtext1.text = englishCommentaryList[0].description
                    if (englishCommentaryListSize == 1) {
                        binding.fabTranslationLeft1.visibility = View.GONE
                    }

                    var j = 0
                    binding.fabTranslationRight1.setOnClickListener {
                        if (j < englishCommentaryListSize - 1) {
                            j++
                            binding.tvAuthorName1.text = englishCommentaryList[j].author_name
                            binding.tvtext1.text = englishCommentaryList[j].description
                            binding.fabTranslationLeft1.visibility = View.VISIBLE
                            if (j == englishCommentaryListSize - 1) {
                                binding.fabTranslationRight1.visibility = View.GONE
                            }
                        }
                    }

                    binding.fabTranslationLeft1.setOnClickListener {
                        if (j > 0) {
                            j--
                            binding.tvAuthorName1.text = englishCommentaryList[j].author_name
                            binding.tvtext1.text = englishCommentaryList[j].description
                            binding.fabTranslationRight1.visibility = View.VISIBLE
                            if (j == 0) {
                                binding.fabTranslationLeft1.visibility = View.GONE
                            }
                        }


                    }

                }

                binding.progressBar.visibility=View.GONE
                binding.tvVerseNumber.visibility=View.VISIBLE
                binding.tvVerseText.visibility=View.VISIBLE
                binding.tvTransliterationIfEnglish.visibility = View.VISIBLE
                binding.tvWordIfEnglish.visibility = View.VISIBLE
                binding.view.visibility = View.VISIBLE
                binding.llCommentatory.visibility=View.VISIBLE
                binding.tvTranslation.visibility = View.VISIBLE
                binding.clTranslation.visibility = View.VISIBLE
                binding.tvCommentatory.visibility = View.VISIBLE
                binding.clCommentatories.visibility = View.VISIBLE
                binding.backgroundImage.visibility = View.VISIBLE
                binding.ivFavouriteVerse.visibility = View.GONE
                binding.ivFavouriteVerseFilled.visibility=View.GONE
                binding.progressBar.visibility=View.GONE
                binding.tvVerseNumber.visibility=View.VISIBLE
                binding.tvVerseText.visibility=View.VISIBLE
                binding.tvTransliterationIfEnglish.visibility = View.VISIBLE
                binding.tvWordIfEnglish.visibility = View.VISIBLE
                binding.view.visibility = View.VISIBLE
                binding.tvTranslation.visibility = View.VISIBLE
                binding.clTranslation.visibility = View.VISIBLE
                binding.tvCommentatory.visibility = View.VISIBLE
                binding.clCommentatories.visibility = View.VISIBLE
                binding.backgroundImage.visibility = View.VISIBLE
                binding.linearLayout.visibility=View.VISIBLE
                binding.tvAuthor.visibility=View.VISIBLE
                binding.tvAuthorName.visibility=View.VISIBLE
                binding.tvArrow.visibility=View.VISIBLE
                binding.tvText.visibility=View.VISIBLE
                binding.fabTranslationRight.visibility=View.VISIBLE
                binding.llCommentatory.visibility=View.VISIBLE
                binding.tvAuthor1.visibility=View.VISIBLE
                binding.tvAuthorName1.visibility=View.VISIBLE
                binding.tvArrow1.visibility=View.VISIBLE
                binding.tvtext1.visibility=View.VISIBLE
                binding.fabTranslationRight1.visibility=View.VISIBLE

            }

        }else{
            checkInternet()
        }

    }

    private fun onSaveVerse() {
        binding.ivFavouriteVerse.setOnClickListener {
//            binding.ivFavouriteVerse.visibility = View.GONE
//            binding.ivFavouriteVerseFilled.visibility = View.VISIBLE
            val key = viewModel.getAllSavedVerses()
           Log.d("Gaurav",key.toString())
            if(key.contains("||$chapterNum.$verseNum||")) {
             //   Toast.makeText(requireContext(), "i am inside the function", Toast.LENGTH_SHORT).show()
                binding.ivFavouriteVerse.visibility = View.GONE
                binding.ivFavouriteVerseFilled.visibility = View.VISIBLE
            } else{
                binding.ivFavouriteVerse.visibility = View.VISIBLE
                binding.ivFavouriteVerseFilled.visibility = View.GONE
            }
          //  Toast.makeText(requireContext(), "We are outside the function", Toast.LENGTH_SHORT).show()
            savingVerse()
        }
        binding.ivFavouriteVerseFilled.setOnClickListener {
            binding.ivFavouriteVerse.visibility = View.VISIBLE
            binding.ivFavouriteVerseFilled.visibility = View.GONE
            deleteVerse()
        }
    }

    private fun deleteVerse() {
        lifecycleScope.launch {
            viewModel.deleteAParticularVerse(chapterNum,verseNum)
        }
    }

    private fun savingVerse() {
        verseDetail.observe(viewLifecycleOwner) {
            val englishTranslationList = arrayListOf<Translation>()
            for (i in it.translations) {
                if (i.language == "english") {
                    englishTranslationList.add(i)
                }
            }

            val englishCommentaryList = arrayListOf<Commentary>()
            for (j in it.commentaries) {
                if (j.language == "sanskrit") {
                    englishCommentaryList.add(j)
                }
            }


            val savedVerses = SavedVerse(
                it.chapter_number,
                englishCommentaryList,
                it.id,
                it.slug,
                it.text,
                englishTranslationList,
                it.transliteration,
                it.verse_number,
                it.word_meanings
            )
            lifecycleScope.launch {
                viewModel.insertEnglishChapters(savedVerses)
            }

        }
    }

    private fun checkInternet() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.tvShowingMessage.visibility = View.GONE
                binding.iv.visibility=View.GONE
                binding.progressBar.visibility = View.VISIBLE
                binding.ivFavouriteVerseFilled.visibility=View.GONE

                getVerseDetail()
            } else {
                binding.tvShowingMessage.visibility = View.VISIBLE
                binding.iv.visibility=View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.tvVerseNumber.visibility = View.GONE
                binding.tvVerseText.visibility = View.GONE
                binding.tvTransliterationIfEnglish.visibility = View.GONE
                binding.tvWordIfEnglish.visibility = View.GONE
                binding.view.visibility = View.GONE
                binding.tvTranslation.visibility = View.GONE
                binding.clTranslation.visibility = View.GONE
                binding.tvCommentatory.visibility = View.GONE
                binding.clCommentatories.visibility = View.GONE
                binding.backgroundImage.visibility = View.GONE
                binding.ivFavouriteVerse.visibility = View.GONE
            }
        }
    }

    private fun getVerseDetail() {
        lifecycleScope.launch {
            viewModel.getAParticularVerse(chapterNum, verseNum).collect {
                verseDetail.postValue(it)
                binding.tvVerseText.text = it.text
                binding.tvTransliterationIfEnglish.text = it.transliteration
                binding.tvWordIfEnglish.text = it.word_meanings

                val englishTranslationList = arrayListOf<Translation>()
                for (i in it.translations) {
                    if (i.language == "english") {
                        englishTranslationList.add(i)
                    }
                }

                val englishTranslationListSize = englishTranslationList.size
                if (englishTranslationList.isNotEmpty()) {
                    binding.tvAuthorName.text = englishTranslationList[0].author_name
                    binding.tvText.text = englishTranslationList[0].description
                    if (englishTranslationListSize == 1) {
                        binding.fabTranslationRight.visibility = View.GONE
                    }
                    var i = 0;
                    binding.fabTranslationRight.setOnClickListener {
                        if (i < englishTranslationListSize - 1) {
                            i++
                            binding.tvAuthorName.text = englishTranslationList[i].author_name
                            binding.tvText.text = englishTranslationList[i].description
                            binding.fabTranslationLeft.visibility = View.VISIBLE

                            if (i == englishTranslationListSize - 1) {
                                binding.fabTranslationRight.visibility = View.GONE
                            }

                        }
                    }

                    binding.fabTranslationLeft.setOnClickListener {
                        if (i > 0) {
                            i--
                            binding.tvAuthorName.text = englishTranslationList[i].author_name
                            binding.tvText.text = englishTranslationList[i].description
                            binding.fabTranslationRight.visibility = View.VISIBLE

                            if (i == 0) {
                                binding.fabTranslationLeft.visibility = View.GONE
                            }

                        }
                    }
                }


                //CommentaryList

                val englishCommentaryList = arrayListOf<Commentary>()
                for (j in it.commentaries) {
                    if (j.language == "english"||j.language=="hindi") {
                        englishCommentaryList.add(j)
                    }
                }

                val englishCommentaryListSize = englishCommentaryList.size
                if (englishCommentaryList.isNotEmpty()) {
                    binding.tvAuthorName1.text = englishCommentaryList[0].author_name
                    binding.tvtext1.text = englishCommentaryList[0].description
                    if (englishCommentaryListSize == 1) {
                        binding.fabTranslationLeft1.visibility = View.GONE
                    }

                    var j = 0
                    binding.fabTranslationRight1.setOnClickListener {
                        if (j < englishCommentaryListSize - 1) {
                            j++
                            binding.tvAuthorName1.text = englishCommentaryList[j].author_name
                            binding.tvtext1.text = englishCommentaryList[j].description
                            binding.fabTranslationLeft1.visibility = View.VISIBLE
                            if (j == englishCommentaryListSize - 1) {
                                binding.fabTranslationRight1.visibility = View.GONE
                            }
                        }
                    }

                    binding.fabTranslationLeft1.setOnClickListener {
                        if (j > 0) {
                            j--
                            binding.tvAuthorName1.text = englishCommentaryList[j].author_name
                            binding.tvtext1.text = englishCommentaryList[j].description
                            binding.fabTranslationRight1.visibility = View.VISIBLE
                            if (j == 0) {
                                binding.fabTranslationLeft1.visibility = View.GONE
                            }
                        }


                    }

                }


            }
            binding.progressBar.visibility=View.GONE

            binding.tvVerseNumber.visibility=View.VISIBLE
            binding.tvVerseText.visibility=View.VISIBLE
            binding.tvTransliterationIfEnglish.visibility = View.VISIBLE
            binding.tvWordIfEnglish.visibility = View.VISIBLE
            binding.view.visibility = View.VISIBLE

            binding.tvTranslation.visibility = View.VISIBLE
            binding.clTranslation.visibility = View.VISIBLE
            binding.tvCommentatory.visibility = View.VISIBLE
            binding.clCommentatories.visibility = View.VISIBLE
            binding.backgroundImage.visibility = View.VISIBLE
            binding.ivFavouriteVerse.visibility = View.VISIBLE
            binding.linearLayout.visibility=View.VISIBLE
            binding.linearLayout.visibility=View.VISIBLE
            binding.tvAuthor.visibility=View.VISIBLE
            binding.tvAuthorName.visibility=View.VISIBLE
            binding.tvArrow.visibility=View.VISIBLE
            binding.tvText.visibility=View.VISIBLE
            binding.fabTranslationRight.visibility=View.VISIBLE
            binding.llCommentatory.visibility=View.VISIBLE
            binding.tvAuthor1.visibility=View.VISIBLE
            binding.tvAuthorName1.visibility=View.VISIBLE
            binding.tvArrow1.visibility=View.VISIBLE
            binding.tvtext1.visibility=View.VISIBLE
            binding.fabTranslationRight1.visibility=View.VISIBLE
            binding.ivPlay.visibility=View.VISIBLE
            binding.ivPlay1.visibility=View.VISIBLE

        }
    }

    private fun getAndSetChapterVerseNum() {
        val bundle = arguments
        chapterNum = bundle?.getInt("chapterNum")!!
        verseNum = bundle.getInt("verseNum")

        binding.tvVerseNumber.text = "||$chapterNum.$verseNum||"

    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeechManage.stop(true)
        textToSpeechManager.stop(false)
    }


}