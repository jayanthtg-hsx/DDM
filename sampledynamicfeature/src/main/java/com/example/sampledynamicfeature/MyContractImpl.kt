package com.example.sampledynamicfeature

import android.util.Log
import com.example.ddm.MyContract

val TAG = "xDDM:lib"

class MyContractImpl : MyContract {
    override fun initialise() {
        Log.d(TAG, "initialise: ")
    }

    override fun getIntValue(): Int {
        Log.d(TAG, "getIntValue: ")
        return 0
    }

    override fun getStringValue(): String {
        Log.d(TAG, "getStringValue: ")
        return ""
    }
}