package com.wangxingxing.jetpackcomposestate.todo.four

import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wangxingxing.jetpackcomposestate.TodoIcon
import com.wangxingxing.jetpackcomposestate.TodoItem
import com.wangxingxing.jetpackcomposestate.ui.theme.JetpackComposeStateTheme

/**
 * 当TodoItem列表中的条目被选中时，会弹出一个输入框，用于编辑选中TodoItem的信息
 * @param item 选中的TodoItem条目
 * @param onEditItemChange 编辑条目时的回调
 * @param onEditDone 编辑完成时的回调
 * @param onRemoveItem 删除条目时的回调
 */
@Composable
fun TodoItemInlineEditor(
    // LingJie's Mark: 状态提升
    item: TodoItem,
    onEditItemChange: (TodoItem) -> Unit,

    onEditDone: () -> Unit,
    onRemoveItem: () -> Unit
) {
    TodoItemInput(
        text = item.task,
        onTextChange = { onEditItemChange(item.copy(task = it)) },
        selectedIcon = item.icon,
        onSelectedIconChange = { onEditItemChange(item.copy(icon = it)) },
    ) {
        // 保存和删除两个图标
        Row {
            val shrinkButtons = Modifier.widthIn(20.dp)

            TextButton(
                onClick = onEditDone,
                modifier = shrinkButtons
            ) {
                Text(
                    text = "\uD83D\uDCBE",  // LingJie's Mark: 软盘（Emoji表情符号）
                    textAlign = TextAlign.End,
                    modifier = Modifier.widthIn(30.dp)
                )
            }

            TextButton(
                onClick = onRemoveItem,
                modifier = shrinkButtons
            ) {
                Text(
                    text = "❌",
                    textAlign = TextAlign.End,
                    modifier = Modifier.widthIn(30.dp)
                )
            }
        }
    }
}

// 顶部输入框加上一个灰色背景
@Composable
fun TodoItemInputBackground(
    elevate: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    // 帧动画的形式展现Surface底部的阴影
    val animatedEvevation by animateDpAsState(if (elevate) 1.dp else 0.dp, TweenSpec(500))
    Surface(
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
        shape = RectangleShape,
        // Surface 底部有一个小小的阴影
        elevation = animatedEvevation
    ) {
        Row(
            modifier = modifier.animateContentSize(animationSpec = TweenSpec(300)),
            content = content
        )
    }
}


/**
 * 文本输入框
 * @param text 状态对象
 * @param onTextChange 文本输入框中文本内容发生改变之后的回调函数
 * @param modifier 修饰符
 * @param onImeAction 软键盘中的“完成”点击之后，要执行的回调函数
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TodoInputText(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onImeAction: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = text,
        onValueChange = onTextChange,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        maxLines = 1,
        modifier = modifier,
        // 配置软键盘
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
            //点击完成之后，隐藏键盘
            keyboardController?.hide()
        })
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

// 添加模式的输入框
@Composable
fun TodoItemEntryInput(onItemComplete: (TodoItem) -> Unit) {
    // 这个函数使用 remember 给自己添加内存，然后在内存中存储一个由 mutableStateOf 创建的 MutableState<String>，
    // 它是 Compose 的内置类型，提供了一个可观察的状态持有者。
    // val (value, setValue) = remember { mutableStateOf(default) }
    // 对 value 的任何更改都将自动重新组合读取此状态的任何可组合函数。
    val (text, setText) = remember { mutableStateOf("") }
    // icon 是当前选中的图标
    val (icon, setIcon) = remember { mutableStateOf(TodoIcon.Default) }
    // IconRow是否可见取决于文本框中是否有文本
    val animIconRowVisible = text.isNotBlank()
    // 点击“Add”按钮，提交要做的事情
    val submit = {
        onItemComplete(TodoItem(text, icon))
        setIcon(TodoIcon.Default)
        setText("")
    }

    TodoItemInput(
        text = text,
        onTextChange = setText,
        selectedIcon = icon,
        onSelectedIconChange = setIcon,
        animIconRowVisible = animIconRowVisible
    ) {
        TodoEditButton(
            onClick = submit,
            text = "Add",
            enabled = text.isNotBlank()  // 输入框中有文字，按钮可用
        )
    }
}

/**
 * @param endContent: 最右侧的控件。比如：编辑时有保存和删除两个按钮；添加时有添加按钮
 */
@Composable
fun TodoItemInput(
    text: String,
    onTextChange: (String) -> Unit,

    // LingJie's Mark: 状态提升
    selectedIcon: TodoIcon,
    onSelectedIconChange: (TodoIcon) -> Unit,

    animIconRowVisible: Boolean = true,
    endContent: @Composable () -> Unit
) {

    Column {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            TodoInputText(
                text = text,
                onTextChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))
            Box(Modifier.align(Alignment.CenterVertically)) {
                endContent()
            }

        }

        if (animIconRowVisible) {
            AnimatedIconRow(
                selectedIcon = selectedIcon,
                onSelectedIconChange = onSelectedIconChange,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * 一排图标，根据文本框是否有内容，自动收起和弹出，带动画效果
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedIconRow(
    // LingJie's Mark: 状态提升
    selectedIcon: TodoIcon,
    onSelectedIconChange: (TodoIcon) -> Unit,

    modifier: Modifier = Modifier,
    visible: Boolean = true
) {
    // LingJie's Mark: 淡入淡出
    val enter = remember { fadeIn(animationSpec = TweenSpec(300, easing = FastOutLinearInEasing)) }
    val exit = remember { fadeOut(animationSpec = TweenSpec(100, easing = FastOutSlowInEasing)) }
    Box(modifier.defaultMinSize(minHeight = 16.dp)) {
        // LingJie's Mark: 显示隐藏时的动画
        AnimatedVisibility(
            visible = visible,
            enter = enter,
            exit = exit
        ) {
            IconRow(
                selectedIcon = selectedIcon,
                onSelectedIconChange = onSelectedIconChange
            )
        }
    }
}

@Composable
fun IconRow(
    // LingJie's Mark: 状态提升
    selectedIcon: TodoIcon,
    onSelectedIconChange: (TodoIcon) -> Unit,

    modifier: Modifier = Modifier
) {
    Row(modifier) {
        for (todoIcon in TodoIcon.values()) {
            SelectableIconButton(
                icon = todoIcon.imageVector,
                iconContentDescription = todoIcon.contentDescription,
                onIconSelected = { onSelectedIconChange(todoIcon) },
                isSelected = (todoIcon == selectedIcon),  // icon 选中的icon（state）
            )
        }
    }
}

/**
 * 图标的选中影响①②③和TodoViewModel的状态，所以状态提升到TodoActivityScreen()函数。
 */
@Composable
private fun SelectableIconButton(
    icon: ImageVector,
    @StringRes iconContentDescription: Int,

    // LingJie's Mark: 状态提升
    onIconSelected: () -> Unit,
    isSelected: Boolean,

    modifier: Modifier = Modifier
) {
    val tint = if (isSelected) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
    }
    TextButton(
        onClick = { onIconSelected() }, // ①
        shape = CircleShape,
        modifier = Modifier
    ) {
        Column {
            Icon(
                imageVector = icon,
                tint = tint,    // ②
                contentDescription = stringResource(id = iconContentDescription)
            )
            // ③
            if (isSelected) {
                Box(
                    modifier
                        .padding(top = 3.dp)
                        .width(icon.defaultWidth)
                        .height(1.dp)
                        .background(tint)
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}


/*@Preview
@Composable
fun TodoItemInputPreview() {
    JetpackComposeStateTheme {
        TodoItemInput { item ->
            Log.d("ning", "item:$item")
        }
    }
}*/

@Preview
@Composable
fun AnimatedIconRowPreview() {
    JetpackComposeStateTheme {
        AnimatedIconRow(TodoIcon.Default, {})
    }
}
