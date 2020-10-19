package com.peter.memo3.db.table.memo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.peter.memo3.db.table.BaseDao

@Dao
interface MemoDao : BaseDao<Memo> {

    @Query("select * from memo order by date")
    fun getAll(): LiveData<List<Memo>>
}