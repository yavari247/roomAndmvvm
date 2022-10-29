package com.example.roomdemo.db.repository

import com.example.roomdemo.db.local.Subscriber
import com.example.roomdemo.db.local.SubscriberDAO

class SubscriberRepository(private val dao: SubscriberDAO) {
    val subscribers=dao.getAllSubscribers()

    suspend fun insert(subscriber: Subscriber):Long{
      return  dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber){
        dao.updateSubscriber(subscriber)
    }
    //delete on item by dao class and its methods
    suspend fun delete(subscriber: Subscriber){
        dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll(){
        dao.deleteAll()
    }

}