package com.nizarfadlan.uasc_nizar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nizarfadlan.uasc_nizar.Extensions.showNotification
import com.nizarfadlan.uasc_nizar.Extensions.toast
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class MainActivity : AppCompatActivity(), ListTodoAdapter.OnItemClickListener {
    private lateinit var rvTodo: RecyclerView
    private val list = ArrayList<Todo>()
    private val client = OkHttpClient()
    private var request = OkHttpRequest(client)

    class OkHttpRequest(private val client: OkHttpClient) {

        fun get(url: String, callback: Callback): Call {
            val request = Request.Builder()
                .url(url)
                .header("Content-Type", JSON.toString())
                .build()

            val call = client.newCall(request)
            call.enqueue(callback)
            return call
        }

        companion object {
            val JSON = MediaType.parse("application/json; charset=utf-8")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvTodo = findViewById(R.id.todoRecyclerView)
        rvTodo.setHasFixedSize(true)

        getListTodos("https://jsonplaceholder.typicode.com/todos")
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

                        val todo = Todo(
                            userId = jsonObject.getInt("userId"),
                            id = jsonObject.getInt("id"),
                            title = jsonObject.getString("title"),
                            complete = jsonObject.getBoolean("completed")
                        )

                        list.add(todo)
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
        rvTodo.layoutManager = LinearLayoutManager(this)
        val listTodoAdapter = ListTodoAdapter(this, list)
        listTodoAdapter.setOnItemClickListener(this)
        rvTodo.adapter = listTodoAdapter
    }

    override fun onItemClick(todo: Todo) {
        toast("Kamu memilih todo ${todo.id} dengan title ${todo.title}")
        showNotification(this, todo.id, "Todo", "Kamu memilih todo ${todo.id} dengan title ${todo.title}")
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}