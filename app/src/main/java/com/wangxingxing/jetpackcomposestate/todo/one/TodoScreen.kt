package com.wangxingxing.jetpackcomposestate.todo.one

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wangxingxing.jetpackcomposestate.TodoIcon
import com.wangxingxing.jetpackcomposestate.TodoItem
import com.wangxingxing.jetpackcomposestate.util.generateRandomTodoItem
import kotlin.random.Random

/**
 * author : 王星星
 * date : 2021/9/17 17:40
 * email : 1099420259@qq.com
 * description :
 */

@Composable
fun TodoScreen(
    items: List<TodoItem>,
    onAddItem: (TodoItem) -> Unit,
    onRemoveItem: (TodoItem) -> Unit
) {
    Column {
        // 多行待办事项
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(items = items) {
                // LingJie's Mark: FIXME: 每次items变化，所有的TodoRow(...)都执行了一遍。
                TodoRow(
                    todo = it,
                    onItemClicked = { onRemoveItem(it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Button(onClick = { onAddItem(generateRandomTodoItem()) },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
        ) {
            Text(text = "Add random item")
        }
    }
}

private fun randomTint(): Float {
    // LingJie's Mark: 返回0.3~0.9之间的Float
    return Random.nextFloat().coerceIn(0.3f, 0.9f)
}

// 一个待办事项条目
@Composable
fun TodoRow(
    todo: TodoItem,
    onItemClicked: (TodoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onItemClicked(todo) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween // 子元素水平均匀分布
    ) {
        Text(text = todo.task)
        // LingJie's Mark:
        // 1、使用rememberSaveable代替remember解决列表超过一页上下滑动列表randomTint()会重新执行的问题。
        // 2、key可以不加，key的作用参见https://blog.csdn.net/qq_39312146/article/details/130738223
        val iconAlpha: Float = remember(todo.id) { randomTint() }
        Icon(
            imageVector = todo.icon.imageVector,
            // LingJie's Mark: 图标透明度。LocalContentColor隐式传参，会在后面CompositionLocal课程中讲。
            tint = LocalContentColor.current.copy(alpha = iconAlpha),
            contentDescription = stringResource(id = todo.icon.contentDescription)
        )

        //LocalContentColor
        //CompositionLocal 包含层次结构中给定位置的首选内容颜色。
        //这种颜色应该用于任何排版/图标，以确保这些颜色在背景颜色改变时进行调整。
        //例如，在深色背景上，文字应该是浅色，而在浅色背景上，文字应该是深色。
    }
}

@Preview
@Composable
fun PreviewTodoScreen() {
    val items = listOf(
        TodoItem("Learn compose", TodoIcon.Event),
        TodoItem("Take the codelab"),
        TodoItem("Apply state", TodoIcon.Done),
        TodoItem("Build dynamic UIs", TodoIcon.Square)
    )
    TodoScreen(items = items, {}, {})
}