package com.foundationPractice.homePage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rockex6.practiceappfoundation.R

class BottomBarAdapter(
    private val context: Context,
    private val bottomBarModels: ArrayList<BottomBarModel>,
    private val bottomBarOnClick: BottomBarOnClick) : RecyclerView.Adapter<BottomBarViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomBarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bottom_bar, null, false)
        return BottomBarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bottomBarModels.size
    }

    override fun onBindViewHolder(holder: BottomBarViewHolder, position: Int) {
        val bottomBarModel = bottomBarModels[position]
        holder.vBottomBarTitle.text = bottomBarModel.title
        holder.vBottomBarTitle.isSelected = bottomBarModel.isSelect
        holder.itemView.tag = bottomBarModel.id
        holder.itemView.setOnClickListener {
            bottomBarModels.forEach {
                it.isSelect = false
            }
            bottomBarModel.isSelect = true
            notifyDataSetChanged()
            bottomBarOnClick.onBottomBarClick(bottomBarModel.id)
        }
        if(bottomBarModel.id == "news"){
            bottomBarOnClick.onBottomBarClick(bottomBarModel.id)
        }
    }
}


class BottomBarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val vBottomBarTitle: TextView = itemView.findViewById(R.id.vBottomBarTitle)
}

interface BottomBarOnClick {
    fun onBottomBarClick(id: String)
}