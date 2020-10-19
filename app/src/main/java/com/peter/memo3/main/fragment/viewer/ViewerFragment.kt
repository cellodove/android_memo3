package com.peter.memo3.main.fragment.viewer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.peter.memo3.R
import com.peter.memo3.base.BaseFragment
import com.peter.memo3.databinding.FragmentViewerBinding
import com.peter.memo3.main.MainViewModel

class ViewerFragment : BaseFragment<FragmentViewerBinding>(){
    private val viewModel by activityViewModels<MainViewModel>()

    override fun getLayout(): Int = R.layout.fragment_viewer


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()
    }

    private fun setObserver() {
        viewModel.apply {
            memoLiveData.observe(
                viewLifecycleOwner,
                Observer {
                    binding.apply {
                        content.text = it.content
                        showImage(it.imageUrl)
                    }
                }
            )
        }
    }

    private fun showImage(imageUrl:String) {
        Glide.with(this)
            .load(imageUrl)
            .into(binding.image)
    }


}