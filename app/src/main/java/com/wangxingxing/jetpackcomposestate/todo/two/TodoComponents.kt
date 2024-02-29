package com.wangxingxing.jetpackcomposestate.todo.two

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wangxingxing.jetpackcomposestate.TodoIcon
import com.wangxingxing.jetpackcomposestate.TodoItem
import com.wangxingxing.jetpackcomposestate.ui.theme.JetpackComposeStateTheme

/**
 * author : 王星星
 * date : 2021/9/17 16:49
 * email : 1099420259@qq.com
 * description :
 */

private const val TAG = "TodoComponents"

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
        // LingJie's Mark: 配置软键盘右下角按键为【完成】状态
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        // LingJie's Mark: 点击【完成】后，执行业务逻辑并且隐藏软键盘
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
                keyboardController?.hide()
            }
        )
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
    // icon 是当前选中的图标
    val (icon, setIcon) = remember { mutableStateOf(TodoIcon.Default) }
    // IconRow是否可见取决于文本框中是否有文本
    val iconVisible = text.isNotBlank()
    // 点击“Add”按钮，提交要做的事情
    val submit = {
        onItemComplete(TodoItem(text, icon))
        setIcon(TodoIcon.Default)
        setText("")
    }
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
                    .padding(end = 8.dp),
                onImeAction = submit
            )

            TodoEditButton(
                onClick = submit,
                text = "Add",
                modifier = Modifier.align(Alignment.CenterVertically),
                enabled = text.isNotBlank() // 输入框中有文字，按钮可用
            )
        }

        if (iconVisible) {
            AnimatedIconRow(
                icon = icon,
                onIconChange = setIcon,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedIconRow(
    icon: TodoIcon,
    onIconChange: (TodoIcon) -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = true
) {
    // 动画
    val enter = remember { fadeIn(animationSpec = TweenSpec(300, easing = FastOutLinearInEasing)) }
    val exit = remember { fadeOut(animationSpec = TweenSpec(100, easing = FastOutSlowInEasing)) }
    Box(modifier.defaultMinSize(minHeight = 16.dp)) {
        AnimatedVisibility(
            visible = visible,
            enter = enter,
            exit = exit
        ) {
            IconRow(
                icon = icon,
                onIconChange = onIconChange
            )
        }
    }
}

@Composable
fun IconRow(
    icon: TodoIcon,
    onIconChange: (TodoIcon) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        for (todoIcon in TodoIcon.values()) {
            SelectableIconButton(
                icon = todoIcon.imageVector,
                iconContentDescription = todoIcon.contentDescription,
                onIconSelected = { onIconChange(todoIcon) },
                isSelected = (todoIcon == icon) // icon 选中的icon（state）
            )
        }
    }
}

@Composable
private fun SelectableIconButton(
    icon: ImageVector,
    @StringRes iconContentDescription: Int,
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
        onClick = { onIconSelected },
        shape = CircleShape,
        modifier = Modifier
    ) {
        Column {
            Icon(
                imageVector = icon,
                tint = tint,
                contentDescription = stringResource(id = iconContentDescription)
            )
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

@Preview
@Composable
fun TodoItemInputPreview() {
    JetpackComposeStateTheme {
        TodoItemInput {
            Log.d(TAG, "TodoItemInputPreview: item:$it")
        }
    }
}