package com.zzs.network.database

import androidx.room.*

@Dao
interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cache: Cache):Long

    @Query("select *from cache where `key`=:key")
    fun getCache(key: String): Cache?

    @Delete
    fun delete(cache: Cache):Int

    //只能传递对象昂,删除时根据Cache中的主键 来比对的
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(cache: Cache): Int

}