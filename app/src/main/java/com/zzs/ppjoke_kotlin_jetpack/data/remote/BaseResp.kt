package com.zzs.ppjoke_kotlin_jetpack.data.remote

data class BaseResp<T>(
    val data: ModelList<T>,
    val message: Any,
    val status: Int
)

data class ModelList<T>(
    val data: List<T>
)

data class BaseResponse<T>(
    val data: Model<T>,
    val message: Any,
    val status: Int
)

data class Model<T>(
    val data: T
)