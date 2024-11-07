package com.android.example.shreebhagwatgita

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import android.widget.Toast
import com.google.android.play.integrity.internal.s
import java.util.HashSet
import java.util.Locale

class TextToSpeechManager(private val context:Context) {

    private var textToSpeech:TextToSpeech?=null
    private var isInitialized=false

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale.ENGLISH)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(context, result.toString(), Toast.LENGTH_SHORT).show()
                } else {



                    val voice = Voice("yue-hk-x-yud-network",Locale("hi_IN"),400,200,false,HashSet(
                        listOf("female")
                    ))

                    for(voice in textToSpeech?.voices!!){
                        Log.d("voices",voice.name)
                    }
//

                    textToSpeech?.voice  = voice
                    isInitialized=true



                }

            } else{
                Toast.makeText(context, "Initialization Error!", Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun speak(text:String){
        if (isInitialized) {
            textToSpeech?.apply {
                setSpeechRate(1.0f)
                setPitch(0.8f)
                speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
            }
        } else{
            Toast.makeText(context,"Initialization Error!", Toast.LENGTH_SHORT).show()
        }
    }

    fun stop(shutdown:Boolean){
        if(isInitialized) {
            textToSpeech?.stop()
            if (shutdown) textToSpeech?.shutdown()
        }else{
            Toast.makeText(context,"Initialization Error!", Toast.LENGTH_SHORT).show()
        }
    }

    fun setUtteranceProgressListener(listener:UtteranceProgressListener) {
        textToSpeech?.setOnUtteranceProgressListener(listener)
    }



}