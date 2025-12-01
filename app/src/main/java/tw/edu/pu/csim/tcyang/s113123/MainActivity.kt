package tw.edu.pu.csim.tcyang.s113123

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
// 1. 移除不再需要的 import
// import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.foundation.layout.padding
// import androidx.compose.material3.Scaffold
// import androidx.compose.material3.Text
// import androidx.compose.runtime.Composable
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.tooling.preview.Preview
import tw.edu.pu.csim.tcyang.s113123.ui.theme.S113123Theme
import android.content.pm.ActivityInfo
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 強制設定螢幕為直式
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // 隱藏狀態列和導覽列
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        // enableEdgeToEdge() // 在全螢幕模式下，可以註解掉或保留

        setContent {
            S113123Theme {
                // 2. 將 Scaffold 和 Greeting 替換成 ExamScreen()
                ExamScreen()
            }
        }
    }
}