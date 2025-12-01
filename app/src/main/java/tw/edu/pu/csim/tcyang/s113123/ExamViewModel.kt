package tw.edu.pu.csim.tcyang.s113123

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ExamViewModel : ViewModel() {
    // 使用 mutableStateOf 來建立可觀察的狀態
    // 這樣當值改變時，Compose UI 會自動更新

    // 作者資訊，請修改成您自己的系級與姓名
    var author by mutableStateOf("資管二B 林哲旭")
        private set // private set 表示只能在 ViewModel 內部修改

    // 螢幕寬度，初始值為 0f
    var screenWidth by mutableStateOf(0f)
        private set

    // 螢幕高度，初始值為 0f
    var screenHeight by mutableStateOf(0f)
        private set

    // 更新螢幕尺寸的方法
    fun updateScreenSize(width: Float, height: Float) {
        screenWidth = width
        screenHeight = height
    }
}
