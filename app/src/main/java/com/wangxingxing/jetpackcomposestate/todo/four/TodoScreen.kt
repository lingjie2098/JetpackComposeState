package com.wangxingxing.jetpackcomposestate.todo.four

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
    currentEditing: TodoItem?,
    onAddItem: (TodoItem) -> Unit,
    onRemoveItem: (TodoItem) -> Unit,
    onStartEdit: (TodoItem) -> Unit,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit
) {
    Column {
        // 当currentlyEditing（当前编辑条目）为空时，显示添加输入框
        // 否则进入编辑状态时，最顶部会显示 “Editing item”文本
        val enableTopSelection = currentEditing == null
        TodoItemInputBackground(
            elevate = true
        ) {
            // Add 添加模式
            if (enableTopSelection) {
                TodoItemEntryInput(onAddItem)
            } else {
                // 编辑模式
                Text(
                    text = "Editing item",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }

        // 多行待办事项
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(items = items) { todo ->
                // 当前条目被选中，进入编辑状态
                if (currentEditing?.id == todo.id) {
                    TodoItemInlineEditor(
                        item = currentEditing,
                        onEditItemChange = onEditItemChange,
                        onEditDone = onEditDone,
                        onRemoveItem = { onRemoveItem(todo) }
                    )
                } else {
                    TodoRow(
                        todo = todo,
                        onItemClicked = { onStartEdit(it) },
                        modifier = Modifier.fillParentMaxWidth()
                    )
                }
            }
        }

        Button(
            onClick = { onAddItem(generateRandomTodoItem()) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Add random item")
        }
    }
}

private fun randomTint(): Float {
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
        // 随机透明度
        // 但是每一次recomposition（重组）时，会重新生成透明度值
        // val iconAlpha: Float = randomTint()
        // 1.remember 给了组合函数一个内存空间
        // 2.remember中表达式计算出来的值，将会保存在组合树中（composition tree），
        // 并且只有在remember的键发生改变时，表达式才会重新计算。
        // 3.我们可以将remember视为给函数提供了一个存储对象的内存空间，就像私有属性在对象中所做的那样。
        val iconAlpha: Float = remember(todo.id) { randomTint() }
        Icon(
            imageVector = todo.icon.imageVector,
            tint = LocalContentColor.current.copy(alpha = iconAlpha),
            contentDescription = stringResource(id = todo.icon.contentDescription)
        )

        //LocalContentColor
        //CompositionLocal 包含层次结构中给定位置的首选内容颜色。
        //这种颜色应该用于任何排版/图标，以确保这些颜色在背景颜色改变时进行调整。
        //例如，在深色背景上，文字应该是浅色，而在浅色背景上，文字应该是深色。
    }
}
