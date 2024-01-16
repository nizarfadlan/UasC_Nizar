package com.nizarfadlan.uasc_nizar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nizarfadlan.uasc_nizar.adapter.ListTodoAdapter
import com.nizarfadlan.uasc_nizar.model.Todo
import com.nizarfadlan.uasc_nizar.utils.Extensions.checkAndShowNotification
import com.nizarfadlan.uasc_nizar.utils.Extensions.checkPermission
import com.nizarfadlan.uasc_nizar.utils.Extensions.showNotification
import com.nizarfadlan.uasc_nizar.utils.Extensions.toast
import com.nizarfadlan.uasc_nizar.utils.Request
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class TodoListActivity : AppCompatActivity(), ListTodoAdapter.OnItemClickListener {
    private lateinit var rvTodo: RecyclerView
    private val list = ArrayList<Todo>()
    private val client = OkHttpClient()
    private var request = Request(client)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)
        checkPermission()

        val tvBack: TextView = findViewById(R.id.tv_back)
        tvBack.setOnClickListener {
            goToMainActivity()
        }

        val userId = intent.getIntExtra(EXTRA_USER_ID, 1)

        rvTodo = findViewById(R.id.todoRecyclerView)
        rvTodo.setHasFixedSize(true)

        getListTodos("https://jsonplaceholder.typicode.com/users/$userId/todos")
    }

    private fun getListTodos(url: String){
        request.get(url, object : Callback {
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
        checkAndShowNotification(todo.id, "Todo", "Kamu memilih todo ${todo.id} dengan title ${todo.title}")
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    companion object {
        private const val TAG = "MainActivity"
        const val EXTRA_USER_ID = "extra_user_id"
    }
}