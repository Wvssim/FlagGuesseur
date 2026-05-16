package com.example.quiz_app_wvssim.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quiz_app_wvssim.domain.model.QuizCategory
import com.example.quiz_app_wvssim.ui.components.*
import com.example.quiz_app_wvssim.ui.theme.*
import com.example.quiz_app_wvssim.ui.viewmodel.LeaderboardViewModel

@Composable
fun LeaderboardScreen(
    category: QuizCategory,
    viewModel: LeaderboardViewModel,
    onBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val accent = category.accentColor()

    LaunchedEffect(category) { viewModel.bind(category) }

    StarfieldBackground {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(52.dp))

            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .background(GlassCard, CircleShape)
                        .border(1.dp, MistGray.copy(0.3f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour", tint = CloudGray)
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        "CLASSEMENT",
                        style = MaterialTheme.typography.labelLarge,
                        color = accent,
                        letterSpacing = 3.sp
                    )
                    Text(
                        category.displayName,
                        style = MaterialTheme.typography.headlineMedium,
                        color = SnowWhite
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(category.emoji(), fontSize = 32.sp)
            }

            Spacer(Modifier.height(24.dp))

            when {
                state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Gold, strokeWidth = 2.dp)
                }
                state.entries.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🏁", fontSize = 48.sp)
                        Spacer(Modifier.height(12.dp))
                        Text("Aucun score encore", color = MistGray, fontSize = 16.sp)
                        Text("Sois le premier !", color = MistGray.copy(0.6f), fontSize = 13.sp)
                    }
                }
                else -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    itemsIndexed(state.entries) { index, entry ->
                        LeaderboardItem(index = index, name = entry.userName, score = entry.score, accent = accent)
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardItem(index: Int, name: String, score: Int, accent: Color = Gold) {
    val isPodium = index < 3
    val rankColor = when (index) {
        0 -> Gold
        1 -> CloudGray
        2 -> Color(0xFFCD7F32) // bronze
        else -> MistGray
    }
    val rankEmoji = when (index) {
        0 -> "🥇"; 1 -> "🥈"; 2 -> "🥉"; else -> null
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isPodium) Modifier.border(
                    1.dp,
                    Brush.horizontalGradient(listOf(rankColor.copy(0.5f), Color.Transparent)),
                    RoundedCornerShape(16.dp)
                ) else Modifier
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isPodium) rankColor.copy(0.08f) else GlassCard.copy(0.5f)
            )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Rang
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(rankColor.copy(if (isPodium) 0.2f else 0.1f), CircleShape)
                        .border(1.dp, rankColor.copy(if (isPodium) 0.6f else 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (rankEmoji != null) {
                        Text(rankEmoji, fontSize = 16.sp)
                    } else {
                        Text("${index + 1}", color = MistGray, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
                Spacer(Modifier.width(14.dp))
                Text(
                    name,
                    color = if (isPodium) SnowWhite else CloudGray,
                    fontWeight = if (isPodium) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 16.sp
                )
            }
            // Score
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("♛", color = rankColor, fontSize = 13.sp)
                Spacer(Modifier.width(4.dp))
                Text(
                    "$score",
                    color = if (isPodium) rankColor else CloudGray,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            }
        }
    }
}
