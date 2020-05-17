package com.reschikov.gxbdevelopment.testtask.network

import java.net.InetAddress
import java.net.Socket
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

private const val SSL_V3 = "SSLv3"
private const val TLS_V1_1 = "TLSv1.1"
private const val TLS_V1_2 = "TLSv1.2"

class TLSSocketFactory(private val sslSocketFactory: SSLSocketFactory) : SSLSocketFactory() {

    override fun getDefaultCipherSuites(): Array<String> = sslSocketFactory.defaultCipherSuites

    override fun createSocket(s: Socket?, host: String?, port: Int, autoClose: Boolean): Socket {
        return enableTLS(sslSocketFactory.createSocket(s, host, port, autoClose))
    }

    override fun createSocket(host: String?, port: Int): Socket {
        return enableTLS(sslSocketFactory.createSocket(host, port))
    }

    override fun createSocket(host: String?, port: Int,  localHost: InetAddress?, localPort: Int): Socket {
        return enableTLS(sslSocketFactory.createSocket(host, port, localHost, localPort))
    }

    override fun createSocket(host: InetAddress?, port: Int): Socket = enableTLS(sslSocketFactory.createSocket(host, port))

    override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int): Socket {
        return enableTLS(sslSocketFactory.createSocket(address, port, localAddress, localPort))
    }

    private fun enableTLS(socket: Socket): Socket {
        return  (socket as? SSLSocket)?.apply {
            enabledProtocols = arrayOf(SSL_V3, TLS_V1_1, TLS_V1_2)
        } ?: socket
    }

    override fun getSupportedCipherSuites(): Array<String> = sslSocketFactory.defaultCipherSuites
}