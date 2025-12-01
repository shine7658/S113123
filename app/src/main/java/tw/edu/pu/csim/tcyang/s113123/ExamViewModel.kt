package tw.edu.pu.csim.tcyang.s113123

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel

class ExamViewModel : ViewModel() {
    var author by mutableStateOf("資管二B 林哲旭")
        private set

    var screenWidth by mutableStateOf(0f)
        private set
    var screenHeight by mutableStateOf(0f)
        private set

    var score by mutableStateOf(0)
        private set
    var message by mutableStateOf("")
        private set

    fun updateScreenSize(width: Float, height: Float) {
        screenWidth = width
        screenHeight = height
    }

    fun checkCollision(rect1: Rect, rect2: Rect): Boolean {
        return rect1.overlaps(rect2)
    }

    fun updateScore(isCorrect: Boolean) {
        if (isCorrect) {
            score++
            message = "答對了，加一分"
        } else {
            score--
            message = "答錯了，扣一分"
        }
    }

    fun updateMessage(newMessage: String) {
        message = newMessage
    }

    fun clearMessage() {
        message = ""
    }
}
