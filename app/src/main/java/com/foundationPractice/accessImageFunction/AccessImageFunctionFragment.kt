package com.foundationPractice.accessImageFunction

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rockex6.practiceappfoundation.R
import android.graphics.Bitmap
import kotlinx.android.synthetic.main.fragment_access_image_function.*
import kotlin.math.roundToInt


class AccessImageFunctionFragment : Fragment() {


    private val RESULT_LOAD_IMAGE = 7788
    private lateinit var mContext: Context


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_access_image_function, container, false)
    }


    companion object {
        fun newInstance() = AccessImageFunctionFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        takePictureButton.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, RESULT_LOAD_IMAGE)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            val selectedImage: Uri? = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = selectedImage?.let {
                mContext.applicationContext.contentResolver.query(it, filePathColumn, null, null,
                    null)
            }
            if (cursor != null) {
                cursor.moveToFirst()
                val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                val picturePath: String = cursor.getString(columnIndex)
                cursor.close()
                val bitMap = getScaledBitmap(picturePath, 800, 800)
                cameraImage.setImageBitmap(bitMap)
            }
        }
    }


    private fun getScaledBitmap(picturePath: String, width: Int, height: Int): Bitmap {
        val sizeOptions = BitmapFactory.Options()
        sizeOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(picturePath, sizeOptions)
        val inSampleSize = calculateInSampleSize(sizeOptions, width, height)
        sizeOptions.inJustDecodeBounds = false
        sizeOptions.inSampleSize = inSampleSize
        return BitmapFactory.decodeFile(picturePath, sizeOptions)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
            val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }




}


