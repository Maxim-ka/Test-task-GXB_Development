package com.reschikov.gxbdevelopment.testtask.network.wolfram

import com.reschikov.gxbdevelopment.testtask.network.Retrofitable
import com.reschikov.gxbdevelopment.testtask.network.TLSSocketFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.security.KeyStore
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

private const val URL_SERVER = "https://api.wolframalpha.com/v2/"

class WolframRetrofit : Retrofitable{

    private val trustManager = getX509TrustManager()

    private val client = OkHttpClient
        .Builder()
        .sslSocketFactory(createTLSSocketFactory(), trustManager)
        .build()

    private val retrofit = Retrofit
        .Builder()
        .client(client)
        .baseUrl(URL_SERVER)
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()

    override fun getRetrofit(): Retrofit = retrofit

    @Throws(IllegalStateException::class)
    private fun getX509TrustManager() : X509TrustManager{
        val trustManagerFactory = TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm())
            .apply { init(null as KeyStore?) }
        val trustManagers = trustManagerFactory.trustManagers
        if (trustManagers.size != 1 || trustManagers.first() !is X509TrustManager) {
            throw IllegalStateException("Unexpected default trust managers:"
                             + Arrays.toString(trustManagers))
            }
        return  trustManagers.first() as X509TrustManager
    }

    private fun createTLSSocketFactory() : TLSSocketFactory{
        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf (trustManager), null)
        return TLSSocketFactory(sslContext.socketFactory)
    }
}