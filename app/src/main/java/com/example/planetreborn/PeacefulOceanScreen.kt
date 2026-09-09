package com.example.planetreborn

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.math.min

data class Stone(
    val id: Int,
    val offset: Offset = Offset.Zero,
    val released: Boolean = false,
    val dissolved: Float = 0f
)

@Composable
fun PeacefulOceanScreen(navController: NavController) {

    var level by remember { mutableStateOf(1) }
    val totalStones = 3 + (level - 1) * 2

    var stones by remember(level) {
        mutableStateOf(List(totalStones) { Stone(it) })
    }

    var relaxed by remember { mutableStateOf(0f) }

    LaunchedEffect(level) {
        while (true) {
            delay(32)

            stones = stones.map { stone ->
                if (stone.released && stone.dissolved < 1f) {
                    stone.copy(
                        offset = stone.offset + Offset(0f, 6f + level),
                        dissolved = min(1f, stone.dissolved + 0.01f)
                    )
                } else stone
            }

            relaxed =
                stones.sumOf { it.dissolved.toDouble() }.toFloat() / totalStones
        }
    }

    val clarity by animateFloatAsState(
        targetValue = 0.2f + relaxed * 0.6f,
        label = "clarity"
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF031525))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Peaceful Ocean", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { relaxed.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF64B5F6),
            trackColor = Color(0xFF0A2B45)
        )

        Spacer(Modifier.height(6.dp))
        Text("Relaxation ${(relaxed * 100).toInt()}%", color = Color.White)

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF062A45).copy(alpha = clarity)),
            contentAlignment = Alignment.Center
        ) {

            val faceScale by animateFloatAsState(
                targetValue = 0.9f + relaxed * 0.15f,
                label = "face"
            )

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .scale(faceScale)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (relaxed < 0.5f) "😟" else "😌",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                stones.forEach { stone ->
                    if (stone.dissolved < 1f) {
                        Box(
                            modifier = Modifier
                                .offset(
                                    x = (stone.offset.x / 2).dp,
                                    y = (stone.offset.y / 2).dp
                                )
                                .size(56.dp)
                                .alpha(1f - stone.dissolved)
                                .background(Color.DarkGray, CircleShape)
                                .pointerInput(stone.id) {
                                    detectDragGestures(
                                        onDrag = { change, drag ->
                                            change.consume()
                                            if (!stone.released) {
                                                stones = stones.map {
                                                    if (it.id == stone.id)
                                                        it.copy(offset = it.offset + drag)
                                                    else it
                                                }
                                            }
                                        },
                                        onDragEnd = {
                                            stones = stones.map {
                                                if (it.id == stone.id && it.offset.y > 80f)
                                                    it.copy(released = true)
                                                else if (it.id == stone.id)
                                                    it.copy(offset = Offset.Zero)
                                                else it
                                            }
                                        }
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🪨")
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Exit Ocean")
        }
    }}
