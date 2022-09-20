package com.example.realmdb

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.realmdb.model.UserModel
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.reflect.Field

class MainActivity : AppCompatActivity() {


    enum class Mode{
        NEW, SAVE, SELECTED
    }

    private val config: RealmConfiguration by lazy {
        RealmConfiguration.Builder(schema = setOf(UserModel::class))
            .build()
    }
    private val realm: Realm by lazy {
        Realm.open(config)
    }
    private val fId: EditText by lazy {
        findViewById(R.id.fId)
    }
    private val fName: EditText by lazy {
        findViewById(R.id.fName)
    }
    private val fAge: EditText by lazy {
        findViewById(R.id.fAge)
    }
    private val rvUsers: RecyclerView by lazy {
        findViewById(R.id.rvUsers)
    }
    private val btSave: Button by lazy {
        findViewById(R.id.btSave)
    }
    private val btDelete: Button by lazy {
        findViewById(R.id.btDelete)
    }
    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.progressBar)
    }
    private var adapter: UserAdapter ?= null

    private var listUsers = arrayListOf<UserModel>()


    private var mode: Mode = Mode.NEW
    private var seletedUserId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        btDelete.setOnClickListener {
            when(mode){
                Mode.NEW -> clearFields()
                Mode.SELECTED -> deleteUser()
                else -> {}
            }
        }

        btSave.setOnClickListener {
            when(mode){
                Mode.NEW -> newUser()
                Mode.SELECTED -> updateUser()
                Mode.SAVE -> {}
            }
        }

        loadData()
    }

    private fun loadData(){
        try{
            CoroutineScope(Dispatchers.Main).launch {
                progressBar.visibility = View.VISIBLE
                listUsers = arrayListOf()
                delay(200)
                val listUsers: RealmResults<UserModel> = App.realm.query<UserModel>().find()
                listUsers.forEach {
                    this@MainActivity.listUsers.add(it)
                }
                adapter = UserAdapter(this@MainActivity.listUsers, object : UserAdapter.IUserListener{
                    override fun onClick(user: UserModel) {
                        fId.setText(user.id.toString())
                        fName.setText(user.name)
                        fAge.setText(user.age.toString())
                        seletedUserId = user.id!!
                        mode = Mode.SELECTED
                    }
                })
                rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
                rvUsers.adapter = adapter
                adapter?.notifyDataSetChanged()
                progressBar.visibility = View.GONE
            }
        }catch (e: Exception){
            progressBar.visibility = View.GONE
        }
    }

    private fun newUser(){
        CoroutineScope(Dispatchers.Main).launch {
            progressBar.visibility = View.VISIBLE
            try {
                val user = UserModel().apply {
                    id = nextinc()
                    name = fName.text.toString()
                    age = fAge.text.toString().toInt()
                }

                App.realm.writeBlocking {
                    copyToRealm(user)
                }
                this@MainActivity.listUsers.add(user)
                adapter?.notifyDataSetChanged()
                clearFields()
                mode = Mode.NEW
            }catch (e: Exception){
                Log.d("TEST_RM","Erorr: "+e.message)
            }finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun updateUser(){
        CoroutineScope(Dispatchers.Main).launch {
            progressBar.visibility = View.VISIBLE
            App.realm.write {
                val user = this.query<UserModel>("id=$0",seletedUserId).first().find()
                user?.apply {
                    name = fName.text.toString()
                    age = fAge.text.toString().toInt()
                }
            }
            clearFields()
            loadData()
            mode = Mode.NEW
            progressBar.visibility = View.GONE
        }
    }

    private fun deleteUser(){
        CoroutineScope(Dispatchers.Main).launch {
            progressBar.visibility = View.VISIBLE
            App.realm.write {
                val user = this.query<UserModel>("id=$0",seletedUserId).first().find()
                delete(user!!)
            }
            clearFields()
            loadData()
            mode = Mode.NEW
            progressBar.visibility = View.GONE
        }
    }



    private fun clearFields(){
        fId.setText("")
        fName.setText("")
        fAge.setText("")
    }
}