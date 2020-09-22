package com.zzs.network.database

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*

object DataBaseManager{

    //反序列,把二进制数据转换成java object对象
    fun toObject(data: ByteArray): Any? {
        return ByteArrayInputStream(data).use { byteInput ->
            ObjectInputStream(byteInput).use {
                it.readObject()
            }
        }
    }

    //序列化存储数据需要转换成二进制
    private fun <T> toByteArray(body: T): ByteArray {
        return ByteArrayOutputStream().use { byteOut ->
            ObjectOutputStream(byteOut).use {
                it.writeObject(body)
                it.flush()
                byteOut.toByteArray()
            }
        }
    }

    fun <T> delete(key: String, body: T) {
        Cache(key, toByteArray(body)).also {
            CacheDatabase.getInstance().getCacheDao().delete(it)
        }
    }

    fun <T> insert(key: String, body: T) {
        Cache(key, toByteArray(body)).apply {
            CacheDatabase.getInstance().getCacheDao().insert(this)
        }
    }

    fun getCache(key: String): Any? {
        return CacheDatabase.getInstance().getCacheDao().getCache(key)?.let {
            return toObject(it.data)
        }
    }
}