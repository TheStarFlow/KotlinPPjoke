package com.zzs.network.service

import android.os.Build
import androidx.annotation.RequiresApi
import com.zzs.network.common.NetworkConstant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Socket
import java.security.SecureRandom
import java.security.SecureRandomSpi
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

class ApiService private constructor() {
    private val okHttpClient: OkHttpClient
    private val retrofit :Retrofit

    companion object{
        val INSTANCE by lazy {
            ApiService()
        }
    }


    init {
        val timeOut = 5L
        val log = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(log)
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .writeTimeout(timeOut, TimeUnit.SECONDS)
            .build()
        val trustManager = Array(1) {
            @RequiresApi(Build.VERSION_CODES.N)
            object : X509TrustManager {
                override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
                }

                override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                   return arrayOf()
                }

            }
        }
        try {
            val ssl = SSLContext.getInstance("SSL")
            ssl.init(null, trustManager, SecureRandom())

            HttpsURLConnection.setDefaultSSLSocketFactory(ssl.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier(object : HostnameVerifier {
                override fun verify(p0: String?, p1: SSLSession?): Boolean {
                    return true
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

        retrofit = getRetrofit()
    }

    @JvmName("getRetrofit1")
    private fun getRetrofit(): Retrofit {
        val builder = Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            baseUrl(NetworkConstant.SERVICE_ADDRESS)
            client(okHttpClient)
        }
        return builder.build()
    }

    fun <T> create(service:Class<T>) :T{
        return  retrofit.create(service)
    }
}