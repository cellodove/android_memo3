package com.peter.memo3.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.peter.memo3.db.table.memo.Memo
import com.peter.memo3.repository.AppRepository
import com.peter.memo3.util.FragmentType
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
//나중에 관리하기 쉽게 모든 함수는 찢는다 처음에 좀 귀찮을수있어도.
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _fragmentTypeLiveData =  MutableLiveData<FragmentType>()
    private val _memoLiveData = MutableLiveData<Memo>()

    val fragmentTypeLiveData: LiveData<FragmentType>
        get() = _fragmentTypeLiveData

    val memoLiveData: LiveData<Memo>
        get() = _memoLiveData


    //리스트화면에서 데이터들 가지고오겠다
    init {
        _fragmentTypeLiveData.value = FragmentType.LIST
    }

    //화면전환하겠다
    fun changeFragmentType(fragmentType: FragmentType) {
        _fragmentTypeLiveData.value = fragmentType
    }

    //타이틀 수정 텍스트 와쳐가 호출하면 타이틀의 데이터를 바꾼다.
    fun changeTitle(title: String) {
        _memoLiveData.value?.title = title
    }



    //아이디는 메모가 생성될때 자동으로 1씩 증가함 아이디가0이라는것은 처음 만들어진 메모라는뜻
    fun isFirstMemo(): Boolean =
        _memoLiveData.value?.id == 0


    fun insertMemo() {
        viewModelScope.launch {
            val currentMemo = _memoLiveData.value
            currentMemo?.date = getCurrentDate()

            if (currentMemo != null)
                repository.insertMemo(currentMemo)
        }
    }

    //입력될때 날자와시간을 가져오는 함수
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }

    //레포지토리 연결
    private val repository: AppRepository by lazy {
        AppRepository.getInstance(getApplication())
    }

    //새로만드는것과 별만 다르지 않음
    fun updateMemo() {
        viewModelScope.launch {
            val currentMemo = _memoLiveData.value
            currentMemo?.date = getCurrentDate()

            if (currentMemo!=null){
                repository.updateMemo(currentMemo)
            }
        }
    }


}