package com.wangxingxing.jetpackcomposestate.todo.one

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangxingxing.jetpackcomposestate.TodoItem

/**
 * author : 王星星
 * date : 2021/9/17 18:19
 * email : 1099420259@qq.com
 * description :
 */

class TodoViewModel : ViewModel() {

    private val _todoItems = MutableLiveData(listOf<TodoItem>())
    val todoItems: LiveData<List<TodoItem>> = _todoItems

    // 添加元素
    fun addItem(item: TodoItem) {
        _todoItems.value = _todoItems.value!! + listOf(item)
    }

    // 移除元素
    fun removeItem(item: TodoItem) {
        _todoItems.value = _todoItems.value!!.toMutableList().also {
            it.remove(item)
        }
    }
}