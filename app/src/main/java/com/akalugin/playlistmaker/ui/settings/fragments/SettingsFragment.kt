package com.akalugin.playlistmaker.ui.settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akalugin.playlistmaker.databinding.FragmentSettingsBinding
import com.akalugin.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    private val viewModel: SettingsViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            with(viewModel) {
                shareTextView.setOnClickListener {
                    shareApp()
                }

                supportTextView.setOnClickListener {
                    openSupport()
                }

                userAgreementTextView.setOnClickListener {
                    openTerms()
                }

                nightThemeActiveLiveData.observe(this@SettingsFragment.viewLifecycleOwner) { isChecked ->
                    nightThemeSwitch.isChecked = isChecked
                }

                nightThemeSwitch.setOnClickListener {
                    switchTheme()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}