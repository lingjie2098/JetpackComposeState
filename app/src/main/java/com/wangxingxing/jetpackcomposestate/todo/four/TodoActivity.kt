package com.wangxingxing.jetpackcomposestate.todo.four

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.wangxingxing.jetpackcomposestate.ui.theme.JetpackComposeStateTheme

class TodoActivity : ComponentActivity() {

    private val TAG = "TodoActivity"

    private val todoViewModel: TodoViewModel by viewModels<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeStateTheme {
                TodoActivityScreen(todoViewModel = todoViewModel)
            }
        }
    }
}

@Composable
private fun TodoActivityScreen(todoViewModel: TodoViewModel) {
    // LingJie's Mark: 涉及ViewModel的状态全部提升到这里。
    TodoScreen(
        items = todoViewModel.todoItems,
        currentEditing = todoViewModel.currentEditItem,
        onAddItem = todoViewModel::addItem,
        onRemoveItem = todoViewModel::removeItem,
        onStartEdit = todoViewModel::onEditItemSelected,
        onEditItemChange = todoViewModel::onEditItemChange,
        onEditDone = todoViewModel::onEditDone
    )
}