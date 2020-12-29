package com.college.moderunnermvp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PbAdapter(myContext: Context, historyList:List<FragmentPersonalBest.StatsCard>):RecyclerView.Adapter<PbAdapter.PbViewHolder>() {
    var myCtx = myContext
    var hList = historyList

    inner class PbViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var viewTarget: TextView = itemView.findViewById(R.id.pb_txt_target)
        var viewTopSpeed: TextView = itemView.findViewById(R.id.pb_top_speed)
        var viewPeakAcceleration: TextView = itemView.findViewById(R.id.pb_txt_peak_acceleration)
        var viewBestTime: TextView = itemView.findViewById(R.id.pb_best_time)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PbViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(myCtx)
        var view:View = inflater.inflate(R.layout.pb_card_layout, null)
        var holder: PbAdapter.PbViewHolder = PbViewHolder(view)
        return holder
    }
    override fun getItemCount(): Int {
        return hList.size
    }
    override fun onBindViewHolder(holder: PbViewHolder, position: Int) {
        var stats: FragmentPersonalBest.StatsCard = hList[position]
        holder.viewTarget.text = stats.target
        holder.viewTopSpeed.text = stats.topSpeed
        holder.viewPeakAcceleration.text = stats.acceleration
        holder.viewBestTime.text = stats.finishTime
    }
}