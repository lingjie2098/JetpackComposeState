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

@Composable
fun TodoInputText(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // LingJie's Mark: TextField等效EditText
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
    // LingJie's Mark: Button用于明显的操作；TextButton用于不明显（less-pronounced）的操作
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
    // val (value, setValue) = remember { mutableStateOf(default) }，这个函数使用 remember 给自己添加内存，然后在内存中存储一个由 mutableStateOf 创建的 MutableState<String>。
    // LingJie's Mark: (text, setText)语法糖参见Kotlin解构声明。
    // LingJie's Mark: 下面代码的等效做法见①②③
    val (text, setText) =
        // LingJie's Mark: 使用 remember API 将对象存储在内存中。remember 既可用于存储可变对象，又可用于存储不可变对象。
        remember {
            // LingJie's Mark: mutableStateOf 会创建可观察的 MutableState<T>，后者是与 Compose 运行时集成的可观察类型。即如果 value 有任何变化，系统就会为读取 value 的所有可组合函数安排重组。
            mutableStateOf("")
        }
    // ①
//    var text by remember { mutableStateOf("") }
    Column {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            TodoInputText(
                text = text,
                // ②
//                onTextChange = { text = it },
                onTextChange = setText,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            TodoEditButton(
                onClick = {
                    onItemComplete(TodoItem(text))
                    // ③
//                    text = ""
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