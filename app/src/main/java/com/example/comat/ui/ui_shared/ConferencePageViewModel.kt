package com.example.comat.ui.ui_shared

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.comat.models.Conference
import com.example.comat.models.Schedule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ConferencePageViewModel : ViewModel() {
    private lateinit var matchedConference: DataSnapshot

    private var _conference = MutableLiveData<Conference>().apply {
        value = Conference("", ArrayList<Schedule>(), "", "", "", "", "", "", "")

    }
    val conference: LiveData<Conference> = _conference

    private var _isEnrolled = MutableLiveData<Boolean>().apply {

    }
    val isEnrolled: LiveData<Boolean> = _isEnrolled

    fun fetchConferenceData(conferenceId: String): Unit {
        var selectedConference = Conference("", ArrayList(), "", "", "", "", "", "", "")
        val database = FirebaseDatabase.getInstance().reference
        val valueListner = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot): Unit {
                val schedulesArray = ArrayList<Schedule>()
                val conferencesList = ArrayList<Conference>()
                val conferences = dataSnapshot.child("conferences")
                for (conference in conferences.children) {
                    val conferenceID = conference.child("conference_id").value.toString()
//                    only fill the list with user created conference
                    if (conferenceID.contains(conferenceId)) {
                        matchedConference = conference
                        val date = conference.child("date").value.toString()
                        val venue = conference.child("venue").value.toString()
                        val speakers = conference.child("speakers").value.toString()
                        val description = conference.child("description").value.toString()
                        val name = conference.child("name").value.toString()
                        val logoUrl = conference.child("logoUrl").value.toString()
                        val creatorId = conference.child("creator").value.toString()
                        val conferenceId = conference.child("conference_id").value.toString()
                        val schedules = conference.child("schedule").value as ArrayList<*>
                        for (x in schedules) {
                            val item = x as HashMap<*, *>
                            val start = item["start"] as String
                            val end = item["end"] as String
                            val program = item["program"] as String
                            val speakers = item["speakers"] as String
                            val schedule = Schedule(start, end, program, speakers)
                            schedulesArray += schedule
                        }
                        _conference.value = Conference(
                            date,
                            schedulesArray,
                            venue,
                            speakers,
                            name,
                            description,
                            logoUrl, creatorId, conferenceId
                        )
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(valueListner)
    }

    //    enrolls user into the conference
    fun enroll(conferenceId: String): Unit {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val databaseReference = FirebaseDatabase.getInstance().reference
        val conference = databaseReference.child("conferences").child(conferenceId)
        val userDb = userId?.let { databaseReference.child("users").child(it) }
        conference.child("enrolled_users").push().setValue(userId)
        userDb?.child("enrolled")?.push()?.setValue(conferenceId).also {
            _isEnrolled.value = true
        }

    }
}