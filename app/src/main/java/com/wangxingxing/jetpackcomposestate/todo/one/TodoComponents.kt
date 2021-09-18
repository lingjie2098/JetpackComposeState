package com.wangxingxing.jetpackcomposestate.todo.one

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wangxingxing.jetpackcomposestate.TodoItem
import com.wangxingxing.jetpackcomposestate.ui.theme.JetpackComposeStateTheme

/**
 * author : 王星星
 * date : 2021/9/17 16:49
 * email : 1099420259@qq.com
 * description :
 */

private const val TAG = "TodoComponents"

//输入框
@Composable
fun TodoInputText(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        maxLines = 1,
        modifier = modifier
    )
}

@Composable
fun TodoEditButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(),
        modifier = modifier,
        enabled = enabled
    ) {
        Text(text)
    }
}

@Composable
fun TodoItemInput(onItemComplete: (TodoItem) -> Unit) {
    // 这个函数使用 remember 给自己添加内存，然后在内存中存储一个由 mutableStateOf 创建的 MutableState<String>，
    // 它是 Compose 的内置类型，提供了一个可观察的状态持有者。
    // val (value, setValue) = remember { mutableStateOf(default) }
    // 对 value 的任何更改都将自动重新组合读取此状态的任何可组合函数。
    val (text, setText) = remember { mutableStateOf("") }
    Column {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            TodoInputText(
                text = text,
                onTextChange = setText,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            TodoEditButton(
                onClick = {
                    onItemComplete(TodoItem(text))
                    setText("")
                },
                text = "Add",
                modifier = Modifier.align(Alignment.CenterVertically),
                enabled = text.isNotBlank() // 输入框中有文字，按钮可用
            )
        }
    }
}

@Preview
@Composable
fun TodoItemInputPreview() {
    JetpackComposeStateTheme {
        TodoItemInput {
            Log.d(TAG, "TodoItemInputPreview: item:$it")
        }
    }
}