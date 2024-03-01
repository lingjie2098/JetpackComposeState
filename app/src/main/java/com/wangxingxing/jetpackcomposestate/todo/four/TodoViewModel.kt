package com.wangxingxing.jetpackcomposestate.todo.four

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.wangxingxing.jetpackcomposestate.TodoItem

/**
 * author : 王星星
 * date : 2021/9/17 18:19
 * email : 1099420259@qq.com
 * description :
 */

class TodoViewModel : ViewModel() {
    // LingJie's Mark: 与one/TodoViewModel.kt比对。

    // TodoItem集合只读
    // LingJie's Mark: var + private set 等效 val。mutableStateListOf表示todoItems可观察，一旦todoItems中的元素发生增、减、换都生效，但是元素内部变更是不生效的。
    var todoItems = mutableStateListOf<TodoItem>()
        private set

    // 当前正在编辑的TodoItem的索引位置
    private var currentEditPosition by mutableStateOf(-1)
    val currentEditItem: TodoItem?
        get() = todoItems.getOrNull(currentEditPosition)

    fun addItem(item: TodoItem) {
        todoItems.add(item)
    }

    fun removeItem(item: TodoItem) {
        todoItems.remove(item)
        // 移除之后回调，“叉叉”被点击了
        onEditDone()
    }

    // 当TodoItem列表中的条目被选中时，传入该对象，获取它在列表中的索引位置
    fun onEditItemSelected(item: TodoItem) {
        currentEditPosition = todoItems.indexOf(item)
    }

    fun onEditDone() {
        currentEditPosition = -1
    }

    // TodoItem编辑完成，重新给集合中的TodoItem赋值
    // id属性值不能修改，进行校验
    fun onEditItemChange(item: TodoItem) {
        val currentItem = requireNotNull(currentEditItem)
        require(currentItem.id == item.id) {
            "You can only change an item with the same id as currentEditItem"
        }
        todoItems[currentEditPosition] = item
    }
}