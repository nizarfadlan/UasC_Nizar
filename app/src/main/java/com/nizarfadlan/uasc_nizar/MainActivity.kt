package com.nizarfadlan.uasc_nizar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nizarfadlan.uasc_nizar.adapter.ListUserAdapter
import com.nizarfadlan.uasc_nizar.model.User
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import com.nizarfadlan.uasc_nizar.utils.Request

class MainActivity : AppCompatActivity(), ListUserAdapter.OnItemClickListener {
    private lateinit var rvUser: RecyclerView
    private val list = ArrayList<User>()
    private val client = OkHttpClient()
    private var request = Request(client)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvUser = findViewById(R.id.userRecyclerView)
        rvUser.setHasFixedSize(true)

        getListTodos("https://jsonplaceholder.typicode.com/users")
    }

    private fun getListTodos(url: String){
        request.get(url, object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.message?.let { Log.d(TAG, it) }
            }

            override fun onResponse(call: Call, response: Response) {
                val strResponse = response.body()?.string()

                try {
                    val jsonArray = JSONArray(strResponse)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)

                        val user = User(
                            id = jsonObject.getInt("id"),
                            name = jsonObject.getString("name"),
                            email = jsonObject.getString("email")
                        )

                        list.add(user)
                    }

                    runOnUiThread {
                        setupRv()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun setupRv(){
        rvUser.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(this, list)
        listUserAdapter.setOnItemClickListener(this)
        rvUser.adapter = listUserAdapter
    }

    override fun onItemClick(user: User) {
        val intent = Intent(this, TodoListActivity::class.java)
        intent.putExtra(TodoListActivity.EXTRA_USER_ID, user.id)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}