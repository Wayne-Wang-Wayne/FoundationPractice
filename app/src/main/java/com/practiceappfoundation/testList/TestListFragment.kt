package com.practiceappfoundation.testList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practiceappfoundation.util.loadImage
import com.rockex6.practiceappfoundation.databinding.FragmentTestListBinding


class TestListFragment : Fragment() {

    private var _binding: FragmentTestListBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //bindingView
        _binding = FragmentTestListBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
       fun newInstance() = TestListFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.catImage.loadImage(this,"https://cataas.com/cat")
    }

    override fun onResume() {
        super.onResume()
        binding.catImage.loadImage(this,"https://cataas.com/cat")
    }
}