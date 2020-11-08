package com.example.comat.ui.conferences

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.comat.models.Conference
import com.example.comat.models.Schedule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ConferencesViewModel : ViewModel() {

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
                loadData(dataSnapshot)
            }

            //            loads messageInfo into the messagelist live data
            private fun loadData(dataSnapshot: DataSnapshot) {
                val schedulesArray = ArrayList<Schedule>()
                val conferencesList = ArrayList<Conference>()
                val conferences = dataSnapshot.child("conferences")
                for (conference in conferences.children) {
                    val creator = conference.child("creator").value.toString()
//                    only fill the messagelist with user chat
                    if (!creator.contains(user.uid)) {
                        val date = conference.child("date").value.toString()
                        val venue = conference.child("venue").value.toString()
                        val speakers = conference.child("speakers").value.toString()
                        val description = conference.child("description").value.toString()
                        val name = conference.child("name").value.toString()
                        val logoUrl = conference.child("logoUrl").value.toString()
                        val creatorId = conference.child("creator").value.toString()
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
                            logoUrl, creatorId,
                        )
                    }
                }
                Log.d("conferences", conferencesList.toString())
                _conferenceList.value=conferencesList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(valueListner)
    }
}