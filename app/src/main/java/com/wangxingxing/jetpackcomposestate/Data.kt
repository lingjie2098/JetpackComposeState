package com.wangxingxing.jetpackcomposestate

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import java.util.*

/**
 * author : 王星星
 * date : 2021/9/17 17:13
 * email : 1099420259@qq.com
 * description : 数据类
 */

// 待办事项条目
data class TodoItem(
    val task: String,
    val icon: TodoIcon = TodoIcon.Default,
    val id: UUID = UUID.randomUUID()
)

// 待办事项条目的图标
enum class TodoIcon(
    val imageVector: ImageVector, // 矢量图
    @StringRes val contentDescription: Int // 图标的文字描述
) {
    Square(Icons.Default.CropSquare, R.string.cd_expand),
    Done(Icons.Default.Done, R.string.cd_done),
    Event(Icons.Default.Event, R.string.cd_event),
    Privacy(Icons.Default.PrivacyTip, R.string.cd_privacy),
    Trash(Icons.Default.RestoreFromTrash, R.string.cd_restore);

    companion object {
        val Default = Square
    }
}

