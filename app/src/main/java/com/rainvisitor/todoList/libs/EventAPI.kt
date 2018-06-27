package com.rainvisitor.todoList.libs


import com.rainvisitor.todoList.libs.EventAPI.Factory.DESCRIPTION
import com.rainvisitor.todoList.libs.EventAPI.Factory.ID
import com.rainvisitor.todoList.libs.EventAPI.Factory.ORDERS
import com.rainvisitor.todoList.libs.EventAPI.Factory.STATE
import com.rainvisitor.todoList.libs.EventAPI.Factory.USER_ID
import com.rainvisitor.todoList.models.Event
import com.rainvisitor.todoList.models.GeneralResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface EventAPI {

    @GET("/users/eventList")
    fun get(@Query(USER_ID) userId: Int): Call<MutableList<Event>>

    @GET("/users/addEvent")
    fun add(@Query(USER_ID) userId: Int,
            @Query(DESCRIPTION) description: String,
            @Query(ORDERS) orders: Int): Call<GeneralResponse>

    @GET("/users/updateEvent")
    fun update(@Query(ID) id: Int,
               @Query(DESCRIPTION) description: String,
               @Query(STATE) state: Int): Call<GeneralResponse>

    @GET("/users/deleteEvent")
    fun delete(@Query(ID) id: Int): Call<GeneralResponse>

    object Factory {

        const val ID = "id"
        const val USER_ID = "userId"
        const val DESCRIPTION = "description"
        const val STATE = "state"
        const val ORDERS = "orders"

        private const val HOST = "35.194.251.203"
        private const val PORT = 3000
        private const val API_URL = "http://$HOST:$PORT"

        fun create(): EventAPI {
            val client = OkHttpClient.Builder().build()
            val retrofit = Retrofit.Builder()
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(API_URL)
                    .build()
            return retrofit.create(EventAPI::class.java)
        }
    }
}