package com.example.realmdb

import android.app.Application
import com.example.realmdb.model.GameModel
import com.example.realmdb.model.UserModel
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class App : Application() {

    companion object{
        val config: RealmConfiguration by lazy {
            RealmConfiguration.Builder(
                schema = setOf(
                    UserModel::class,
                    GameModel::class
                )
            ).build()
        }

        val realm: Realm by lazy {
            Realm.open(config)
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}