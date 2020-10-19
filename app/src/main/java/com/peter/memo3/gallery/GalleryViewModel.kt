package com.peter.memo3.gallery

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.peter.memo3.gallery.data.ImageData

class GalleryViewModel (application: Application) : AndroidViewModel(application){

    private val cursor by lazy {
        application.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

    }

    private val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED
    )

    private val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    val imageListLiveData = liveData<List<ImageData>> {
        val imageDataList = arrayListOf<ImageData>()

        cursor?.let {
            val idIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val displayNameIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateAddedIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

            while (it.moveToNext()) {
                imageDataList.add(
                    ImageData(
                        it.getLong(idIndex),
                        it.getString(displayNameIndex),
                        it.getLong(dateAddedIndex)
                    )
                )
            }
        }

        emit(imageDataList)
    }

    fun getImageUriByPosition(position: Int): String {
        val id = imageListLiveData.value?.get(position)?.id

        return Uri.withAppendedPath(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            id.toString()
        ).toString()
    }

}

