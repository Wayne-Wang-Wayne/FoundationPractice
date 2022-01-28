package com.foundationPractice.gridLayoutRecyclerViewFunction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.rockex6.practiceappfoundation.R
import kotlinx.android.synthetic.main.fragment_grid_layout_function.*

class GridLayoutFunctionFragment : Fragment() {

    private lateinit var gridRecyclerViewAdapter: GridRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_grid_layout_function, container, false)
    }

    companion object {
        fun newInstance() = GridLayoutFunctionFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setData()

    }

    fun init(){
        gridRecyclerViewAdapter = context?.let { GridRecyclerViewAdapter(it) }!!
        pokeRecyclerview.apply {
            layoutManager = GridLayoutManager(context,3)
            adapter = gridRecyclerViewAdapter
        }
    }
    private fun setData(){
        val idList = ArrayList<String>()
        for (count in 1..198){
            idList.add(count.toString())
        }
        gridRecyclerViewAdapter.setData(idList)
    }
}