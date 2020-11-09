package com.example.comat.ui.enrolled_conference

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.comat.models.Conference
import com.example.comat.models.Schedule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class EnrolledConferenceViewModel : ViewModel() {
    private lateinit var user: FirebaseUser
    private lateinit var database: DatabaseReference

    private val _conferenceList = MutableLiveData<List<Conference>>().apply {
//        load empty array list to instantiate the message list live data
        value = ArrayList<Conference>()
    }
    val conferenceList = _conferenceList

    //  fetches user name from cloud firestore and updates the profile view
    fun fetchUserData() {
        Log.d("response", "fetched")
        user = FirebaseAuth.getInstance().currentUser!!
        database = FirebaseDatabase.getInstance().reference
        val valueListner = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val schedulesArray = ArrayList<Schedule>()
                val conferencesList = ArrayList<Conference>()
                val conferences = dataSnapshot.child("conferences")
                for (conference in conferences.children) {
                    val enrolled_users = conference.child("enrolled_users")
                    for (item in enrolled_users.children) {
                        val x = item.value.toString()
//                    only fill the list with user created conference
                        if (x.contains(user.uid)) {
                            val date = conference.child("date").value.toString()
                            val venue = conference.child("venue").value.toString()
                            val speakers = conference.child("speakers").value.toString()
                            val description = conference.child("description").value.toString()
                            val name = conference.child("name").value.toString()
                            val logoUrl = conference.child("logoUrl").value.toString()
                            val creatorId = conference.child("creator").value.toString()
                            val conferenceId = conference.child("conference_id").value.toString()
                            Log.d("results", conference.value.toString())
                            for (schedule in conferences.child("schedule").children) {
                                val start = schedule.child("start").value.toString()
                                val end = schedule.child("end").value.toString()
                                val program = schedule.child("program").value.toString()
                                val speakers = schedule.child("speakers").value.toString()
                                val schedule = Schedule(start, end, program, speakers)
                                schedulesArray += schedule
                            }
                            conferencesList += Conference(
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
                Log.d("conferences", conferencesList.toString())
                _conferenceList.value = conferencesList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(valueListner)
    }
}