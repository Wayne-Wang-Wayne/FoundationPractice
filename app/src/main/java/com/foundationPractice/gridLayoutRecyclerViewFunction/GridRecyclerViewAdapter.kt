package com.setDDG.gridLayoutRecyclerViewFunction

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.rockex6.practiceappfoundation.R
import com.setDDG.util.loadImage
import kotlinx.android.synthetic.main.item_project_news_list.view.*


class GridRecyclerViewAdapter(
    private val context: Context) : RecyclerView.Adapter<GridLayoutListViewHolder>() {


    private var data: ArrayList<String> = ArrayList()

    fun setData(data: ArrayList<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridLayoutListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_project_news_list, parent, false)

        val lp = view.layoutParams
        lp.height = parent.measuredHeight / 4
        view.layoutParams = lp
        return GridLayoutListViewHolder(view)
    }


    override fun onBindViewHolder(holder: GridLayoutListViewHolder, position: Int) {
        holder.pokemonImageView.loadImage(context,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${data[position]}.png")
    }

    override fun getItemCount(): Int {
        return data.size
    }

}


class GridLayoutListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val pokemonImageView: AppCompatImageView = itemView.pokemonImageView
}