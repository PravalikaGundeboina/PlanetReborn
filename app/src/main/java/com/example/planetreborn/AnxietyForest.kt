package com.example.planetreborn




import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun AnxietyForestScreen(navController: NavController) {

    var level by remember { mutableStateOf(1) }
    var running by remember { mutableStateOf(false) }
    var phase by remember { mutableStateOf("Ready") }
    var secondsLeft by remember { mutableStateOf(150) }

    val pulse by animateFloatAsState(
        targetValue = if (running && phase == "Inhale") 1.18f else 0.92f,
        label = "pulse"
    )

    fun levelPattern(lvl: Int): Triple<Int, Int, Int> {
        return when (lvl) {
            1 -> Triple(4, 0, 4)
            2 -> Triple(4, 2, 6)
            3 -> Triple(5, 3, 7)
            else -> Triple(6, 4, 8)
        }
    }

    LaunchedEffect(running, level) {
        if (!running) return@LaunchedEffect

        val (inhale, hold, exhale) = levelPattern(level)

        while (running && secondsLeft > 0) {
            phase = "Inhale"
            repeat(inhale) {
                if (!running) return@LaunchedEffect
                delay(1000)
                secondsLeft--
            }

            if (hold > 0) {
                phase = "Hold"
                repeat(hold) {
                    if (!running) return@LaunchedEffect
                    delay(1000)
                    secondsLeft--
                }
            }

            phase = "Exhale"
            repeat(exhale) {
                if (!running) return@LaunchedEffect
                delay(1000)
                secondsLeft--
            }
        }

        if (secondsLeft <= 0) {
            running = false
            phase = "Completed 🌿"
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF06130A))
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Anxiety Forest", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(6.dp))
        Text("Level $level • $phase", color = Color(0xFFBDBDBD))

        Spacer(Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .size(220.dp)
                .scale(pulse)
                .background(Color(0xFF2E7D32), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (!running) "🌲" else if (phase == "Inhale") "⬆️" else if (phase == "Hold") "⏸️" else "⬇️",
                style = MaterialTheme.typography.headlineLarge
            )
        }

        Spacer(Modifier.height(22.dp))

        Text(
            "Time left: ${secondsLeft}s",
            color = Color(0xFFE0E0E0),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(18.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    secondsLeft = 150
                    phase = "Ready"
                    running = false
                }
            ) { Text("Reset") }

            Button(
                onClick = { running = !running },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (running) Color(0xFFD32F2F) else Color(0xFF43A047)
                )
            ) {
                Text(if (running) "Stop" else "Start")
            }
        }

        Spacer(Modifier.height(14.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = { if (level > 1) level-- }) { Text("◀ Level") }
            OutlinedButton(onClick = { if (level < 4) level++ }) { Text("Level ▶") }
        }

        Spacer(Modifier.height(18.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Exit Forest")
        }
    }
}
