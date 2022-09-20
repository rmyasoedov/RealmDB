package com.example.realmdb.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class GameModel : RealmObject{
    @PrimaryKey
    var id: Int?=null
    var player1: Int?=null
    var player2: Int?=null
    var total: String = ""
    var winner: Int?=null
}