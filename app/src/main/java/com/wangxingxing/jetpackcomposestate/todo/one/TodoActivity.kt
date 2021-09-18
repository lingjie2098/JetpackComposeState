package com.wangxingxing.jetpackcomposestate.todo.one

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.wangxingxing.jetpackcomposestate.TodoItem
import com.wangxingxing.jetpackcomposestate.ui.theme.JetpackComposeStateTheme

class TodoActivity : ComponentActivity() {

    private val TAG = "TodoActivity"

    private val todoViewModel: TodoViewModel by viewModels<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeStateTheme {
                TodoActivityScreen(todoViewModel = todoViewModel)
//                TodoItemInput {
//                    Log.d(TAG, "onCreate: item:$it")
//                }
            }
        }
    }
}

@Composable
private fun TodoActivityScreen(todoViewModel: TodoViewModel) {
    /*val items = listOf(
    TodoItem("Learn compose", TodoIcon.Event),
    TodoItem("Take the codelab"),
    TodoItem("Apply state", TodoIcon.Done),
    TodoItem("Build dynamic UIs", TodoIcon.Square)
)*/
    // observeAsState 观察 LiveData 并返回一个 State 对象，该对象在 LiveData 修改时更新。
    // 给items的值由委托对象提供
    // 通过State对LiveData进行观察监听，每一次LiveData的value值发生改变，都会更新State，就会引起组合树重组
    // 界面也就更新了
    val items: List<TodoItem> by todoViewModel.todoItems.observeAsState(listOf())
    TodoScreen(
        items = items,
        onAddItem = { todoViewModel.addItem(it) },
        onRemoveItem = { todoViewModel.removeItem(it) }
    )
}