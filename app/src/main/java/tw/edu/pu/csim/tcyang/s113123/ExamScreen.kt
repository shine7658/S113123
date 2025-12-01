package tw.edu.pu.csim.tcyang.s113123

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current


    val serviceAnswers = mapOf(
        R.drawable.service0 to R.drawable.role0, // 極早期療育 -> 嬰幼兒
        R.drawable.service1 to R.drawable.role1, // 離島服務 -> 兒童
        R.drawable.service2 to R.drawable.role2, // 極重多障 -> 成人
        R.drawable.service3 to R.drawable.role3  // 輔具服務 -> 一般民眾
    )
    val answerMessages = mapOf(
        R.drawable.service0 to "極早期療育，屬於嬰幼兒方面的服務",
        R.drawable.service1 to "離島服務，屬於兒童方面的服務",
        R.drawable.service2 to "極重多障，屬於成人方面的服務",
        R.drawable.service3 to "輔具服務，屬於一般民眾方面的服務"
    )



    val roleSizePx = 300f
    val roleImageSizeDp: Dp = with(density) { roleSizePx.toDp() }


    val serviceImageSizeDp = 150.dp
    val serviceImageSizePx = with(density) { serviceImageSizeDp.toPx() }


    var roleRects by remember { mutableStateOf<Map<Int, Rect>>(emptyMap()) }


    var serviceImageId by remember { mutableStateOf(R.drawable.service0) }
    var serviceOffset by remember { mutableStateOf(Offset(0f, 0f)) }
    var restartAnimation by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = viewModel.screenWidth, key2 = restartAnimation) {
        if (viewModel.screenWidth > 0 && viewModel.screenHeight > 0) {
            viewModel.clearMessage()
            serviceOffset = Offset(
                x = (viewModel.screenWidth / 2) - (serviceImageSizePx / 2),
                y = 0f
            )

            var collisionDetected = false
            while (serviceOffset.y < viewModel.screenHeight && !collisionDetected) {
                delay(20)
                serviceOffset = serviceOffset.copy(y = serviceOffset.y + 10)

                val serviceRect = Rect(serviceOffset, serviceOffset + Offset(serviceImageSizePx, serviceImageSizePx))

                for ((collidedRoleId, roleRect) in roleRects) {
                    if (viewModel.checkCollision(serviceRect, roleRect)) {
                        val correctAnswerId = serviceAnswers[serviceImageId]
                        val isCorrect = (correctAnswerId == collidedRoleId)

                        viewModel.updateScore(isCorrect)

                        val toastMessage = answerMessages[serviceImageId] ?: "沒有找到答案說明"
                        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()

                        collisionDetected = true
                        break
                    }
                }
            }

            if (!collisionDetected) {
                viewModel.updateMessage("掉到最下方")
            }

            delay(3000)

            var nextServiceId: Int
            do {
                nextServiceId = when (Random.nextInt(4)) {
                    0 -> R.drawable.service0
                    1 -> R.drawable.service1
                    2 -> R.drawable.service2
                    else -> R.drawable.service3
                }
            } while (nextServiceId == serviceImageId)
            serviceImageId = nextServiceId

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

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.happy),
                contentDescription = null,
                modifier = Modifier.size(200.dp).clip(CircleShape)
            )
            Text("瑪利亞基金會服務大考驗", fontSize = 20.sp)

            Text("作者：${viewModel.author}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(10.dp)) // 間隔 10dp

            Text("螢幕大小：${viewModel.screenWidth.toInt()} * ${viewModel.screenHeight.toInt()}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(10.dp)) // 間隔 10dp

            Text("成績：${viewModel.score}分 (${viewModel.message})", fontSize = 20.sp)
        }


        val rolesData = listOf(
            R.drawable.role0 to "嬰幼兒圖示",
            R.drawable.role1 to "兒童圖示",
            R.drawable.role2 to "成人圖示",
            R.drawable.role3 to "一般民眾圖示"
        )

        rolesData.forEach { (id, description) ->
            val modifier = when (id) {

                R.drawable.role0 -> {
                    Modifier
                        .align(Alignment.TopStart)
                        .offset { IntOffset(0, ((viewModel.screenHeight / 2) - roleSizePx).toInt()) }
                }


                R.drawable.role1 -> {
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset { IntOffset(0, ((viewModel.screenHeight / 2) - roleSizePx).toInt()) }
                }


                R.drawable.role2 -> {
                    Modifier.align(Alignment.BottomStart)
                }


                else -> {
                    Modifier.align(Alignment.BottomEnd)
                }
            }

            Image(
                painter = painterResource(id = id),
                contentDescription = description,
                modifier = modifier
                    .size(roleImageSizeDp)
                    .onGloballyPositioned { layoutCoordinates ->
                        val position = layoutCoordinates.positionInRoot()
                        val size = layoutCoordinates.size
                        roleRects = roleRects + (id to Rect(position, Offset(position.x + size.width, position.y + size.height)))
                    }
            )
        }

        if (viewModel.screenWidth > 0) {
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