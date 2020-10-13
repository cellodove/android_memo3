package com.peter.memo3.db.table

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface BaseDao<T> {
    @Insert
    fun insert(t:T)

    @Update
    fun update(t:T)

    @Delete
    fun delete(t:T)

}