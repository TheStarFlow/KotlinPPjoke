package com.zzs.network.database

import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zzs.common.globals.AppGlobals

@Database(entities = arrayOf(Cache::class),version = 1,exportSchema = true)
abstract class CacheDatabase constructor(): RoomDatabase() {

    companion object{
       private  var instance:CacheDatabase? = null

        fun getInstance() =  instance?: synchronized(this){
                instance?: buildDatabase().also { instance = it }
        }

        private fun  buildDatabase():CacheDatabase{
            return Room.databaseBuilder(AppGlobals.application,CacheDatabase::class.java,"ppjoke_cache")
                .allowMainThreadQueries()
                .build()
        }
    }

    abstract fun getCacheDao() :CacheDao

}