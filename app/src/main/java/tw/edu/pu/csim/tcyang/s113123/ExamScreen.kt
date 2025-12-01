package tw.edu.pu.csim.tcyang.s113123

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun ExamScreen() {
    val viewModel: ExamViewModel = viewModel()
    val density = LocalDensity.current

    val imageSizePx = 300f
    val imageSizeDp: Dp = with(density) { imageSizePx.toDp() }
    val centerImageSize = 200.dp
    val offsetX = (centerImageSize / 2) + (imageSizeDp / 2)

    // 服務圖示的狀態
    var serviceImageId by remember { mutableStateOf(R.drawable.service0) }
    val serviceImageSize = 150.dp
    val serviceImageSizePx = with(density) { serviceImageSize.toPx() }
    var serviceOffset by remember { mutableStateOf(Offset(0f, 0f)) }

    // *** 新增一個狀態來觸發動畫重啟 ***
    var restartAnimation by remember { mutableStateOf(false) }


    // 動畫效果：讓服務圖示掉落
    // *** 修改 LaunchedEffect 的 key ***
    LaunchedEffect(key1 = viewModel.screenWidth, key2 = restartAnimation) {
        if (viewModel.screenWidth > 0) {
            // 初始化位置在螢幕頂部中央
            serviceOffset = Offset(
                x = (viewModel.screenWidth / 2) - (serviceImageSizePx / 2),
                y = 0f
            )

            // 每 0.1 秒往下掉 20px
            while (serviceOffset.y < viewModel.screenHeight - serviceImageSizePx) {
                delay(100) // 0.1秒
                serviceOffset = serviceOffset.copy(y = serviceOffset.y + 20)
            }

            // 碰撞到底部後，隨機更換一個新的服務圖示
            serviceImageId = when (Random.nextInt(4)) {
                0 -> R.drawable.service0
                1 -> R.drawable.service1
                2 -> R.drawable.service2
                else -> R.drawable.service3
            }

            // *** 短暫延遲，讓使用者能看到圖示更換 ***
            delay(50) // 延遲 50 毫秒

            // *** 透過改變 restartAnimation 的值來觸發下一次動畫 ***
            restartAnimation = !restartAnimation
        }
    }


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

        // 5. 新增的掉落服務圖示
        Image(
            painter = painterResource(id = serviceImageId),
            contentDescription = "服務圖示",
            modifier = Modifier
                .offset { IntOffset(serviceOffset.x.roundToInt(), serviceOffset.y.roundToInt()) }
                .size(serviceImageSize)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val newX = (serviceOffset.x + dragAmount.x)
                            .coerceIn(0f, viewModel.screenWidth - serviceImageSizePx)
                        serviceOffset = serviceOffset.copy(x = newX)
                    }
                }
        )
    }
}