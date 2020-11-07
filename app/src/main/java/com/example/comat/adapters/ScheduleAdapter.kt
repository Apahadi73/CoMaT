package com.example.comat.adapters

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.comat.databinding.ScheduleItemBinding
import com.example.comat.models.Schedule


class ScheduleAdapter(
    private val context: Context,
) : BaseAdapter() {
    val schedules = ArrayList<Schedule>()

    fun addToSchedule(newSchedule: Schedule): Unit {
        schedules.add(newSchedule)
        Log.d("schedule_new", schedules.toString())
        notifyDataSetChanged()
    }
    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return schedules.size
    }

    override fun getItem(p0: Int): Any {
        return schedules[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        val binding: ScheduleItemBinding = DataBindingUtil.inflate(inflater,
            com.example.comat.R.layout.schedule_item,
            parent,
            false)
        val schedule = getItem(position) as Schedule
        Log.d("test", schedule.toString())
        binding.scheduleName.text = schedule.program
        binding.scheduleSpeakers.text = schedule.speakers
        binding.scheduleStarTime.text = schedule.start
        binding.scheduleEndTime.text = schedule.end
        return binding.root
    }
}
