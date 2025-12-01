package tw.edu.pu.csim.tcyang.s113123

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel

class ExamViewModel : ViewModel() {
    // 作者資訊
    var author by mutableStateOf("資管二B 林哲旭")
        private set

    // 螢幕寬高
    var screenWidth by mutableStateOf(0f)
        private set
    var screenHeight by mutableStateOf(0f)
        private set

    // *** 新增分數和訊息屬性 ***
    var score by mutableStateOf(0)
        private set
    var message by mutableStateOf("")
        private set

    // 更新螢幕尺寸的方法
    fun updateScreenSize(width: Float, height: Float) {
        screenWidth = width
        screenHeight = height
    }

    // *** 新增碰撞檢測及更新分數的邏輯 ***

    // 檢查服務圖示 (rect1) 是否與角色圖示 (rect2) 重疊
    fun checkCollision(rect1: Rect, rect2: Rect): Boolean {
        return rect1.overlaps(rect2)
    }

    // 更新分數和訊息
    fun updateScore(newScore: Int, newMessage: String) {
        score = newScore
        message = newMessage
    }

    // 重設訊息
    fun clearMessage() {
        message = ""
    }
}
