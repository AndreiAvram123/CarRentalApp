package com.andrei.UI.fragments.registration

import android.content.Intent
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.fragments.BaseFragment
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentChooseProfilePictureBinding
import com.andrei.carrental.viewmodels.ViewModelSignUp
import dagger.hilt.android.AndroidEntryPoint
import pl.aprilapps.easyphotopicker.*
import javax.inject.Inject

@AndroidEntryPoint
class ChooseProfilePictureFragment : BaseRegistrationFragment(R.layout.fragment_choose_profile_picture){

    private val binding:FragmentChooseProfilePictureBinding by viewBinding()

    private val easyImage:EasyImage by lazy {
        EasyImage.Builder(requireContext()).allowMultiple(false)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY).build()
    }


    override fun initializeUI() {
        binding.ivProfilePicture.setOnClickListener {
            easyImage.openChooser(this)
        }
        binding.btNext.setOnClickListener {
            val profilePicture = binding.ivProfilePicture.drawable
            viewModelSignUp.setProfilePicture(profilePicture)
            navigateForward()
        }
        binding.btBack.setOnClickListener {
            navigateBack()
        }
    }

    override fun showError(error: String) {

    }

    override fun hideError() {

    }

    override fun disableNextButton() {

    }


    override fun enableNextButton(){
        binding.btNext.isEnabled = true
    }

    private fun setProfilePicture(mediaFile: MediaFile){
        binding.ivProfilePicture.setImageURI(mediaFile.file.toUri())
    }
    override fun navigateForward(){
       val action = ChooseProfilePictureFragmentDirections.actionChooseProfilePictureFragmentToRegistrationCompleteFragment()
        findNavController().navigate(action)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        easyImage.handleActivityResult(requestCode,resultCode,data,requireActivity(), object : DefaultCallback() {
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                if(imageFiles.isNotEmpty()){
                    setProfilePicture(imageFiles.first())
                    enableNextButton()
                }
            }
    })
}
}