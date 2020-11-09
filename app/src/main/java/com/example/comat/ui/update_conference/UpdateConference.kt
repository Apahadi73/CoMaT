package com.example.comat.ui.update_conference

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.example.comat.R
import com.example.comat.adapters.ScheduleAdapter
import com.example.comat.databinding.FragmentNewConferenceBinding
import com.example.comat.databinding.UpdateConferenceFragmentBinding
import com.example.comat.models.Schedule
import com.example.comat.ui.ui_shared.ConferencePageArgs
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialogue_add_schedule.*
import java.util.*
import kotlin.collections.ArrayList

class UpdateConference : Fragment() {

    private lateinit var updateConferenceViewModel: UpdateConferenceViewModel
    private var SELECT_PHOTO: Int = 1
    private lateinit var binding: UpdateConferenceFragmentBinding
    private lateinit var imageURI: Uri
    private var dateString: String? = null
    private var isLogoPicked: Boolean = false
    private var schedules = ArrayList<Schedule>()
    private var startTime = ""
    private var endTime = ""
    private lateinit var adapter: ScheduleAdapter
    private var confSpeakers = ""
    private var conferenceId = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        updateConferenceViewModel =
            ViewModelProvider(this).get(UpdateConferenceViewModel::class.java)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.update_conference_fragment, container, false)
        conferenceId = arguments?.let { ConferencePageArgs.fromBundle(it).conferenceId }.toString() ?: ""
        Log.d("id",conferenceId)
//        picks an image from user
        pickLogo()

        handlerDatePicker()
        val validator = handleDataValidation()
        updateConference(validator)
        binding.addScheduleBtn.setOnClickListener {
            openDialog(context)
        }

        updateConferenceViewModel.navigate.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(R.id.action_updateConference_to_my_conferences)
            }
        })

//        sets list view adapter for schedules
        adapter = ScheduleAdapter(requireContext())
        binding.scheduleListView.adapter = adapter
        return binding.root
    }

    //    opens dialog for user to add schedule
    private fun openDialog(context: Context?): Unit {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialogue_add_schedule, null)
        val dialogBuilder =
            AlertDialog.Builder(context).setView(dialogView).setTitle("ADD SCHEDULE")
        val alertDialog = dialogBuilder.show()
//        picks start time
        alertDialog.pick_start_time.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { _, mHour, mMinute ->
                    startTime = " Starts At: $mHour:$mMinute"
                    alertDialog.start_time.text = startTime
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }
//        picks end time
        alertDialog.pick_end_time.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { _, mHour, mMinute ->
                    endTime = "Ends At: $mHour:$mMinute"
                    alertDialog.end_time.text = endTime
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }
//        adds schedule to the conference schedule list
        alertDialog.add_new_schedule.setOnClickListener {
            var scheduleName = ""
            var speakers = ""
            scheduleName = alertDialog.program_name.text.toString()
            speakers = alertDialog.speakers.text.toString()
            confSpeakers += ", $speakers"
            if (startTime != "" && endTime != "" && speakers != "" && scheduleName != "") {
                val newSchedule = Schedule(startTime, endTime, scheduleName, speakers)
                Log.d("newSchedule", newSchedule.toString())
                schedules.add(newSchedule)
                adapter.addToSchedule(newSchedule)
                alertDialog.dismiss()

            } else {
                alertDialog.errorView.text = "Please enter a valid schedule"
            }
        }
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
    private fun updateConference(validator: AwesomeValidation) {
        val user = FirebaseAuth.getInstance().currentUser?.uid
        binding.updateBtn.setOnClickListener {
            Log.d("reached", "button clicked")
            if (validator.validate()) {
                if (!isLogoPicked) {
                    Toast.makeText(context, "Please pick a logo", Toast.LENGTH_LONG)
                } else if (dateString == null) {
                    Toast.makeText(context, "Please pick a date", Toast.LENGTH_LONG)
                } else {
                    activity?.applicationContext?.let {
                        if (user != null) {
                            updateConferenceViewModel.updateConference(
                                it,
                                imageURI,
                                binding.conferenceName.text.toString(),
                                binding.conferenceDescription.text.toString(),
                                dateString!!,
                                adapter.schedules,
                                confSpeakers,
                                binding.confVenue.text.toString(),
                                user,
                                conferenceId
                            )
                        }
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