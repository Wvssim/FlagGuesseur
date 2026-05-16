package com.example.quiz_app_wvssim.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.quiz_app_wvssim.domain.model.QuizCategory
import com.example.quiz_app_wvssim.ui.components.GoldButton
import com.example.quiz_app_wvssim.ui.components.StarfieldBackground
import com.example.quiz_app_wvssim.ui.components.accentColor
import com.example.quiz_app_wvssim.ui.theme.*
import com.example.quiz_app_wvssim.ui.viewmodel.DuelPhase
import com.example.quiz_app_wvssim.ui.viewmodel.DuelViewModel

@Composable
fun DuelScreen(
    category: QuizCategory,
    userName: String,
    viewModel: DuelViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.startMatchmaking(userName, category) }

    StarfieldBackground {
        when (state.phase) {
            DuelPhase.SEARCHING, DuelPhase.WAITING_OPPONENT ->
                DuelSearchingScreen(
                    isWaiting = state.phase == DuelPhase.WAITING_OPPONENT,
                    duelId = state.duelId,
                    onBack = onBack
                )
            DuelPhase.COUNTDOWN -> DuelCountdownScreen(state.countdown)
            DuelPhase.PLAYING   -> DuelPlayingScreen(state = state, onAnswer = viewModel::submitAnswer)
            DuelPhase.FINISHED  -> DuelResultScreen(
                myName = userName,
                myScore = state.myScore,
                opponentName = state.opponentName,
                opponentScore = state.opponentScore,
                category = category,
                onBack = onBack
            )
        }

        if (state.error.isNotEmpty()) {
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                containerColor = CoralDim,
                action = { TextButton(onClick = onBack) { Text("Retour", color = CoralRed) } }
            ) { Text(state.error, color = SnowWhite) }
        }
    }
}

@Composable
private fun DuelSearchingScreen(isWaiting: Boolean, duelId: String, onBack: () -> Unit) {
    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 0.9f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(900, easing = EaseInOutSine), RepeatMode.Reverse), label = ""
    )
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .scale(pulse)
                .background(GoldDim.copy(0.2f), CircleShape)
                .border(2.dp, Gold.copy(0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("⚔️", fontSize = 36.sp)
        }
        Spacer(Modifier.height(28.dp))
        Text(
            if (isWaiting) "Salle créée !" else "Recherche...",
            style = MaterialTheme.typography.headlineMedium,
            color = SnowWhite
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (isWaiting) "En attente d'un adversaire" else "Connexion à un duel disponible",
            color = MistGray, fontSize = 14.sp, textAlign = TextAlign.Center
        )
        if (isWaiting && duelId.isNotEmpty()) {
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .background(GlassCard, RoundedCornerShape(12.dp))
                    .border(1.dp, Gold.copy(0.3f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Code : ", color = MistGray, fontSize = 13.sp)
                Text(duelId.take(8).uppercase(), color = Gold, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
            }
        }
        Spacer(Modifier.height(36.dp))
        TextButton(onClick = onBack) {
            Text("Annuler", color = MistGray)
        }
    }
}

@Composable
private fun DuelCountdownScreen(countdown: Int) {
    val scale by animateFloatAsState(
        targetValue = if (countdown > 0) 1f else 0.5f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 300f),
        label = "count"
    )
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                if (countdown > 0) "$countdown" else "GO !",
                modifier = Modifier.scale(scale),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Black,
                    brush = Brush.verticalGradient(listOf(Gold, Amber))
                ),
                fontSize = 100.sp
            )
            Spacer(Modifier.height(16.dp))
            Text("Prépare-toi !", color = MistGray, letterSpacing = 2.sp, fontSize = 13.sp)
        }
    }
}

@Composable
private fun DuelPlayingScreen(
    state: com.example.quiz_app_wvssim.ui.viewmodel.DuelUiState,
    onAnswer: (Int) -> Unit
) {
    val question = state.questions.getOrNull(state.currentIndex) ?: return
    val context = LocalContext.current
    val danger = state.remainingSeconds < 5

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        // Scores adversaires
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayerScore(name = "Toi", score = state.myScore, color = SkyBlue)

            // Timer central
            Box(
                modifier = Modifier
                    .background(
                        if (danger) CoralRed.copy(0.15f) else GlassCard,
                        RoundedCornerShape(12.dp)
                    )
                    .border(1.dp, if (danger) CoralRed.copy(0.6f) else MistGray.copy(0.2f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    "${state.remainingSeconds}s",
                    color = if (danger) CoralRed else SnowWhite,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            }

            PlayerScore(
                name = state.opponentName.ifEmpty { "Adversaire" },
                score = state.opponentScore,
                color = CoralRed
            )
        }

        // Progression questions
        Box(modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape).background(GlassCard)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth((state.currentIndex + 1f) / state.questions.size.coerceAtLeast(1))
                    .fillMaxHeight()
                    .background(
                        Brush.horizontalGradient(listOf(Gold, Amber)),
                        RoundedCornerShape(2.dp)
                    )
            )
        }

        // Drapeau
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(
                    1.dp,
                    Brush.linearGradient(listOf(Gold.copy(0.5f), Gold.copy(0.1f), Color.Transparent)),
                    RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("file:///android_asset/flags/${question.flagUrl}.svg")
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Text(
            question.prompt,
            style = MaterialTheme.typography.headlineSmall,
            color = SnowWhite,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            question.options.forEachIndexed { index, option ->
                val isCorrect = index == question.correctIndex
                OptionButton(
                    text = option,
                    enabled = state.acceptingAnswer,
                    onClick = { onAnswer(index) },
                    status = when {
                        state.acceptingAnswer -> OptionStatus.DEFAULT
                        isCorrect -> OptionStatus.CORRECT
                        state.lastAnswerCorrect == false && index != question.correctIndex -> OptionStatus.WRONG
                        else -> OptionStatus.DEFAULT
                    }
                )
            }
        }
    }
}

@Composable
private fun PlayerScore(name: String, score: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(name, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp,
            maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 90.dp))
        Text("$score", color = SnowWhite, fontWeight = FontWeight.Black, fontSize = 20.sp)
    }
}

@Composable
private fun DuelResultScreen(
    myName: String,
    myScore: Int,
    opponentName: String,
    opponentScore: Int,
    category: QuizCategory,
    onBack: () -> Unit
) {
    val iWon = myScore > opponentScore
    val isDraw = myScore == opponentScore
    val accent = category.accentColor()

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            when { isDraw -> "Égalité !"; iWon -> "Victoire !" ; else -> "Défaite..." },
            style = MaterialTheme.typography.headlineLarge,
            color = when { isDraw -> CloudGray; iWon -> Gold; else -> CoralRed }
        )
        Spacer(Modifier.height(8.dp))
        Text(category.displayName, color = MistGray, letterSpacing = 2.sp, fontSize = 12.sp)

        Spacer(Modifier.height(36.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DuelPlayerResult(name = myName, score = myScore, isWinner = iWon, isDraw = isDraw)
            Text("VS", color = MistGray, fontWeight = FontWeight.Black, fontSize = 20.sp)
            DuelPlayerResult(name = opponentName.ifEmpty { "Adversaire" }, score = opponentScore, isWinner = !iWon && !isDraw, isDraw = isDraw)
        }

        Spacer(Modifier.height(48.dp))

        GoldButton("Retour à l'accueil", onClick = onBack, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun DuelPlayerResult(name: String, score: Int, isWinner: Boolean, isDraw: Boolean) {
    val color = when { isDraw -> CloudGray; isWinner -> Gold; else -> MistGray }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(color.copy(0.1f), CircleShape)
                .border(2.dp, color.copy(if (isWinner || isDraw) 0.7f else 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                if (isWinner && !isDraw) "🏆" else name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                fontSize = if (isWinner && !isDraw) 28.sp else 22.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
        Text(
            name, color = if (isWinner) SnowWhite else CloudGray,
            fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp, maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 100.dp)
        )
        Text("$score pts", color = color, fontWeight = FontWeight.Black, fontSize = 18.sp)
    }
}
