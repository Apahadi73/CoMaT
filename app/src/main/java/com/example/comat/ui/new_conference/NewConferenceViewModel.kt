package com.example.comat.ui.new_conference

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.comat.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class NewConferenceViewModel : ViewModel() {

    fun createNewConference(context: Context, imageUri: Uri,name:String,description:String,date:String,schedule:String,speakers:String,venue:String): Unit {
        Log.d("reached", "reached")
        val user = FirebaseAuth.getInstance().currentUser
        val databaseReference = FirebaseDatabase.getInstance().reference
        val storageReference = FirebaseStorage.getInstance().reference;
        val imageStorageBucketRef: StorageReference = storageReference.child("images/${user?.uid}")
//        puts image into the firebase storage, fetches image download url and uploads the user profile to the backend
        imageStorageBucketRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot -> // Get a URL to the uploaded content
                // Get a URL to the uploaded content
                val downloadTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                downloadTask.addOnSuccessListener {
                    val imageDownloadUrl = it.toString()
                    Log.d("response", "downloadUrl:$imageDownloadUrl")
                    val userDb = user?.uid?.let { it1 -> databaseReference.child("users").child(it1) }
                    if (userDb != null) {
                        userDb.child("name").setValue(name)
                        userDb.child("description").setValue(description)
                        userDb.child("date").setValue(date)
                        userDb.child("schedule").setValue(schedule)
                        userDb.child("speakers").setValue(speakers)
                        userDb.child("venue").setValue(venue)
                        userDb.child("logoUrl").setValue(imageDownloadUrl)
                        NavController(context).navigate(R.id.action_nav_new_conference_to_nav_conferences)
                    }
                }
                Log.d("response", "Successfully uploaded the data to firebase")
            }
            .addOnFailureListener {
                Log.d("response", "Unsuccessful to upload the image to firebase")
            }
    }
}