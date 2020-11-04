package com.example.comat.ui.new_conference

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.example.comat.R
import com.example.comat.databinding.FragmentNewConferenceBinding
import kotlinx.android.synthetic.*
import java.util.*

class NewConferenceFragment : Fragment() {

    private lateinit var newConferenceViewModel: NewConferenceViewModel
    private var SELECT_PHOTO: Int = 1
    private lateinit var binding: FragmentNewConferenceBinding
    private lateinit var imageURI: Uri
    private var dateString: String? = null
    private var isLogoPicked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newConferenceViewModel =
            ViewModelProvider(this).get(NewConferenceViewModel::class.java)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_conference, container, false)
//        picks an image from user
        pickLogo()

        handlerDatePicker()
        val validator = handleDataValidation()
        createNewConference(validator)

        newConferenceViewModel.navigate.observe(viewLifecycleOwner,{
            if (it){
                findNavController().navigate(R.id.action_nav_new_conference_to_nav_conferences)
            }
        })
        return binding.root
    }

//    picks logo
    private fun pickLogo() {
        binding.pickImage.setOnClickListener {
            val intent: Intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, SELECT_PHOTO)
        }
    }

    //    creates new conference
    private fun createNewConference(validator: AwesomeValidation) {
        binding.createConf.setOnClickListener {
            Log.d("reached", "button clicked")
            if (validator.validate()) {
                if (!isLogoPicked) {
                    makeText(context, "Please pick a logo", LENGTH_LONG)
                } else if (dateString == null) {
                    makeText(context, "Please pick a date", LENGTH_LONG)
                } else {
                    activity?.applicationContext?.let {
                        newConferenceViewModel.createNewConference(
                            it,
                            imageURI,
                            binding.conferenceName.text.toString(),
                            binding.conferenceDescription.text.toString(),
                            dateString!!,
                            binding.confSchedule.text.toString(),
                            binding.confSpeakers.text.toString(),
                            binding.confVenue.text.toString(),
                        )
                    }
                }
            }
        }
    }

    //    handles data validation
    private fun handleDataValidation(): AwesomeValidation {
        //        validation
        val validator = AwesomeValidation(ValidationStyle.BASIC)
        validator.addValidation(
            activity,
            R.id.conference_name,
            ".{4,}",
            R.string.invalid_conference_name
        )
        validator.addValidation(
            activity,
            R.id.conference_description,
            ".{10,}",
            R.string.invalid_conference_description
        )
        validator.addValidation(
            activity,
            R.id.conf_schedule,
            RegexTemplate.NOT_EMPTY,
            R.string.invalid_conference_schedule
        )
        validator.addValidation(
            activity,
            R.id.conf_speakers,
            RegexTemplate.NOT_EMPTY,
            R.string.invalid_conference_speakers
        )
        validator.addValidation(
            activity,
            R.id.conf_venue,
            RegexTemplate.NOT_EMPTY,
            R.string.invalid_conference_venue
        )
        return validator
    }

    //    handles data picker
    private fun handlerDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        binding.pickDateBtn.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                        dateString = "$mDay/$mMonth/$mYear"
                        binding.showDate.text = "Date Picked: $dateString"
                    },
                    year,
                    month,
                    day
                )
            }?.show()
        }
    }


    //    handles image picker result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PHOTO) {
            // handle chosen image
            if (data != null) {
                imageURI = data.data!!
                binding.confLogo.setImageURI(imageURI)
                isLogoPicked = true
            }
        }
    }
}