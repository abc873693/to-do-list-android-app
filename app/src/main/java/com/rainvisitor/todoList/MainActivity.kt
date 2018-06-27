package com.rainvisitor.todoList

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.rainvisitor.todoList.adapter.EventAdapter
import com.rainvisitor.todoList.libs.EventAPI
import com.rainvisitor.todoList.models.Event
import com.rainvisitor.todoList.models.GeneralResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity() {

    private val userId = 1

    private var eventList: MutableList<Event> = ArrayList()

    private var eventAdapter: EventAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setView()
    }

    private fun setView() {
        swipeRefreshLayout.setOnRefreshListener {
            updateData()
        }
        buttonAdd.setOnClickListener {
            val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.layout_input, null)
            val dialog = AlertDialog.Builder(this@MainActivity, R.style.dialogDefault)
                    .setView(view).create()
            val buttonSend = view.findViewById(R.id.buttonSend) as Button
            buttonSend.setOnClickListener {
                val editData = view.findViewById(R.id.editData) as EditText
                val textInputLayout = view.findViewById(R.id.textInputLayout) as TextInputLayout
                val content = editData.text.toString()
                if (content.isEmpty()) {
                    textInputLayout.error = "請勿留空"
                } else {
                    EventAPI.Factory.create().add(userId, content, eventList.size).enqueue(object : Callback<GeneralResponse> {
                        override fun onFailure(call: Call<GeneralResponse>?, t: Throwable?) {
                            t?.printStackTrace()
                            runOnUiThread {
                                Toast.makeText(this@MainActivity, "錯誤", Toast.LENGTH_SHORT).show()
                                swipeRefreshLayout.isRefreshing = false
                            }
                        }

                        override fun onResponse(call: Call<GeneralResponse>?, response: Response<GeneralResponse>?) {
                            updateData()
                        }
                    })
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
        updateData()
    }

    private fun setRecycleView() {
        eventAdapter = EventAdapter(this, eventList, object : EventAdapter.OnItemClickListener {
            override fun onItemClick(item: Event, position: Int) {
            }

            override fun onStateUpdate(item: Event, position: Int, state: Boolean) {
                EventAPI.Factory.create().update(item.id, item.description
                        ?: "", if (state) 1 else 0).enqueue(object : Callback<GeneralResponse> {
                    override fun onFailure(call: Call<GeneralResponse>?, t: Throwable?) {
                        t?.printStackTrace()
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "錯誤", Toast.LENGTH_SHORT).show()
                            swipeRefreshLayout.isRefreshing = false
                        }
                    }

                    override fun onResponse(call: Call<GeneralResponse>?, response: Response<GeneralResponse>?) {
                        runOnUiThread { Toast.makeText(this@MainActivity, "成功", Toast.LENGTH_SHORT).show() }
                        updateData()
                    }
                })
            }

            override fun onItemEditClick(item: Event, position: Int) {
                val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.layout_input, null)
                val dialog = AlertDialog.Builder(this@MainActivity, R.style.dialogDefault)
                        .setView(view).create()
                val editData = view.findViewById(R.id.editData) as EditText
                val buttonSend = view.findViewById(R.id.buttonSend) as Button
                editData.setText(item.description)
                buttonSend.setOnClickListener {
                    val textInputLayout = view.findViewById(R.id.textInputLayout) as TextInputLayout
                    val content = editData.text.toString()
                    if (content.isEmpty()) {
                        textInputLayout.error = "請勿留空"
                    } else {
                        EventAPI.Factory.create().update(item.id, content, item.state!!).enqueue(object : Callback<GeneralResponse> {
                            override fun onFailure(call: Call<GeneralResponse>?, t: Throwable?) {
                                t?.printStackTrace()
                                runOnUiThread {
                                    Toast.makeText(this@MainActivity, "錯誤", Toast.LENGTH_SHORT).show()
                                    swipeRefreshLayout.isRefreshing = false
                                }
                            }

                            override fun onResponse(call: Call<GeneralResponse>?, response: Response<GeneralResponse>?) {
                                runOnUiThread { Toast.makeText(this@MainActivity, "成功", Toast.LENGTH_SHORT).show() }
                                updateData()
                            }
                        })
                        dialog.dismiss()
                    }
                }
                dialog.show()
            }

            override fun onItemLongClick(item: Event, position: Int) {
                AlertDialog.Builder(this@MainActivity, R.style.dialogDefault)
                        .setTitle("刪除")
                        .setMessage("確定要刪除?")
                        .setNegativeButton(R.string.cancel) { _, _ -> }
                        .setPositiveButton(R.string.ok) { _, _ ->
                            EventAPI.Factory.create().delete(item.id).enqueue(object : Callback<GeneralResponse> {
                                override fun onFailure(call: Call<GeneralResponse>?, t: Throwable?) {
                                    t?.printStackTrace()
                                    runOnUiThread {
                                        Toast.makeText(this@MainActivity, "錯誤", Toast.LENGTH_SHORT).show()
                                        swipeRefreshLayout.isRefreshing = false
                                    }
                                }

                                override fun onResponse(call: Call<GeneralResponse>?, response: Response<GeneralResponse>?) {
                                    runOnUiThread {
                                        Toast.makeText(this@MainActivity, "成功", Toast.LENGTH_SHORT).show()
                                    }
                                    updateData()
                                }
                            })
                        }
                        .show()
            }
        })
        recyclerViewEvent.adapter = eventAdapter
        recyclerViewEvent.layoutManager = LinearLayoutManager(this)
    }

    private fun updateData() {
        swipeRefreshLayout.isRefreshing = true
        EventAPI.Factory.create().get(userId).enqueue(object : Callback<MutableList<Event>> {
            override fun onFailure(call: Call<MutableList<Event>>?, t: Throwable?) {
                t?.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "錯誤", Toast.LENGTH_SHORT).show()
                    swipeRefreshLayout.isRefreshing = false
                }
            }

            override fun onResponse(call: Call<MutableList<Event>>?, response: Response<MutableList<Event>>?) {
                response?.let {
                    it.body()?.let {
                        eventList = it
                        setRecycleView()
                    }
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }
}
