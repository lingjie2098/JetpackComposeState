package com.wangxingxing.jetpackcomposestate.util

import com.wangxingxing.jetpackcomposestate.TodoIcon
import com.wangxingxing.jetpackcomposestate.TodoItem

/**
 * author : 王星星
 * date : 2021/9/17 18:05
 * email : 1099420259@qq.com
 * description :
 */

private val msgs = listOf(
    "Learn compose",
    "Learn state",
    "Build dynamic UIs",
    "Learn Unidirectional Data Flow",
    "Integrate LiveData",
    "Integrate ViewModel",
    "Remember to savedState!",
    "Build stateless composables",
    "Use state from stateless composables"
)

private val icons = TodoIcon.values()

fun generateRandomTodoItem(): TodoItem {
    return TodoItem(msgs.random(), icons.random())
}