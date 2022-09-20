package com.example.realmdb.model

import android.util.Log
import com.example.realmdb.App
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class UserModel : RealmObject{
    @PrimaryKey
    var id: Int?=null
    var name: String = ""
    var age: Int ?= null

    constructor(name: String, age: Int) : this() {
        this.name = name
        this.age = age
    }

    constructor(){

    }

    fun nextinc(): Int{
        val listUsers: RealmResults<UserModel> = App.realm.query<UserModel>().find()
        val max = listUsers.maxByOrNull {
            it.id?:0
        }?.id?:0
        return max+1
    }
}
