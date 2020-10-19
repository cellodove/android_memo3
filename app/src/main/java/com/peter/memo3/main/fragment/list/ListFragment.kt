package com.peter.memo3.main.fragment.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.peter.dovememo.main.fragment.list.MemoAdapter
import com.peter.memo3.R
import com.peter.memo3.base.BaseFragment
import com.peter.memo3.databinding.FragmentListBinding
import com.peter.memo3.db.table.memo.Memo
import com.peter.memo3.main.MainViewModel
import com.peter.memo3.util.FragmentType

class ListFragment : BaseFragment<FragmentListBinding>(), MemoAdapter.MemoListListener {

    private val viewModel by activityViewModels<MainViewModel>()

    override fun getLayout(): Int = R.layout.fragment_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        setObserver()
        setListener()
    }

    private fun initRecyclerView() {
        binding.list.apply {
            addItemDecoration(
                //리사이클러뷰 구분선
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )

            adapter = MemoAdapter(this@ListFragment)
        }
    }


    private fun setObserver() {
        viewModel.memoListLiveData.observe(requireActivity(), Observer {
            (binding.list.adapter as MemoAdapter).submitList(it)
        })
    }


    private fun setListener() {
        binding.apply {
            add.setOnClickListener {
                viewModel.changeCurrentMemo(Memo())

                viewModel.changeFragmentType(FragmentType.EDITOR)
            }
        }
    }




    override fun onMemoItemClick(position: Int) {
        viewModel.apply {
            changeCurrentMemo(position)
            changeFragmentType(FragmentType.VIEWER)
        }
    }

    override fun onMemoItemLongClick(position: Int) {
        viewModel.deleteMemo(position)
    }

}