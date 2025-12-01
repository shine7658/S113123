package tw.edu.pu.csim.tcyang.s113123

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tw.edu.pu.csim.tcyang.s113123.ui.theme.S113123Theme
// 1. 匯入必要的類別
import android.content.pm.ActivityInfo
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 2. 強制設定螢幕為直式
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // 3. 隱藏狀態列和導覽列
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // 設定系統 UI 行為為沉浸式，並在使用者滑動時自動隱藏
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // 隱藏狀態列和導覽列
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        enableEdgeToEdge() // 註：您的專案已啟用此功能，它有助於全螢幕顯示
        setContent {
            S113123Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    S113123Theme {
        Greeting("Android")
    }
}
