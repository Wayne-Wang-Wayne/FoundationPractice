package com.foundationPractice.cameraFunction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rockex6.practiceappfoundation.R
import kotlinx.android.synthetic.main.fragment_camera_function.*
import androidx.core.app.ActivityCompat.startActivityForResult

import android.provider.MediaStore

import android.content.Intent
import android.graphics.BitmapFactory

import android.graphics.Bitmap

import android.app.Activity
import java.io.ByteArrayOutputStream


class CameraFunctionFragment : Fragment() {

    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camera_function, container, false)
    }

    companion object {
        fun newInstance() = CameraFunctionFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        takePictureButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val bmp = data?.extras!!["data"] as Bitmap?
                val stream = ByteArrayOutputStream(300)
                bmp!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray: ByteArray = stream.toByteArray()

                // convert byte array to Bitmap
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

                cameraImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 1000, 1000, false))
            }
        }
    }


}