package com.example.planetreborn

import android.os.Bundle
import android.view.SoundEffectConstants
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planetreborn.ui.theme.PlanetRebornTheme
import kotlin.random.Random
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.shape.CircleShape
import kotlinx.coroutines.delay



/* ================= MAIN ACTIVITY ================= */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlanetRebornTheme {

                val nav = rememberNavController()

                NavHost(
                    navController = nav,
                    startDestination = "welcome"
                ) {

                    composable("welcome") {
                        WelcomeScreen { nav.navigate("universe") }
                    }

                    composable("universe") {
                        UniverseScreen(
                            onExploreClick = { nav.navigate("worlds") },
                            onBack = { nav.popBackStack() }
                        )
                    }

                    composable("worlds") {
                        WorldSelectionScreen(
                            onAnxietyClick = { nav.navigate("anxiety") },
                            onOceanClick = { nav.navigate("ocean") },
                            onFireClick = { nav.navigate("fire") },
                            onEarthClick = { nav.navigate("earth") },
                            onEnergyClick = { nav.navigate("energy") },
                            onBack = { nav.popBackStack() }
                        )
                    }

                    composable("anxiety") {
                        AnxietyForestIntroScreen(
                            onEnterWorld = { nav.navigate("anxiety_forest") },
                            onBack = { nav.popBackStack() }
                        )
                    }

                    composable("anxiety_forest") {
                        AnxietyForestScreen(nav)
                    }

                    composable("ocean") {
                        PeacefulOceanIntroScreen(
                            onEnterWorld = { nav.navigate("ocean_world") },
                            onBack = { nav.popBackStack() }
                        )
                    }

                    composable("ocean_world") {
                        PeacefulOceanScreen(nav)
                    }

                    composable("fire") { FireWorldScreen(nav) }
                    composable("earth") { EarthWorldScreen(nav) }
                    composable("energy") { EnergyWorldScreen(nav) }
                }
            }
        }
    }
}

/* ================= UTIL ================= */

fun clickSound(view: View) =
    view.playSoundEffect(SoundEffectConstants.CLICK)

/* ================= BACKGROUND ================= */

@Composable
fun AnimatedGradientBackground(colors: List<Color>) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors))
    )
}

/* ================= FLOATING BUBBLES ================= */

data class Bubble(val x: Float, val y: Float, val r: Float, val speed: Float)

@Composable
fun FloatingBubbles(count: Int = 40) {
    val bubbles = remember {
        List(count) {
            Bubble(
                Random.nextFloat(),
                Random.nextFloat(),
                Random.nextFloat() * 12f + 6f,
                Random.nextFloat() * 0.2f + 0.05f
            )
        }
    }

    val t by rememberInfiniteTransition().animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(20000))
    )

    Canvas(Modifier.fillMaxSize()) {
        bubbles.forEach {
            val y = (it.y - t * it.speed) % 1f
            drawCircle(
                Color(0xFFB3E5FC).copy(0.35f),
                it.r,
                Offset(it.x * size.width, y * size.height)
            )
        }
    }
}

/* ================= TITLES ================= */

@Composable
fun GlowingTitleText(text: String, size: TextUnit) {
    Text(text, fontSize = size, letterSpacing = 2.sp, color = Color(0xFF00E5FF))
}

@Composable
fun GameTitle() {
    val float by rememberInfiniteTransition().animateFloat(
        -10f, 10f,
        infiniteRepeatable(tween(6000), RepeatMode.Reverse)
    )

    Column(
        Modifier.offset(y = float.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlowingTitleText("PLANET", 48.sp)
        GlowingTitleText("REBORN", 64.sp)
    }
}

/* ================= BREATHING ORB ================= */

@Composable
fun BreathingOrb() {
    val phase by rememberInfiniteTransition().animateFloat(
        0f, 1f,
        infiniteRepeatable(
            tween(5000, easing = EaseInOutSine),
            RepeatMode.Reverse
        )
    )

    Canvas(Modifier.size(220.dp)) {
        drawCircle(
            Color(0xFF81C784),
            80f + 40f * phase,
            center
        )
    }
}

/* ================= WELCOME ================= */

@Composable
fun WelcomeScreen(onStartClick: () -> Unit) {
    val view = LocalView.current

    Box(Modifier.fillMaxSize()) {
        AnimatedGradientBackground(listOf(Color(0xFF000428), Color(0xFF004e92)))
        FloatingBubbles()

        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameTitle()
            Spacer(Modifier.height(30.dp))
            Text(
                "Take a breath.\nThis space is for you.",
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(40.dp))
            Button(onClick = { clickSound(view); onStartClick() }) {
                Text("Begin Journey")
            }
        }
    }
}

/* ================= UNIVERSE ================= */

@Composable
fun UniverseScreen(onExploreClick: () -> Unit, onBack: () -> Unit) {
    val view = LocalView.current

    Box(Modifier.fillMaxSize()) {
        AnimatedGradientBackground(listOf(Color(0xFF0F2027), Color(0xFF203A43)))
        FloatingBubbles()

        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Nauvara", style = MaterialTheme.typography.headlineLarge, color = Color.White)
            Spacer(Modifier.height(16.dp))
            Button(onClick = { clickSound(view); onExploreClick() }) {
                Text("Explore Planets")
            }
        }
    }
}

/* ================= WORLDS ================= */

@Composable
fun WorldSelectionScreen(
    onAnxietyClick: () -> Unit,
    onOceanClick: () -> Unit,
    onFireClick: () -> Unit,
    onEarthClick: () -> Unit,
    onEnergyClick: () -> Unit,
    onBack: () -> Unit
) {
    val view = LocalView.current

    Box(Modifier.fillMaxSize()) {
        AnimatedGradientBackground(listOf(Color(0xFF004e92), Color(0xFF000428)))
        FloatingBubbles()

        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { clickSound(view); onAnxietyClick() }) { Text("🌲 Anxiety Forest") }
            Spacer(Modifier.height(16.dp))
            Button(onClick = { clickSound(view); onOceanClick() }) { Text("🌊 Peaceful Ocean") }
            Spacer(Modifier.height(16.dp))
            Button(onClick = { clickSound(view); onFireClick() }) { Text("🔥 Fire Core") }
            Spacer(Modifier.height(16.dp))
            Button(onClick = { clickSound(view); onEarthClick() }) { Text("🌍 Earth Core") }
            Spacer(Modifier.height(16.dp))
            Button(onClick = { clickSound(view); onEnergyClick() }) { Text("⚡ Energy Core") }
        }
    }
}

/* ================= INTRO SCREENS ================= */

@Composable
fun AnxietyForestIntroScreen(onEnterWorld: () -> Unit, onBack: () -> Unit) {
    val view = LocalView.current

    Box(Modifier.fillMaxSize()) {
        AnimatedGradientBackground(listOf(Color(0xFF0B3D2E), Color(0xFF2E7D32)))

        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🌲 Anxiety Forest 🌿", color = Color.White, fontSize = 22.sp)
            Spacer(Modifier.height(20.dp))
            Text(
                "This forest listens to your breath.\nThere is no hurry here.",
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(40.dp))
            BreathingOrb()
            Spacer(Modifier.height(36.dp))
            Button(onClick = { clickSound(view); onEnterWorld() }) {
                Text(" Enter the Forest ")
            }
        }
    }
}

@Composable
fun PeacefulOceanIntroScreen(onEnterWorld: () -> Unit, onBack: () -> Unit) {
    val view = LocalView.current

    Box(Modifier.fillMaxSize()) {
        AnimatedGradientBackground(
            listOf(Color(0xFF003973), Color(0xFF2196F3), Color(0xFF81D4FA))
        )

        Column(
            Modifier.align(Alignment.Center),







            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🌊 Peaceful Ocean 🫧", style = MaterialTheme.typography.headlineLarge, color = Color.White)
            Spacer(Modifier.height(20.dp))
            Text(
                "Let yourself float.\nMove with the waves.",
                color = Color.White.copy(0.9f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(32.dp))
            Button(onClick = { clickSound(view); onEnterWorld() }) {
                Text(" Enter the Ocean ")
            }
        }
    }
}

/* ================= CORE WORLDS ================= */

@Composable
fun FireWorldScreen(nav: NavHostController) {
    var heat by remember { mutableStateOf(100f) }
    var lastTap by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        while (heat > 0f) {
            delay(1000)
            heat += 3f // fire grows naturally
            heat = heat.coerceAtMost(100f)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFD84315)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("🔥 Fire Core", color = Color.White, fontSize = 26.sp)

        Spacer(Modifier.height(20.dp))

        LinearProgressIndicator(
            progress = heat / 100f,
            modifier = Modifier.fillMaxWidth(0.7f),
            color = Color.Yellow
        )

        Spacer(Modifier.height(30.dp))

        Button(onClick = {
            val now = System.currentTimeMillis()
            if (now - lastTap > 1200) {
                heat -= 15f // calm action
            } else {
                heat += 10f // panic tapping
            }
            lastTap = now

            heat = heat.coerceIn(0f, 100f)
            if (heat <= 0f) nav.popBackStack()
        }) {
            Text("🔥 Breathe & Cool")
        }

        Spacer(Modifier.height(12.dp))
        Text("Heat: ${heat.toInt()}%", color = Color.White)
    }
}


@Composable
fun EarthWorldScreen(nav: NavHostController) {
    var stability by remember { mutableStateOf(50f) }
    var lastTapTime by remember { mutableStateOf(0L) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF2E7D32)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("🌍 Earth Core", color = Color.White, fontSize = 26.sp)

        Spacer(Modifier.height(20.dp))

        LinearProgressIndicator(
            progress = stability / 100f,
            modifier = Modifier.fillMaxWidth(0.7f)
        )

        Spacer(Modifier.height(30.dp))

        Button(onClick = {
            val now = System.currentTimeMillis()
            if (now - lastTapTime < 800) {
                stability -= 8f   // rushed = unstable
            } else {
                stability += 6f   // calm placement
            }
            lastTapTime = now

            stability = stability.coerceIn(0f, 100f)

            if (stability >= 100f) nav.popBackStack()
        }) {
            Text("🌱 Place Stone")
        }

        Spacer(Modifier.height(12.dp))
        Text("Stability: ${stability.toInt()}%", color = Color.White)
    }
}

@Composable
fun EnergyWorldScreen(nav: NavHostController) {
    var energy by remember { mutableStateOf(0f) }
    var combo by remember { mutableStateOf(1) }
    var lastTap by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        while (energy > 0f) {
            delay(900)
            energy -= 2f
            energy = energy.coerceAtLeast(0f)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF4527A0)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("⚡ Energy Core", color = Color.White, fontSize = 26.sp)

        Spacer(Modifier.height(20.dp))

        LinearProgressIndicator(
            progress = energy / 100f,
            modifier = Modifier.fillMaxWidth(0.7f),
            color = Color.Cyan
        )

        Spacer(Modifier.height(30.dp))

        Button(onClick = {
            val now = System.currentTimeMillis()
            combo = if (now - lastTap in 600..1200) combo + 1 else 1
            energy += 6f * combo
            lastTap = now

            energy = energy.coerceAtMost(100f)
            if (energy >= 100f) nav.popBackStack()
        }) {
            Text("⚡ Charge ($combo)")
        }

        Spacer(Modifier.height(12.dp))
        Text("Energy: ${energy.toInt()}%", color = Color.White)
    }
}
