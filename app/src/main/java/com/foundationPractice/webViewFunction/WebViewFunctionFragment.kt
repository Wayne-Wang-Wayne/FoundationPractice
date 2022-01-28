package com.foundationPractice.webViewFunction

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rockex6.practiceappfoundation.R
import com.foundationPractice.util.IntentUtil
import kotlinx.android.synthetic.main.fragment_web_view_function.*


class WebViewFunctionFragment : Fragment() {

    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_web_view_function, container, false)
    }

    companion object {
        fun newInstance() = WebViewFunctionFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            webViewButton.setOnClickListener {

                    IntentUtil.startUrl(mContext, webViewET.text.toString())
                    webViewET.setText("")
            }
    }
}