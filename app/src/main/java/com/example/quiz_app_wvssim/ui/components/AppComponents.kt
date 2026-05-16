package com.example.quiz_app_wvssim.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quiz_app_wvssim.domain.model.QuizCategory
import com.example.quiz_app_wvssim.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// ── Fond principal avec étoiles ──────────────────────────────────────────────

@Composable
fun StarfieldBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "stars")
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing)), label = ""
    )

    val stars = remember {
        List(120) {
            Triple(
                Random.nextFloat(),
                Random.nextFloat(),
                Random.nextFloat()
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to Abyss,
                    0.4f to DeepNavy,
                    1f to Color(0xFF050810)
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawStarfield(stars, shimmer)
            drawGeoGrid()
        }

        // Glow orb top-right
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = 80.dp, y = (-60).dp)
                .align(Alignment.TopEnd)
                .blur(120.dp)
                .background(
                    Brush.radialGradient(
                        listOf(Gold.copy(alpha = 0.08f), Color.Transparent)
                    ),
                    RoundedCornerShape(50)
                )
        )

        content()
    }
}

private fun DrawScope.drawStarfield(stars: List<Triple<Float, Float, Float>>, time: Float) {
    stars.forEachIndexed { i, (x, y, seed) ->
        val twinkle = 0.3f + 0.7f * ((sin(time * 2 * Math.PI.toFloat() + seed * 10f) + 1f) / 2f)
        val radius = 0.5f + seed * 1.5f
        drawCircle(
            color = if (seed > 0.8f) Gold.copy(alpha = twinkle * 0.6f) else SnowWhite.copy(alpha = twinkle * 0.4f),
            radius = radius,
            center = Offset(x * size.width, y * size.height)
        )
    }
}

private fun DrawScope.drawGeoGrid() {
    val gridColor = SnowWhite.copy(alpha = 0.025f)
    val step = size.width / 8f
    var x = 0f
    while (x <= size.width) {
        drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 0.5f)
        x += step
    }
    val stepY = size.height / 12f
    var y = 0f
    while (y <= size.height) {
        drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 0.5f)
        y += stepY
    }
}

// ── Glass Card ───────────────────────────────────────────────────────────────

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    glowColor: Color = Gold,
    cornerRadius: Dp = 20.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(glowColor.copy(alpha = 0.5f), glowColor.copy(alpha = 0.1f), Color.Transparent)
                ),
                shape = RoundedCornerShape(cornerRadius)
            ),
        color = GlassCard.copy(alpha = 0.7f),
        shape = RoundedCornerShape(cornerRadius),
        tonalElevation = 0.dp
    ) {
        Column(content = content)
    }
}

// ── Bouton primaire doré ──────────────────────────────────────────────────────

@Composable
fun GoldButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .background(
                if (enabled)
                    Brush.horizontalGradient(listOf(Gold, Amber))
                else
                    Brush.horizontalGradient(listOf(MistGray, MistGray)),
                RoundedCornerShape(16.dp)
            )
            .drawBehind {
                if (enabled) drawCircle(
                    Gold.copy(alpha = 0.25f),
                    radius = size.minDimension * 0.8f,
                    center = Offset(size.width / 2, size.height / 2),
                    blendMode = BlendMode.Screen
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Abyss,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = CloudGray
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(text, fontWeight = FontWeight.Black, letterSpacing = 1.sp, fontSize = 15.sp)
        }
    }
}

// ── Catégorie — couleur par continent ────────────────────────────────────────

fun QuizCategory.accentColor(): Color = when (this) {
    QuizCategory.EUROPE     -> EuropeColor
    QuizCategory.AFRICA     -> AfricaColor
    QuizCategory.ASIA       -> AsiaColor
    QuizCategory.MIDDLE_EAST-> MiddleEastColor
    QuizCategory.AMERICAS   -> AmericasColor
    QuizCategory.OCEANIA    -> OceaniaColor
    QuizCategory.WORLD      -> WorldColor
}

fun QuizCategory.emoji(): String = when (this) {
    QuizCategory.EUROPE      -> "🌍"
    QuizCategory.AFRICA      -> "🌍"
    QuizCategory.ASIA        -> "🌏"
    QuizCategory.MIDDLE_EAST -> "🕌"
    QuizCategory.AMERICAS    -> "🌎"
    QuizCategory.OCEANIA     -> "🏝"
    QuizCategory.WORLD       -> "🌐"
}

// ── Crown Badge ───────────────────────────────────────────────────────────────

@Composable
fun CrownBadge(crowns: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                Brush.horizontalGradient(listOf(GoldDim.copy(0.6f), Amber.copy(0.3f))),
                RoundedCornerShape(12.dp)
            )
            .border(1.dp, Gold.copy(0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("♛", fontSize = 16.sp, color = Gold)
            Spacer(Modifier.width(6.dp))
            Text(
                "$crowns",
                fontWeight = FontWeight.Black,
                color = Gold,
                fontSize = 17.sp,
                letterSpacing = 0.5.sp
            )
        }
    }
}

// ── Section header ────────────────────────────────────────────────────────────

@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(3.dp, 18.dp).background(Gold, RoundedCornerShape(2.dp)))
        Spacer(Modifier.width(10.dp))
        Text(
            title.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = Gold,
            letterSpacing = 2.sp
        )
    }
}
