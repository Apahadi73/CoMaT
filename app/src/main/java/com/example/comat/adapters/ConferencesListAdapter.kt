package com.example.comat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.comat.databinding.ConferenceItemBinding
import com.example.comat.databinding.ScheduleItemBinding
import com.example.comat.models.Conference
import com.squareup.picasso.Picasso

class ConferencesListAdapter(private val clickListener: ConferenceClickListner) :
    ListAdapter<Conference, ConferencesListAdapter.ConferenceViewHolder>(ConferenceListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConferenceViewHolder {
        return ConferenceViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ConferenceViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class ConferenceViewHolder private constructor(val binding: ConferenceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            currentItem: Conference?,
            clickListener: ConferenceClickListner,
        ) { //        downloads and populate imageview with user image from firebase image storage
            if (currentItem != null) {
                Picasso.get().load(currentItem.logoUrl).into(binding.logoView)
                binding.conf =
                    Conference(
                        currentItem.date,
                        currentItem.schedule,
                        currentItem.venue,
                        currentItem.speakers,
                        currentItem.name,
                        currentItem.description,
                        currentItem.logoUrl, currentItem.creatorId, currentItem.conferenceId
                    )
                binding.learnMoreListner = clickListener
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
            ): ConferenceViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ConferenceItemBinding.inflate(layoutInflater, parent, false)
                return ConferenceViewHolder(binding)
            }
        }
    }

}

class ConferenceListDiffCallback : DiffUtil.ItemCallback<Conference>() {
    override fun areItemsTheSame(oldItem: Conference, newItem: Conference): Boolean {
        return oldItem.conferenceId == newItem.conferenceId
    }

    override fun areContentsTheSame(
        oldItem: Conference,
        newItem: Conference,
    ): Boolean {
        return oldItem == newItem
    }

}


//click listner
class ConferenceClickListner(val clickListener: (conferenceId: String) -> Unit) {
    fun onClick(conference: Conference) = clickListener(conference.conferenceId)
}
