package com.andrei.UI.fragments

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.andrei.carrental.R
import com.andrei.carrental.viewmodels.ViewModelLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {


    private val viewModelLogin:ViewModelLogin by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val logoutPreference: Preference? = findPreference("sign_out")
         logoutPreference?.setOnPreferenceClickListener {
             viewModelLogin.signOut()
             true
       }
    }
}