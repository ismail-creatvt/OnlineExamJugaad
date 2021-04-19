package com.ismail.creatvt.onlineexamjugaad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T, U, V> LiveData<T>.merge(other:LiveData<U>, block: (T?, U?)->V): LiveData<V> {
    val result = MediatorLiveData<V>()
    result.addSource(this){
        result.postValue(block(it, other.value))
    }
    result.addSource(other){
        result.postValue(block(this.value, it))
    }
    return result
}