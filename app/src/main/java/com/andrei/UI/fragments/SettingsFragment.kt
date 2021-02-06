package com.andrei.UI.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.andrei.carrental.R
import com.andrei.carrental.viewmodels.ViewModelAuth
import com.andrei.engine.helpers.UserManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {


    private val viewModelAuth:ViewModelAuth by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val logoutPreference: Preference? = findPreference("sign_out")
         logoutPreference?.setOnPreferenceClickListener {
             viewModelAuth.signOut()
             true
       }
    }
}