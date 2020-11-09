package com.example.comat.models

data class Conference(val date:String,val schedule: ArrayList<Schedule>,val venue:String, val speakers:String,val name:String,val description:String,val logoUrl:String,val creatorId:String,val conferenceId:String,val host:String)