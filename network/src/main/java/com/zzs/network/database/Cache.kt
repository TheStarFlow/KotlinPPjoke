package com.zzs.network.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "cache")
class Cache (
    @ColumnInfo(name = "key") @PrimaryKey(autoGenerate = false ) @NonNull var key:String,
    @ColumnInfo(name = "_data") var data:ByteArray
):Serializable{

}