package tw.edu.pu.csim.tcyang.s113123

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ExamScreen() {
    // 1. 取得 ViewModel 實例
    val viewModel: ExamViewModel = viewModel()
    val density = LocalDensity.current

    // 2. 主容器，負責填滿整個螢幕並偵測尺寸
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { size ->
                // 將 px 轉換為 dp
                val widthPx = size.width.toFloat()
                val heightPx = size.height.toFloat()
                viewModel.updateScreenSize(widthPx, heightPx)
            }
            .background(Color.Yellow), // 3. 設定黃色背景
        contentAlignment = Alignment.Center // 4. 讓內容物（Column）水平垂直置中
    ) {
        // 5. 垂直排列的容器
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 圓形圖片
            Image(
                painter = painterResource(id = R.drawable.happy), // 請確認您的圖片資源名稱
                contentDescription = "服務圖片",
                modifier = Modifier
                    .size(200.dp) // 設定圖片大小
                    .clip(CircleShape), // 裁切成圓形
                contentScale = ContentScale.Crop // 縮放模式
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 瑪利亞基金會服務大考驗
            Text(text = "瑪利亞基金會服務大考驗", fontSize = 20.sp)

            Spacer(modifier = Modifier.height(10.dp)) // 在此處加入間距

            // 作者資訊 (從 ViewModel 取得)
            Text(text = "作者：${viewModel.author}", fontSize = 20.sp)

            Spacer(modifier = Modifier.height(10.dp)) // 在此處加入間距

            // 螢幕大小 (從 ViewModel 取得)
            Text(
                text = "螢幕大小：${viewModel.screenWidth} * ${viewModel.screenHeight}",
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(10.dp)) // 在此處加入間距

            // 成績
            Text(text = "成績：0分", fontSize = 20.sp)
        }
    }
}
