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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ExamScreen() {
    val viewModel: ExamViewModel = viewModel()
    val density = LocalDensity.current

    // 將 300px 轉換為 Dp
    val imageSizePx = 300f
    val imageSizeDp: Dp = with(density) { imageSizePx.toDp() }
    val centerImageSize = 200.dp

    // 計算偏移量: (中心圖片寬度 / 2) + (角色圖片寬度 / 2)
    val offsetX = (centerImageSize / 2) + (imageSizeDp / 2)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { size ->
                val widthPx = size.width.toFloat()
                val heightPx = size.height.toFloat()
                viewModel.updateScreenSize(widthPx, heightPx)
            }
            .background(Color.Yellow),
    ) {
        // 1. 保持中間的資訊區塊不變，讓它完美置中
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // 中間的圓形圖片
                Image(
                    painter = painterResource(id = R.drawable.happy),
                    contentDescription = "服務圖片",
                    modifier = Modifier
                        .size(centerImageSize) // 使用變數
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "瑪利亞基金會服務大考驗", fontSize = 20.sp)
                Text(text = "作者：${viewModel.author}", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "螢幕大小：${viewModel.screenWidth} * ${viewModel.screenHeight}", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "成績：0分", fontSize = 20.sp)
            }
        }

        // 2. 將嬰幼兒圖示先對齊到螢幕正中央，再向左偏移
        Image(
            painter = painterResource(id = R.drawable.role0),
            contentDescription = "嬰幼兒圖示",
            modifier = Modifier
                .align(Alignment.Center) // 先對齊到絕對中心
                .offset(x = -offsetX)    // 再向左偏移
                .size(imageSizeDp)
        )

        // 3. 將兒童圖示也對齊到螢幕正中央，再向右偏移
        Image(
            painter = painterResource(id = R.drawable.role1),
            contentDescription = "兒童圖示",
            modifier = Modifier
                .align(Alignment.Center) // 先對齊到絕對中心
                .offset(x = offsetX)     // 再向右偏移
                .size(imageSizeDp)
        )

        // 4. 左下與右下的圖示保持不變
        Image(
            painter = painterResource(id = R.drawable.role2),
            contentDescription = "成人圖示",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(imageSizeDp)
        )

        Image(
            painter = painterResource(id = R.drawable.role3),
            contentDescription = "一般民眾圖示",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(imageSizeDp)
        )
    }
}
