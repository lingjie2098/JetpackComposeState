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
    // LingJie's Mark: observeAsState：开始观察todoItems，并通过State表示它的值。每次有新的值提交到LiveData时，State将被更新，引起每个State.value的调用重组（即每次都会重新调用TodoActivityScreen(...)）。使用①处代码也能实现同等效果。为何使用observeAsState，解释在：https://developer.android.google.cn/jetpack/compose/state?hl=zh-cn中的【其他受支持的状态类型】
    val items: List<TodoItem> by todoViewModel.todoItems.observeAsState(listOf())
    // ①
    /*val items = todoViewModel.todoItems.value ?: listOf()
    val state = todoViewModel.todoItems.observeAsState(listOf())
    Timber.tag("asdfsasadf").e("${state.value}")*/
    TodoScreen(
        // LingJie's Mark: 是否发现，ViewModel中的状态和事件都放在了这里，不影响TodoScreen()函数里面的逻辑！！！
        items = items,
        onAddItem = { todoViewModel.addItem(it) },
        onRemoveItem = { todoViewModel.removeItem(it) }
    )
}