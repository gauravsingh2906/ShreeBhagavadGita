package com.android.example.shreebhagwatgita.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.example.shreebhagwatgita.R
import com.android.example.shreebhagwatgita.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private lateinit var binding:FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSettingsBinding.inflate(layoutInflater)


        binding.llSavedChapters.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_savedChaptersFragment)
        }

        binding.llsavedVerses.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_savedVersesFragment)
        }

        return binding.root
    }


}