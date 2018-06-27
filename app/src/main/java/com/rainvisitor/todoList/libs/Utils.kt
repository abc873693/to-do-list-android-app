package com.rainvisitor.todoList.libs

import android.graphics.Paint
import android.widget.TextView

/**
 * Created by Ray on 2017/2/24.
 */

object Utils {

    fun addDeleteLine(textView: TextView) {
        val paint = textView.paint
        paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        paint.isAntiAlias = true
    }

    fun removeDeleteLine(textView: TextView) {
        val paint = textView.paint
        paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        paint.isAntiAlias = false
    }
}
