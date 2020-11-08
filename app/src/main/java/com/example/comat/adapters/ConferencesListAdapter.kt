package com.example.comat.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.comat.R
import com.example.comat.databinding.ConferenceItemBinding
import com.example.comat.models.Conference
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class ConferencesListAdapter() :
    ListAdapter<Conference, ConferencesListAdapter.ConferenceViewHolder>(ConferenceDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ConferencesListAdapter.ConferenceViewHolder {
        return ConferencesListAdapter.ConferenceViewHolder.from(parent)
    }

    override fun onBindViewHolder(
        holder: ConferencesListAdapter.ConferenceViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position)!!)
    }

    class ConferenceViewHolder private constructor(val binding: ConferenceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            currentItem: Conference?,
        ) { //        downloads and populate imageview with user image from firebase image storage
            if (currentItem != null) {
                Picasso.get().load(currentItem.logoUrl).into(binding.logoView)
                Log.d("current",currentItem.toString())
                binding.conf =
                    Conference(
                        currentItem.date,
                        currentItem.schedule,
                        currentItem.venue,
                        currentItem.speakers,
                        currentItem.name,
                        currentItem.description,
                        currentItem.logoUrl, currentItem.creatorId
                    )
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
            ): ConferencesListAdapter.ConferenceViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ConferenceItemBinding.inflate(layoutInflater, parent, false)
                return ConferencesListAdapter.ConferenceViewHolder(binding)
            }
        }
    }

}

class ConferenceDiffCallback : DiffUtil.ItemCallback<Conference>() {
    override fun areItemsTheSame(oldItem: Conference, newItem: Conference): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: Conference,
        newItem: Conference,
    ): Boolean {
        return oldItem == newItem
    }

}