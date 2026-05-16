package com.example.quiz_app_wvssim.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quiz_app_wvssim.domain.model.QuizCategory
import com.example.quiz_app_wvssim.ui.components.*
import com.example.quiz_app_wvssim.ui.theme.*
import com.example.quiz_app_wvssim.ui.viewmodel.LeaderboardViewModel
import com.example.quiz_app_wvssim.utils.NotificationHelper

@Composable
fun ResultScreen(
    category: QuizCategory,
    score: Int,
    correct: Int,
    total: Int,
    earnedCrowns: Int,
    userName: String,
    leaderboardViewModel: LeaderboardViewModel,
    onReplay: () -> Unit,
    onLeaderboard: () -> Unit
) {
    val submitted = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val accuracy = if (total > 0) (correct * 100 / total) else 0

    LaunchedEffect(category, score) {
        if (!submitted.value) {
            leaderboardViewModel.submitScoreIfNeeded(category, score, userName = userName)
            submitted.value = true
            if (score > 0) NotificationHelper.notifyIfRecord(context, category.displayName, score, userName)
        }
    }

    // Animation d'entrée du score
    val scoreScale by rememberInfiniteTransition(label = "score").animateFloat(
        initialValue = 1f, targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse), label = ""
    )

    StarfieldBackground {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Titre résultat
            val perfLabel = when {
                accuracy >= 90 -> "Parfait !"
                accuracy >= 70 -> "Très bien !"
                accuracy >= 50 -> "Bien joué !"
                else           -> "Continue !"
            }
            val perfColor = when {
                accuracy >= 90 -> Gold
                accuracy >= 70 -> Emerald
                accuracy >= 50 -> SkyBlue
                else           -> MistGray
            }

            Text(
                perfLabel,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 36.sp),
                color = perfColor
            )
            Spacer(Modifier.height(4.dp))
            Text(category.displayName, color = MistGray, letterSpacing = 2.sp, fontSize = 13.sp)

            Spacer(Modifier.height(28.dp))

            // Score principal
            GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = Gold) {
                Column(
                    Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Grand score
                    Text(
                        "$score",
                        modifier = Modifier.scale(scoreScale),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Black,
                            brush = Brush.verticalGradient(listOf(Gold, Amber))
                        ),
                        fontSize = 72.sp
                    )
                    Text(
                        "POINTS",
                        style = MaterialTheme.typography.labelLarge,
                        color = GoldDim,
                        letterSpacing = 4.sp
                    )

                    // Couronnes gagnées
                    if (earnedCrowns > 0) {
                        Box(
                            modifier = Modifier
                                .border(1.dp, Gold.copy(0.4f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(
                                "♛ +$earnedCrowns couronnes",
                                color = Gold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    HorizontalDivider(color = MistGray.copy(0.2f))

                    // Statistiques
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ResultStat("CORRECT", "$correct", Emerald)
                        ResultStatDivider()
                        ResultStat("TOTAL", "$total", CloudGray)
                        ResultStatDivider()
                        ResultStat("PRÉCISION", "$accuracy%", perfColor)
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // Boutons
            GoldButton(
                text = "Voir le Classement",
                onClick = onLeaderboard,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onReplay,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MistGray.copy(0.4f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CloudGray)
            ) {
                Icon(Icons.Default.Replay, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Rejouer", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun ResultStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(value, style = MaterialTheme.typography.titleLarge, color = color, fontWeight = FontWeight.Black)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MistGray, letterSpacing = 1.sp)
    }
}

@Composable
private fun ResultStatDivider() {
    Box(Modifier.size(1.dp, 32.dp).padding(vertical = 4.dp))
}
