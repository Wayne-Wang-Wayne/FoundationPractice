package com.foundationPractice.recyclerViewFunction

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.rockex6.practiceappfoundation.R
import com.foundationPractice.customView.CustomTabLayout
import com.foundationPractice.recyclerViewFunction.model.BaseFormatPokeModel
import com.foundationPractice.util.loadImage
import kotlinx.android.synthetic.main.item_pokemon_image.view.*
import kotlinx.android.synthetic.main.item_pokemon_type.view.*

class PokeRecyclerviewAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data = ArrayList<BaseFormatPokeModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<BaseFormatPokeModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder: RecyclerView.ViewHolder
        val view: View
        when (viewType) {
            PokeModelEnum.POKEMON_PICTURE -> {
                view = inflater.inflate(R.layout.item_pokemon_image, parent, false)
                viewHolder = PokeImageViewHolder(view)
            }
            PokeModelEnum.POKEMON_TYPES -> {
                view = inflater.inflate(R.layout.item_pokemon_type, parent, false)
                viewHolder = PokeTypeViewHolder(view)
            }
            else -> {
                view = inflater.inflate(R.layout.item_pokemon_image, parent, false)
                viewHolder = PokeImageViewHolder(view)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        when (getItemViewType(position)) {
            PokeModelEnum.POKEMON_PICTURE -> {
                holder as PokeImageViewHolder
                holder.pokeImageView.loadImage(context, item as String)
            }
            PokeModelEnum.POKEMON_TYPES -> {
                holder as PokeTypeViewHolder
                holder.pokeTabView.let {
                    for (tabInfo in item as ArrayList<String>){
                        it.addTab(it.newTab().setText(tabInfo))
                    }
                }

                holder.pokeTabView.setHasTransientState(true)
            }
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].type
    }

    private fun getItem(position: Int): Any? {
        return if (data[position].content != null) {
            data[position].content
        } else {
            null
        }
    }
}

class PokeImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val pokeImageView: AppCompatImageView = itemView.pokeImageView
}

class PokeTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val pokeTabView: CustomTabLayout = itemView.pokeTabLayout
}





