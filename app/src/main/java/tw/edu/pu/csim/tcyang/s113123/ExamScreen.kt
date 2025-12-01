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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
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

    // 各個圖示的大小設定
    val roleImageSizeDp: Dp = with(density) { 300f.toDp() }
    val serviceImageSizeDp = 150.dp
    val serviceImageSizePx = with(density) { serviceImageSizeDp.toPx() }

    // 用來儲存四個角落角色圖示的邊界 (Rect)
    var roleRects by remember { mutableStateOf<Map<Int, Rect>>(emptyMap()) }

    // 服務圖示的狀態
    var serviceImageId by remember { mutableStateOf(R.drawable.service0) }
    var serviceOffset by remember { mutableStateOf(Offset(0f, 0f)) }
    var restartAnimation by remember { mutableStateOf(false) }

    // 動畫與碰撞邏輯
    LaunchedEffect(key1 = viewModel.screenWidth, key2 = restartAnimation) {
        if (viewModel.screenWidth > 0 && viewModel.screenHeight > 0) {
            // 重設訊息並初始化位置
            viewModel.clearMessage()
            serviceOffset = Offset(
                x = (viewModel.screenWidth / 2) - (serviceImageSizePx / 2),
                y = 0f
            )

            var collisionDetected = false
            while (serviceOffset.y < viewModel.screenHeight && !collisionDetected) {
                delay(20) // 為了更流暢的偵測，縮短延遲時間
                serviceOffset = serviceOffset.copy(y = serviceOffset.y + 10) // 調整每次掉落的距離

                val serviceRect = Rect(serviceOffset, serviceOffset + Offset(serviceImageSizePx, serviceImageSizePx))

                // 檢查與四個角色圖示的碰撞
                for ((roleId, roleRect) in roleRects) {
                    if (viewModel.checkCollision(serviceRect, roleRect)) {
                        val (points, msg) = when (roleId) {
                            R.drawable.role0 -> Pair(10, "碰撞嬰幼兒圖示")
                            R.drawable.role1 -> Pair(10, "碰撞兒童圖示")
                            R.drawable.role2 -> Pair(5, "碰撞成人圖示")
                            else -> Pair(2, "碰撞一般民眾圖示")
                        }

                        // ----- 修改開始 -----
                        // 雖然取得了 points，但在這裡不使用它，只傳入目前的分數
                        // 這樣分數就不會增加，但訊息會正常更新
                        viewModel.updateScore(viewModel.score, msg)
                        // ----- 修改結束 -----

                        collisionDetected = true
                        break // 發生碰撞後跳出 for 迴圈
                    }
                }
            }

            // 如果迴圈結束是因為掉到最下方（且未發生碰撞）
            if (!collisionDetected) {
                viewModel.updateScore(viewModel.score, "掉到最下方")
            }

            // 隨機更換一個新的服務圖示並準備重啟動畫
            serviceImageId = when (Random.nextInt(4)) {
                0 -> R.drawable.service0
                1 -> R.drawable.service1
                2 -> R.drawable.service2
                else -> R.drawable.service3
            }
            delay(500) // 讓使用者有時間看到訊息
            restartAnimation = !restartAnimation
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { size ->
                viewModel.updateScreenSize(size.width.toFloat(), size.height.toFloat())
            }
            .background(Color.Yellow)
    ) {
        // 中間資訊區塊
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.happy), contentDescription = null, modifier = Modifier.size(200.dp).clip(CircleShape))
            Text("瑪利亞基金會服務大考驗", fontSize = 20.sp)
            Text("作者：${viewModel.author}", fontSize = 20.sp)
            Text("螢幕大小：${viewModel.screenWidth.toInt()} * ${viewModel.screenHeight.toInt()}", fontSize = 20.sp)
            // *** 將成績 Text 與 ViewModel 綁定 ***
            Text("成績：${viewModel.score}分 (${viewModel.message})", fontSize = 20.sp)
        }

        // 四個角落的角色圖示
        val roles = listOf(
            Triple(R.drawable.role0, Alignment.CenterStart, "嬰幼兒圖示"),
            Triple(R.drawable.role1, Alignment.CenterEnd, "兒童圖示"),
            Triple(R.drawable.role2, Alignment.BottomStart, "成人圖示"),
            Triple(R.drawable.role3, Alignment.BottomEnd, "一般民眾圖示")
        )

        roles.forEach { (id, alignment, description) ->
            Image(
                painter = painterResource(id = id),
                contentDescription = description,
                modifier = Modifier
                    .align(alignment)
                    .size(roleImageSizeDp)
                    // *** 取得每個角色圖示的位置和大小，並存儲其邊界 ***
                    .onGloballyPositioned { layoutCoordinates ->
                        val position = layoutCoordinates.positionInRoot()
                        val size = layoutCoordinates.size
                        roleRects = roleRects + (id to Rect(position, Offset(position.x + size.width, position.y + size.height)))
                    }
            )
        }

        // 可拖曳的掉落服務圖示
        if (viewModel.screenWidth > 0) { // 確保在取得螢幕寬度後才顯示
            Image(
                painter = painterResource(id = serviceImageId),
                contentDescription = "服務圖示",
                modifier = Modifier
                    .offset { IntOffset(serviceOffset.x.roundToInt(), serviceOffset.y.roundToInt()) }
                    .size(serviceImageSizeDp)
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
}
