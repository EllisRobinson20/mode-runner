package com.college.moderunnermvp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(myContext:Context, historyList:List<DataSample>): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    var myCtx = myContext
    var hList = historyList

    inner class HistoryViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        var viewDate:TextView = itemView.findViewById(R.id.title_date)
        var viewFinishTime: TextView = itemView.findViewById(R.id.h_txt_finish_time)
        var viewDistance: TextView = itemView.findViewById(R.id.h_txt_distance_covered)
        var viewAverageSpeed: TextView = itemView.findViewById(R.id.h_txt_final_average_speed)
        var viewTopSpeed: TextView = itemView.findViewById(R.id.h_txt_top_speed)
        var viewPeakAcceleration: TextView = itemView.findViewById(R.id.h_txt_peak_acceleration)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(myCtx)
        var view:View = inflater.inflate(R.layout.history_card_layout, null)
        var holder: HistoryViewHolder = HistoryViewHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        return hList.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        var history: DataSample = hList[position]

        holder.viewDate.text = history.date
        holder.viewFinishTime.text = history.finishTime
        holder.viewDistance.text = history.targetDistance
        holder.viewAverageSpeed.text = history.averageSpeed
        holder.viewTopSpeed.text = history.topSpeed
        holder.viewPeakAcceleration.text = history.peakAcceleration
    }
}