package com.example.comat.ui.new_conference

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.comat.models.Schedule
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class NewConferenceViewModel : ViewModel() {

    private val _navigate = MutableLiveData<Boolean>().apply {
        value = false
    }
    val navigate: LiveData<Boolean> = _navigate

    fun createNewConference(
        context: Context,
        imageUri: Uri,
        name: String,
        description: String,
        date: String,
        schedule: ArrayList<Schedule>,
        speakers: String,
        venue: String,
        currentUser: String
    ): Unit {
        Log.d("reached", "reached")
        val user = FirebaseAuth.getInstance().currentUser
        val imageId: UUID = UUID.randomUUID()
        val databaseReference = FirebaseDatabase.getInstance().reference
        val storageReference = FirebaseStorage.getInstance().reference;
        val imageStorageBucketRef: StorageReference = storageReference.child("images/${imageId}")
//        puts image into the firebase storage, fetches image download url and uploads the user profile to the backend
        imageStorageBucketRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot -> // Get a URL to the uploaded content
                // Get a URL to the uploaded content
                val downloadTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                downloadTask.addOnSuccessListener {
                    val imageDownloadUrl = it.toString()
                    Log.d("response", "downloadUrl:$imageDownloadUrl")
                    val conferenceId = UUID.randomUUID().toString()
                    val userDb = user?.uid?.let { it1 -> databaseReference.child("users").child(it1).child("my_conferences").push() }
                    val conferenceDb = databaseReference.child("conferences").child(conferenceId)
                    if (userDb != null) {
                        userDb.setValue(conferenceId)
                        conferenceDb.child("name").setValue(name)
                        conferenceDb.child("description").setValue(description)
                        conferenceDb.child("date").setValue(date)
                        conferenceDb.child("schedule").setValue(schedule)
                        conferenceDb.child("speakers").setValue(speakers)
                        conferenceDb.child("venue").setValue(venue)
                        conferenceDb.child("logoUrl").setValue(imageDownloadUrl)
                        conferenceDb.child("creator").setValue(currentUser)
                        conferenceDb.child("conference_id").setValue(conferenceId)
                        _navigate.value = true
                    }
                }
                Log.d("response", "Successfully uploaded the data to firebase")
            }
            .addOnFailureListener {
                Log.d("response", "Unsuccessful to upload the image to firebase")
            }
    }
}