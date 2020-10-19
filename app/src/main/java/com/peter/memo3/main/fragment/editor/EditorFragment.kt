package com.peter.memo3.main.fragment.editor

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.peter.memo3.R
import com.peter.memo3.base.BaseFragment
import com.peter.memo3.databinding.FragmentEditorBinding
import com.peter.memo3.main.MainActivity
import com.peter.memo3.main.MainViewModel

class EditorFragment : BaseFragment<FragmentEditorBinding>(){

    //뷰모델
    private val viewModel by activityViewModels<MainViewModel>()

    //에디트를 가져온다.
    override fun getLayout(): Int = R.layout.fragment_editor


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        setObserver()
    }


    private fun setListener() {
        binding.apply {
            inputContent.addTextChangedListener(object : TextWatcher{

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.changeContent(s.toString())
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun setObserver() {
        viewModel.apply {
            memoLiveData.observe(
                viewLifecycleOwner,
                Observer {
                    binding.apply {
                        inputContent.setText(it.content)

                        Glide.with(requireActivity())
                            .load(it.imageUrl)
                            .into(image)
                    }
                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MainActivity.REQUEST_CODE_GALLERY){
            val imageUrl = data?.getStringExtra("image_url") ?: ""
            viewModel.changeImageUrl(imageUrl)

            Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap>(){

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        Log.d("bitmap info", "${resource.width}, ${resource.height}")
                        binding.image.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}

                })
        }
    }



}