package com.reschikov.gxbdevelopment.testtask.wolfram

import com.reschikov.gxbdevelopment.testtask.ui.Derivable

interface Requestable<T> {
    suspend fun executeRequest(derivable: Derivable) : T
}