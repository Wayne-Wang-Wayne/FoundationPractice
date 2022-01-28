package com.foundationPractice.recyclerViewFunction

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rockex6.practiceappfoundation.R
import kotlinx.android.synthetic.main.fragment_recycler_view_function.*
import kotlinx.android.synthetic.main.retry_layout.*


class RecyclerViewFunctionFragment : Fragment() {

    private lateinit var recyclerViewFunctionViewModel: RecyclerViewFunctionViewModel
    private lateinit var pokeRecyclerviewAdapter: PokeRecyclerviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recycler_view_function, container, false)
    }

    companion object {
        fun newInstance() = RecyclerViewFunctionFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeViewModel()
        recyclerViewFunctionViewModel.fetchPokeData()
    }

    private fun init() {
        recyclerViewFunctionViewModel =
            ViewModelProvider(this).get(RecyclerViewFunctionViewModel::class.java)
        pokeRecyclerviewAdapter = context?.let { PokeRecyclerviewAdapter(it) }!!
        pokeRecyclerview.apply {
            layoutManager = CustomLinearLayoutManager(context)
            adapter = pokeRecyclerviewAdapter
        }
    }

    private fun observeViewModel() {
        recyclerViewFunctionViewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
            errorMessage?.let {
                vRefreshLayout.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
                vErrorMessage.text = it
                vRefreshLayout.setOnClickListener {
                    recyclerViewFunctionViewModel.fetchPokeData()
                    vRefreshLayout.visibility = View.GONE
                }
            }
        })

        recyclerViewFunctionViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            isLoading?.let {
                vProgress.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        recyclerViewFunctionViewModel.pokeFormatList.observe(viewLifecycleOwner, { pokeFormatList ->
            pokeFormatList.let {
                pokeRecyclerviewAdapter.setData(pokeFormatList)
            }
        })
    }
}

class CustomLinearLayoutManager(context: Context?) : LinearLayoutManager(context) {
    private var isScrollEnabled = true

    fun setScrollEnabled(flag: Boolean) {
        isScrollEnabled = flag
        Log.d("setScrollEnabled", flag.toString())
    }

    override fun canScrollVertically(): Boolean {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        Log.d("canScrollVertically", isScrollEnabled.toString())
        return isScrollEnabled && super.canScrollVertically()
    }

}