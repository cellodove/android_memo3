package com.peter.memo3.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

import com.peter.memo3.R
import com.peter.memo3.databinding.ActivityMainBinding
import com.peter.memo3.gallery.GalleryActivity
import com.peter.memo3.main.fragment.editor.EditorFragment
import com.peter.memo3.main.fragment.list.ListFragment
import com.peter.memo3.main.fragment.viewer.ViewerFragment
import com.peter.memo3.util.FragmentType

class MainActivity : AppCompatActivity() {

    companion object{
        const val REQUEST_CODE_GALLERY = 0;
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    //텍스트 변경시 이벤트처리 함수
    private val textWatcher = object : TextWatcher{

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}

        //start 위치에서 before 문자열 개수만큼 문자열이 count 개수만큼 변경되었을 때 호출
        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.changeTitle(text.toString())
        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        setListener()
        setObserver()

    }



    private fun setListener() {
        binding.apply {
            //뒤로가기 누르면 리스트로 화면이 넘어감
            toolbarBack.setOnClickListener {
                viewModel.changeFragmentType(FragmentType.LIST)
            }
            //저장버튼을누르면 저장이되고 리스트로 넘어감
            save.setOnClickListener {
                //수정인지 새로만드는것인지 확인해야함
                if (viewModel.isFirstMemo()){
                    viewModel.insertMemo()
                }else{
                    viewModel.updateMemo()
                }
                viewModel.changeFragmentType(FragmentType.LIST)
            }

            //쓰기버튼을 누르면 에디트화면으로 넘어감
            edit.setOnClickListener {
                viewModel.changeFragmentType(FragmentType.EDITOR)
            }

            addImage.setOnClickListener {
                val intent = Intent(this@MainActivity, GalleryActivity::class.java)
                val fragment = supportFragmentManager.findFragmentByTag(EditorFragment::class.java.name)
                fragment?.startActivityForResult(intent, REQUEST_CODE_GALLERY)
            }
        }
    }


    private fun setObserver() {
        viewModel.fragmentTypeLiveData.observe(this) {

            val fragment = getFragmentByType(it)
            changeFragment(fragment)

            binding.apply {
                toolbarText.apply {
                    text = getToolbarText(it)
                    isVisible = isTitleVisibility(it)
                }
                toolbarBack.isVisible = isBackVisibility(it)


                inputTitle.apply {
                    isVisible = isInputTitleVisibility(it)

                    if (isInputTitleVisibility(it)) {
                        setText(getEditTitle(it))
                        addTextChangedListener(this@MainActivity.textWatcher)
                    } else {
                        removeTextChangedListener(this@MainActivity.textWatcher)
                    }
                }

                save.isVisible = isSaveVisibility(it)
                edit.isVisible = isEditVisibility(it)
                addImage.isVisible = isAddImageVisibility(it)

            }
        }
    }




///////////////////////함수모음////////////////////////////////

    //프레그먼트 타입을 확인하고 그 프레그먼트 타입을 가져온다.
    private fun getFragmentByType(fragmentType: FragmentType): Fragment =
        when(fragmentType){
            FragmentType.LIST -> ListFragment()
            FragmentType.EDITOR -> EditorFragment()
            FragmentType.VIEWER -> ViewerFragment()
        }

    //입력된 프레그먼트 타입으로 바꾼다.
    private fun changeFragment(fragment: Fragment) {
        val fragManager = supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()

        val fragmentName = fragment::class.java.name
        fragTransaction.replace(
            binding.frameLayout.id,
            fragment,
            fragmentName
        ).commit()
    }

    //리스트화면일때는 툴바에  앱이름나오고 뷰어들어가면 메모제목나오게한다. 매모만들때는 비워둠
    private fun getToolbarText(fragmentType: FragmentType): String =
        when(fragmentType){
            FragmentType.LIST -> "Dove memo"
            FragmentType.VIEWER -> viewModel.memoLiveData.value?.title?:""
            else -> ""
        }

    //작성화면이 아니면 참반환 즉 제목 보여줌 함수
    private fun isTitleVisibility(fragmentType: FragmentType): Boolean =
        fragmentType != FragmentType.EDITOR

    //리스트화면아니면 뒤로가기키 보여줌 화면
    private fun isBackVisibility(fragmentType : FragmentType): Boolean =
        fragmentType != FragmentType.LIST

    //작성화면이면 제목수정하는 textEdit 을 보여준다.
    private fun isInputTitleVisibility(fragmentType: FragmentType): Boolean =
        fragmentType == FragmentType.EDITOR

    //만약 제목이있으면 제목을 보여준다.아니면 빈화면
    private fun getEditTitle(fragmentType: FragmentType): String =
        when(fragmentType){
            FragmentType.EDITOR -> viewModel.memoLiveData.value?.title?:""
            else -> ""
        }

    private fun isSaveVisibility(fragmentType: FragmentType): Boolean =
        fragmentType == FragmentType.EDITOR

    private fun isEditVisibility(fragmentType: FragmentType) : Boolean =
        fragmentType == FragmentType.VIEWER

    private fun isAddImageVisibility(fragmentType: FragmentType) : Boolean =
        fragmentType == FragmentType.EDITOR


    //뒤로가기 누르면 행동
    override fun onBackPressed() {
       when(viewModel.fragmentTypeLiveData.value){
           FragmentType.LIST -> super.onBackPressed()
           FragmentType.VIEWER,
           FragmentType.EDITOR -> viewModel.changeFragmentType(FragmentType.LIST)
       }
    }
}