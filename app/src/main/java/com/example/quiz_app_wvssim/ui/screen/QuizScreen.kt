package com.example.quiz_app_wvssim.ui.screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.quiz_app_wvssim.domain.model.QuizCategory
import com.example.quiz_app_wvssim.ui.components.*
import com.example.quiz_app_wvssim.ui.theme.*
import com.example.quiz_app_wvssim.ui.viewmodel.GameMode
import com.example.quiz_app_wvssim.ui.viewmodel.QuizViewModel
import java.util.Locale

enum class OptionStatus { DEFAULT, CORRECT, WRONG }

@Composable
fun QuizScreen(
    category: QuizCategory,
    mode: GameMode = GameMode.QUIZ,
    viewModel: QuizViewModel = viewModel(),
    onBackPressed: () -> Unit,
    onFinished: (score: Int, correct: Int, total: Int, crowns: Int) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showQuitDialog by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    var isListening by remember { mutableStateOf(false) }
    var recognizedText by remember { mutableStateOf("") }

    val speechRecognizer = remember {
        if (SpeechRecognizer.isRecognitionAvailable(context))
            SpeechRecognizer.createSpeechRecognizer(context) else null
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* permission handled on next tap */ }

    fun startVocalRecognition(options: List<String>, onMatch: (Int) -> Unit) {
        val sr = speechRecognizer ?: return
        isListening = true
        recognizedText = ""
        sr.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: android.os.Bundle?) {
                isListening = false
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) ?: return
                val spoken = matches.firstOrNull()?.lowercase(Locale.getDefault())?.trim() ?: return
                recognizedText = spoken
                val idx = options.indexOfFirst { opt ->
                    opt.lowercase(Locale.getDefault()).trim().let { o ->
                        spoken.contains(o) || o.contains(spoken) || spoken == o
                    }
                }
                if (idx >= 0) onMatch(idx) else onMatch(-1)
            }
            override fun onError(error: Int) { isListening = false; onMatch(-1) }
            override fun onReadyForSpeech(params: android.os.Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() { isListening = false }
            override fun onPartialResults(partialResults: android.os.Bundle?) {}
            override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
        })
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
        }
        sr.startListening(intent)
    }

    DisposableEffect(Unit) {
        onDispose { speechRecognizer?.destroy() }
    }

    LaunchedEffect(category) {
        viewModel.start(category, mode)
    }

    LaunchedEffect(state.lastAnswerCorrect) {
        if (state.lastAnswerCorrect == false) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    LaunchedEffect(state.finished) {
        if (state.finished) {
            onFinished(state.score, state.correctAnswers, state.totalQuestions, state.earnedCrowns)
        }
    }

    if (state.showReviveOption) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = NavySurface,
            titleContentColor = SnowWhite,
            textContentColor = CloudGray,
            title = { Text("Plus de vies !", fontWeight = FontWeight.Bold) },
            text = { Text("Utilise ${state.currentReviveCost} couronnes pour continuer ?") },
            confirmButton = {
                GoldButton(
                    "Revivre ♛${state.currentReviveCost}",
                    onClick = { viewModel.revive() },
                    modifier = Modifier.width(180.dp)
                )
            },
            dismissButton = {
                TextButton(onClick = onBackPressed) { Text("Quitter", color = MistGray) }
            }
        )
    }

    if (showQuitDialog) {
        AlertDialog(
            onDismissRequest = { showQuitDialog = false },
            containerColor = NavySurface,
            titleContentColor = SnowWhite,
            textContentColor = CloudGray,
            title = { Text("Quitter la partie ?", fontWeight = FontWeight.Bold) },
            text = { Text("Ton score et tes couronnes de cette session seront perdus.") },
            confirmButton = {
                TextButton(onClick = onBackPressed) {
                    Text("Quitter", color = CoralRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuitDialog = false }) {
                    Text("Continuer", color = Gold)
                }
            }
        )
    }

    StarfieldBackground {

        if (state.loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        } else {
            val question = state.currentQuestion
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { showQuitDialog = true }) {
                            Icon(Icons.Default.Close, contentDescription = "Quitter", tint = CloudGray)
                        }

                        if (state.gameMode == GameMode.TIME_ATTACK) {
                            val dangerTime = state.totalTimeRemaining < 10
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (dangerTime) CoralRed.copy(0.15f) else GlassCard,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .border(1.dp, if (dangerTime) CoralRed.copy(0.6f) else MistGray.copy(0.2f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Timer, null, tint = if (dangerTime) CoralRed else Amber, modifier = Modifier.size(18.dp))
                                    Text(
                                        " ${state.totalTimeRemaining}s",
                                        color = if (dangerTime) CoralRed else SnowWhite,
                                        fontWeight = FontWeight.Black, fontSize = 18.sp
                                    )
                                }
                            }
                        } else if (state.gameMode != GameMode.ZEN) {
                            repeat(when (state.gameMode) { GameMode.SURVIVAL -> 1; else -> 3 }) { index ->
                                Icon(
                                    if (index < state.lives) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    null,
                                    tint = if (index < state.lives) CoralRed else MistGray,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    // Score badge
                    Box(
                        modifier = Modifier
                            .background(GoldDim.copy(0.3f), RoundedCornerShape(12.dp))
                            .border(1.dp, Gold.copy(0.4f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("♛", color = Gold, fontSize = 14.sp)
                            Text(" ${state.score}", color = SnowWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }

                val progress = if (state.gameMode == GameMode.TIME_ATTACK)
                    (state.totalTimeRemaining / 60f).coerceIn(0f, 1f)
                else state.remainingSeconds / state.currentTimerMax.toFloat()

                // Progress bar avec glow
                Box(modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(GlassCard)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(
                                Brush.horizontalGradient(
                                    when {
                                        progress < 0.25f -> listOf(CoralRed, CoralRed.copy(0.6f))
                                        progress < 0.5f  -> listOf(Amber, Gold)
                                        else             -> listOf(Emerald, SkyBlue)
                                    }
                                ),
                                RoundedCornerShape(3.dp)
                            )
                    )
                }

                question?.let {
                    // Card drapeau avec bordure dorée
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .border(
                                1.dp,
                                Brush.linearGradient(listOf(Gold.copy(0.6f), Gold.copy(0.1f), Color.Transparent)),
                                RoundedCornerShape(20.dp)
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Black)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("file:///android_asset/flags/${it.flagUrl}.svg")
                                .decoderFactory(SvgDecoder.Factory())
                                .build(),
                            contentDescription = "Drapeau",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Text(
                        text = it.prompt,
                        style = MaterialTheme.typography.headlineSmall,
                        color = SnowWhite,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (mode == GameMode.VOCAL) {
                        VocalAnswerSection(
                            options = it.options,
                            isListening = isListening,
                            recognizedText = recognizedText,
                            acceptingAnswer = state.acceptingAnswer,
                            lastAnswerCorrect = state.lastAnswerCorrect,
                            correctIndex = it.correctIndex,
                            onMicClick = {
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                                    != PackageManager.PERMISSION_GRANTED) {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                } else {
                                    startVocalRecognition(it.options) { idx -> viewModel.submitAnswer(idx) }
                                }
                            }
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            it.options.forEachIndexed { index, option ->
                                val isCorrect = index == it.correctIndex
                                OptionButton(
                                    text = option,
                                    enabled = state.acceptingAnswer,
                                    onClick = { viewModel.submitAnswer(index) },
                                    status = when {
                                        state.acceptingAnswer -> OptionStatus.DEFAULT
                                        isCorrect -> OptionStatus.CORRECT
                                        state.lastAnswerCorrect == false && index != it.correctIndex -> OptionStatus.WRONG
                                        else -> OptionStatus.DEFAULT
                                    }
                                )
                            }
                        }
                    }

                    // Panneau VS IA
                    if (mode == GameMode.VS_AI) {
                        AiOpponentPanel(
                            aiIsThinking = state.aiIsThinking,
                            aiAnsweredCorrect = state.aiAnsweredCorrect,
                            aiScore = state.aiScore
                        )
                    }

                    // Bouton explication IA (après réponse, tous modes)
                    if (!state.acceptingAnswer && state.lastAnswerCorrect != null) {
                        AiExplanationSection(
                            explanation = state.aiExplanation,
                            isLoading = state.aiExplanationLoading,
                            onRequest = { viewModel.requestAiExplanation() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AiOpponentPanel(
    aiIsThinking: Boolean,
    aiAnsweredCorrect: Boolean?,
    aiScore: Int
) {
    val pulseAnim by rememberInfiniteTransition(label = "ai_pulse").animateFloat(
        initialValue = 0.4f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse), label = ""
    )
    val borderColor = when {
        aiAnsweredCorrect == true -> Emerald
        aiAnsweredCorrect == false -> CoralRed
        aiIsThinking -> Color(0xFF9C27B0)
        else -> MistGray.copy(0.3f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF9C27B0).copy(0.08f), RoundedCornerShape(14.dp))
            .border(1.dp, borderColor.copy(if (aiIsThinking) pulseAnim else 0.5f), RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🤖", fontSize = 22.sp)
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("ARIA", color = Color(0xFFCE93D8), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(
                    text = when {
                        aiIsThinking -> "Réflexion en cours..."
                        aiAnsweredCorrect == true -> "Bonne réponse !"
                        aiAnsweredCorrect == false -> "Mauvaise réponse"
                        else -> "En attente..."
                    },
                    color = when {
                        aiAnsweredCorrect == true -> Emerald
                        aiAnsweredCorrect == false -> CoralRed
                        else -> MistGray
                    },
                    fontSize = 12.sp
                )
            }
            if (aiIsThinking) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color(0xFF9C27B0),
                    strokeWidth = 2.dp
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("♛", color = Gold, fontSize = 13.sp)
                    Text(" $aiScore", color = SnowWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun AiExplanationSection(
    explanation: String,
    isLoading: Boolean,
    onRequest: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (explanation.isEmpty() && !isLoading) {
            TextButton(
                onClick = onRequest,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("💡 Explication IA", color = Color(0xFF00BCD4), fontSize = 13.sp)
            }
        }
        if (isLoading) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color(0xFF00BCD4), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text("ARIA rédige...", color = MistGray, fontSize = 12.sp)
            }
        }
        if (explanation.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF00BCD4).copy(0.08f), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFF00BCD4).copy(0.3f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row {
                    Text("💡 ", fontSize = 14.sp)
                    Text(explanation, color = CloudGray, fontSize = 13.sp, lineHeight = 19.sp)
                }
            }
        }
    }
}

@Composable
fun VocalAnswerSection(
    options: List<String>,
    isListening: Boolean,
    recognizedText: String,
    acceptingAnswer: Boolean,
    lastAnswerCorrect: Boolean?,
    correctIndex: Int,
    onMicClick: () -> Unit
) {
    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 1f, targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse), label = ""
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!acceptingAnswer && lastAnswerCorrect != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (lastAnswerCorrect) Emerald.copy(0.15f) else CoralRed.copy(0.15f),
                        RoundedCornerShape(12.dp)
                    )
                    .border(1.dp, if (lastAnswerCorrect) Emerald.copy(0.5f) else CoralRed.copy(0.5f), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = if (lastAnswerCorrect) "Bonne réponse !" else "Réponse : ${options.getOrNull(correctIndex) ?: ""}",
                    color = if (lastAnswerCorrect) Emerald else CoralRed,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (recognizedText.isNotEmpty() && acceptingAnswer) {
            Text(
                text = "\"$recognizedText\"",
                color = CloudGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .scale(if (isListening) pulse else 1f)
                .background(
                    if (isListening) CoralRed.copy(0.2f) else GlassCard,
                    CircleShape
                )
                .border(2.dp, if (isListening) CoralRed.copy(0.7f) else Gold.copy(0.4f), CircleShape)
                .clickable(enabled = acceptingAnswer, onClick = onMicClick)
        ) {
            Icon(
                imageVector = if (isListening) Icons.Default.MicNone else Icons.Default.Mic,
                contentDescription = "Parler",
                tint = if (isListening) CoralRed else Gold,
                modifier = Modifier.size(48.dp)
            )
        }

        Text(
            text = if (isListening) "Écoute en cours..." else if (!acceptingAnswer) "" else "Appuie et dis le nom du pays",
            color = MistGray,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OptionButton(text: String, enabled: Boolean, onClick: () -> Unit, status: OptionStatus) {
    val bg = when (status) {
        OptionStatus.DEFAULT -> GlassCard.copy(alpha = 0.8f)
        OptionStatus.CORRECT -> Emerald.copy(alpha = 0.2f)
        OptionStatus.WRONG   -> CoralRed.copy(alpha = 0.15f)
    }
    val border = when (status) {
        OptionStatus.DEFAULT -> MistGray.copy(alpha = 0.25f)
        OptionStatus.CORRECT -> Emerald
        OptionStatus.WRONG   -> CoralRed
    }
    val textColor = when (status) {
        OptionStatus.DEFAULT -> SnowWhite
        OptionStatus.CORRECT -> Emerald
        OptionStatus.WRONG   -> CoralRed
    }

    Surface(
        onClick = onClick,
        enabled = enabled,
        color = bg,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, border, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text, color = textColor, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            if (status != OptionStatus.DEFAULT) {
                Icon(
                    if (status == OptionStatus.CORRECT) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    null,
                    tint = if (status == OptionStatus.CORRECT) Emerald else CoralRed
                )
            }
        }
    }
}
