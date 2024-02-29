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
    // LingJie's Mark: backing property（幕后属性）
    private val _todoItems = MutableLiveData(listOf<TodoItem>())
    // LingJie's Mark: 状态
    val todoItems: LiveData<List<TodoItem>> = _todoItems

    // LingJie's Mark: 事件---添加元素
    fun addItem(item: TodoItem) {
        // LingJie's Mark: 单单新增_todoItems.value集合里面的元素是不行的（不能更新状态），需要把一个新的集合赋值给_todoItems.value。---解释：https://developer.android.google.cn/jetpack/compose/state?hl=zh-cn 中的【可组合项中的状态】最后。
        _todoItems.value = _todoItems.value!! + listOf(item)
    }

    // LingJie's Mark: 事件---移除元素
    fun removeItem(item: TodoItem) {
        // LingJie's Mark: toMutableList()会转成一个新的MutableList类型对象。_todoItems.value = _todoItems.value!! - listOf(item) 写法也可以。
        _todoItems.value = _todoItems.value!!.toMutableList().also {
            it.remove(item)
        }
    }
}