package com.peter.memo3.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.peter.memo3.db.AppDatabase
import com.peter.memo3.db.table.memo.Memo
import com.peter.memo3.db.table.memo.MemoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
class AppRepository private constructor(application: Application){
    private val memoDao : MemoDao

    init {
        val appDatabase = AppDatabase.getInstance(application)
        memoDao = appDatabase.memoDao()
    }

    //싱글톤
    companion object{
        private lateinit var instance: AppRepository

        fun getInstance(application: Application): AppRepository{
            if (!::instance.isInitialized){
                instance = AppRepository(application)
            }
            return instance
        }
    }

    fun getAllMemoList():LiveData<List<Memo>> = memoDao.getAll()

    suspend fun insertMemo(memo: Memo){
        withContext(Dispatchers.IO){
            memoDao.insert(memo)
        }
    }

    suspend fun deleteMemo(memo: Memo){
        withContext(Dispatchers.IO){
            memoDao.delete(memo)
        }
    }

    suspend fun updateMemo(memo: Memo){
        withContext(Dispatchers.IO){
            memoDao.update(memo)
        }
    }
}