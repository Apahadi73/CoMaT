package com.example.comat.ui.new_conference

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.comat.models.Schedule
import kotlinx.android.synthetic.main.dialogue_add_schedule.*
import java.util.*
import kotlin.collections.ArrayList

class NewConferenceFragment : Fragment() {

    private lateinit var newConferenceViewModel: NewConferenceViewModel
    private var SELECT_PHOTO: Int = 1
    private lateinit var binding: FragmentNewConferenceBinding
    private lateinit var imageURI: Uri
    private var dateString: String? = null
    private var isLogoPicked: Boolean = false
    private var schedules = ArrayList<Schedule>()

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
        binding.addScheduleBtn.setOnClickListener {
            openDialog(context)
        }

        newConferenceViewModel.navigate.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(R.id.action_nav_new_conference_to_nav_conferences)
            }
        })
        return binding.root
    }

    //    opens dialog for user to add schedule
    private fun openDialog(context: Context?): Unit {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialogue_add_schedule, null)
        val dialogBuilder =
            AlertDialog.Builder(context).setView(dialogView).setTitle("ADD SCHEDULE")
        val alertDialog = dialogBuilder.show()
        var startTime = ""
        var endTime = ""
        alertDialog.pick_start_time.setOnClickListener {
           startTime =  pickTime(context)
            alertDialog.start_time.text = startTime
        }
        alertDialog.pick_end_time.setOnClickListener {
           endTime =  pickTime(context)
            alertDialog.start_time.text = endTime
        }
        alertDialog.add_new_schedule.setOnClickListener {
            val scheduleName = alertDialog.program_name.text.toString()
            val speakers = alertDialog.speakers.text.toString()
            schedules.add(Schedule(startTime,endTime,scheduleName,speakers))
            Log.d("schedules", schedules.toString())
            alertDialog.dismiss()
        }
        alertDialog.schedule_time.text = startTime
    }


//    todo: work here
    private fun pickTime(context: Context?): String {
        var time =""
        val calendar = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, mHour, mMinute ->
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                calendar.set(0, 0, 0, hour, minute)
                time =
                    DateFormat.format("hh:mm aa", mHour,) as String
                Log.d("time",time)
            },
            12,
            0,
            true
        )
        timePickerDialog.show()
        return time
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
//                            todo: modify here
                            ArrayList<Schedule>(),
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
                    DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
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