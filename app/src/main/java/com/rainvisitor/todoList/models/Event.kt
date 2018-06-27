package com.rainvisitor.todoList.models

import com.google.gson.annotations.SerializedName

class Event {

    @SerializedName("id")
    var id: Int = 0
    @SerializedName("userId")
    var userId: Int? = 0
    @SerializedName("description")
    var description: String? = ""
    @SerializedName("date")
    var date: String? = null
    @SerializedName("state")
    var state: Int? = 0
    @SerializedName("orders")
    var orders: Int? = 0

    override fun toString(): String {
        return "Event(id=$id, userId=$userId, description=$description, date=$date, status=$state, sortingId=$orders)"
    }
}
