package com.example.quiz_app_wvssim.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quiz_app_wvssim.domain.model.QuizCategory
import com.example.quiz_app_wvssim.ui.components.*
import com.example.quiz_app_wvssim.ui.theme.*
import com.example.quiz_app_wvssim.ui.viewmodel.GameMode
import com.example.quiz_app_wvssim.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCategorySelected: (QuizCategory, GameMode) -> Unit,
    onLiaClick: () -> Unit = {}
) {
    val scroll = rememberScrollState()
    var selectedMode by remember { mutableStateOf(GameMode.QUIZ) }
    val userPrefs by viewModel.userPreferences.collectAsStateWithLifecycle()
    var showNameDialog by remember { mutableStateOf(false) }

    val currentLevel = getLevelInfo(userPrefs.crowns)

    if (showNameDialog) {
        var tempName by remember { mutableStateOf(userPrefs.userName) }
        AlertDialog(
            onDismissRequest = { showNameDialog = false },
            containerColor = NavySurface,
            titleContentColor = SnowWhite,
            textContentColor = CloudGray,
            title = { Text("Modifier le pseudo", style = MaterialTheme.typography.headlineSmall) },
            text = {
                OutlinedTextField(
                    value = tempName,
                    onValueChange = { tempName = it },
                    label = { Text("Pseudo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold,
                        focusedLabelColor = Gold,
                        cursorColor = Gold,
                        unfocusedBorderColor = MistGray,
                        unfocusedLabelColor = MistGray
                    )
                )
            },
            confirmButton = {
                GoldButton("Enregistrer", onClick = {
                    viewModel.updateUserName(tempName)
                    showNameDialog = false
                }, modifier = Modifier.width(160.dp))
            },
            dismissButton = {
                TextButton(onClick = { showNameDialog = false }) {
                    Text("Annuler", color = MistGray)
                }
            }
        )
    }

    StarfieldBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(Modifier.height(52.dp))

            // ── Titre ─────────────────────────────────────────────────────────
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "FLAG",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 48.sp,
                        letterSpacing = 12.sp,
                        brush = Brush.horizontalGradient(listOf(Gold, SnowWhite, Gold))
                    )
                )
                Text(
                    "GUESSEUR",
                    style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 8.sp),
                    color = MistGray
                )
            }

            // ── Profil ────────────────────────────────────────────────────────
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showNameDialog = true },
                glowColor = currentLevel.color
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                Brush.radialGradient(listOf(currentLevel.color.copy(0.3f), Color.Transparent)),
                                CircleShape
                            )
                            .border(1.dp, currentLevel.color.copy(0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            userPrefs.userName.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                            fontWeight = FontWeight.Black,
                            color = currentLevel.color,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    // Nom + rang — prend l'espace restant
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            userPrefs.userName,
                            color = SnowWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(6.dp).background(currentLevel.color, CircleShape))
                            Spacer(Modifier.width(6.dp))
                            Text(currentLevel.title, color = currentLevel.color, fontSize = 12.sp, letterSpacing = 1.sp)
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    CrownBadge(crowns = userPrefs.crowns)
                }

                // Barre de progression vers prochain niveau
                val progress = getLevelProgress(userPrefs.crowns)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(GlassCard)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(
                                Brush.horizontalGradient(listOf(currentLevel.color, Gold)),
                                RoundedCornerShape(2.dp)
                            )
                    )
                }
            }

            // ── LIA ───────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF00BCD4).copy(0.12f), Color(0xFF9C27B0).copy(0.12f))
                        ),
                        RoundedCornerShape(18.dp)
                    )
                    .border(
                        1.dp,
                        Brush.horizontalGradient(
                            listOf(Color(0xFF00BCD4).copy(0.5f), Color(0xFF9C27B0).copy(0.5f))
                        ),
                        RoundedCornerShape(18.dp)
                    )
                    .clickable(onClick = onLiaClick)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    Brush.radialGradient(listOf(Color(0xFF9C27B0).copy(0.3f), Color.Transparent)),
                                    CircleShape
                                )
                                .border(1.dp, Color(0xFF9C27B0).copy(0.5f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🤖", fontSize = 20.sp)
                        }
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text(
                                "LIA",
                                color = Color(0xFF00BCD4),
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                letterSpacing = 3.sp
                            )
                            Text("Assistante IA · Explications & QCM", color = MistGray, fontSize = 12.sp)
                        }
                    }
                    Icon(
                        Icons.Default.ChevronRight,
                        null,
                        tint = Color(0xFF00BCD4).copy(0.7f),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // ── Modes ─────────────────────────────────────────────────────────
            SectionHeader("Mode de jeu", modifier = Modifier.fillMaxWidth())

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(end = 8.dp)
            ) {
                items(GameMode.entries) { mode ->
                    ModeChip(mode = mode, isSelected = selectedMode == mode, onClick = { selectedMode = mode })
                }
            }

            // Description mode
            AnimatedContent(targetState = selectedMode, label = "desc") { mode ->
                GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = MistGray, cornerRadius = 14.dp) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(36.dp)
                                .background(modeColor(mode).copy(0.15f), CircleShape)
                                .border(1.dp, modeColor(mode).copy(0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(modeIcon(mode), null, tint = modeColor(mode), modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(mode.description, color = CloudGray, fontSize = 13.sp, lineHeight = 18.sp)
                    }
                }
            }

            // ── Continents ────────────────────────────────────────────────────
            SectionHeader("Choisir un continent", modifier = Modifier.fillMaxWidth())

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                QuizCategory.entries.forEach { category ->
                    CategoryCard(category = category, onClick = { onCategorySelected(category, selectedMode) })
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun ModeChip(mode: GameMode, isSelected: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) modeColor(mode) else MistGray
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) color.copy(0.15f) else GlassCard.copy(0.5f))
            .border(1.dp, if (isSelected) color.copy(0.7f) else MistGray.copy(0.2f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(modeIcon(mode), null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(mode.displayName, color = if (isSelected) SnowWhite else CloudGray, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
        }
    }
}

@Composable
fun CategoryCard(category: QuizCategory, onClick: () -> Unit) {
    val accent = category.accentColor()
    GlassCard(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        glowColor = accent,
        cornerRadius = 18.dp
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(accent.copy(0.15f), RoundedCornerShape(12.dp))
                        .border(1.dp, accent.copy(0.4f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(category.emoji(), fontSize = 22.sp)
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(category.displayName, color = SnowWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Quiz de drapeaux", color = MistGray, fontSize = 12.sp)
                }
            }
            Icon(
                Icons.Default.ChevronRight, null,
                tint = accent.copy(0.6f),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

fun modeIcon(mode: GameMode) = when (mode) {
    GameMode.QUIZ       -> Icons.Default.Quiz
    GameMode.SURVIVAL   -> Icons.Default.Favorite
    GameMode.TIME_ATTACK-> Icons.Default.Timer
    GameMode.CAPITALS   -> Icons.Default.LocationCity
    GameMode.ZEN        -> Icons.Default.SelfImprovement
    GameMode.VOCAL      -> Icons.Default.Mic
    GameMode.DUEL       -> Icons.Default.People
    GameMode.EXPLORER   -> Icons.Default.Explore
    GameMode.VS_AI      -> Icons.Default.SmartToy
}

fun modeColor(mode: GameMode) = when (mode) {
    GameMode.QUIZ       -> SkyBlue
    GameMode.SURVIVAL   -> CoralRed
    GameMode.TIME_ATTACK-> Amber
    GameMode.CAPITALS   -> Emerald
    GameMode.ZEN        -> Indigo
    GameMode.VOCAL      -> MiddleEastColor
    GameMode.DUEL       -> Gold
    GameMode.EXPLORER   -> Color(0xFF00BCD4)
    GameMode.VS_AI      -> Color(0xFF9C27B0)
}

data class LevelInfo(val title: String, val color: Color, val nextThreshold: Int)

fun getLevelInfo(crowns: Int): LevelInfo = when {
    crowns < 500   -> LevelInfo("NOVICE",            MistGray,   500)
    crowns < 1500  -> LevelInfo("EXPLORATEUR",       SkyBlue,    1500)
    crowns < 3500  -> LevelInfo("GÉOGRAPHE",         Emerald,    3500)
    crowns < 7000  -> LevelInfo("CARTOGRAPHE",       Amber,      7000)
    crowns < 15000 -> LevelInfo("EXPERT MONDIAL",    Gold,       15000)
    else           -> LevelInfo("MAÎTRE DES NATIONS",Gold,       Int.MAX_VALUE)
}

fun getLevelProgress(crowns: Int): Float {
    val level = getLevelInfo(crowns)
    val prevThreshold = when {
        crowns < 500   -> 0
        crowns < 1500  -> 500
        crowns < 3500  -> 1500
        crowns < 7000  -> 3500
        crowns < 15000 -> 7000
        else           -> 15000
    }
    if (level.nextThreshold == Int.MAX_VALUE) return 1f
    return ((crowns - prevThreshold).toFloat() / (level.nextThreshold - prevThreshold)).coerceIn(0f, 1f)
}
