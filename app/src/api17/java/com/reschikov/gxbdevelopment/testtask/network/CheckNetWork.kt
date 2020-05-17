package com.reschikov.gxbdevelopment.testtask.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData

class CheckNetWork(private val context: Context ) : LiveData<Boolean>() {

    private val connectivity : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val receiver = Receiver()

    override fun onActive() {
        super.onActive()
        startOff()
    }

    private fun startOff(){
        context.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onInactive() {
        super.onInactive()
        complete()
    }

    private fun complete(){
        context.unregisterReceiver(receiver)
    }

    private fun checkNet(){
        val networkInfo = connectivity.activeNetworkInfo
        postValue(networkInfo != null && networkInfo.isConnectedOrConnecting)
    }

    inner class Receiver : BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == ConnectivityManager.CONNECTIVITY_ACTION){
                    checkNet()
                }
            }
        }
    }
}