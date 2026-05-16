package com.example.quiz_app_wvssim.ui.screen

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quiz_app_wvssim.data.remote.AiGeneratedQuestion
import com.example.quiz_app_wvssim.ui.components.StarfieldBackground
import com.example.quiz_app_wvssim.ui.theme.*
import com.example.quiz_app_wvssim.ui.viewmodel.LiaViewModel

@Composable
fun LiaScreen(
    viewModel: LiaViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scroll = rememberScrollState()
    val continents = viewModel.getContinents()
    val countries = viewModel.getCountriesByContinent(state.selectedContinent)

    StarfieldBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(Modifier.height(52.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, null, tint = CloudGray)
                }
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        "LIA",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            brush = Brush.horizontalGradient(listOf(Color(0xFF00BCD4), Color(0xFF9C27B0)))
                        ),
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp
                    )
                    Text("Assistante Intelligente", color = MistGray, fontSize = 12.sp, letterSpacing = 1.sp)
                }
                Spacer(Modifier.weight(1f))
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
            }

            // Description card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF00BCD4).copy(0.07f), RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFF00BCD4).copy(0.25f), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text(
                    "Choisis un pays et demande une explication ou un QCM. Les réponses sont rapides et précises !",
                    color = CloudGray,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }

            // Continents selection
            Text("Continent", color = SnowWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.fillMaxWidth())
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(continents) { continent ->
                    LiaChip(
                        label = continent,
                        isSelected = state.selectedContinent == continent,
                        onClick = { viewModel.selectContinent(continent) }
                    )
                }
            }

            // Countries selection
            AnimatedVisibility(
                visible = state.selectedContinent.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Pays", color = SnowWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(countries) { country ->
                            LiaChip(
                                label = country,
                                isSelected = state.selectedCountry == country,
                                onClick = { viewModel.selectCountry(country) },
                                accentColor = Color(0xFF9C27B0)
                            )
                        }
                    }
                }
            }

            // Action buttons (enabled only when country is selected)
            AnimatedVisibility(
                visible = state.selectedCountry.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Explain button
                    Button(
                        onClick = viewModel::requestExplanation,
                        enabled = !state.isLoadingExplanation && !state.isLoadingQcm,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00BCD4).copy(0.15f),
                            contentColor = Color(0xFF00BCD4),
                            disabledContainerColor = Color(0xFF00BCD4).copy(0.05f),
                            disabledContentColor = MistGray
                        ),
                        border = BorderStroke(1.dp, Color(0xFF00BCD4).copy(0.5f))
                    ) {
                        if (state.isLoadingExplanation) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color(0xFF00BCD4), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.AutoAwesome, null, modifier = Modifier.size(16.dp))
                        }
                        Spacer(Modifier.width(6.dp))
                        Text("Expliquer", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    // QCM button
                    Button(
                        onClick = viewModel::generateQcm,
                        enabled = !state.isLoadingExplanation && !state.isLoadingQcm,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF9C27B0).copy(0.15f),
                            contentColor = Color(0xFFCE93D8),
                            disabledContainerColor = Color(0xFF9C27B0).copy(0.05f),
                            disabledContentColor = MistGray
                        ),
                        border = BorderStroke(1.dp, Color(0xFF9C27B0).copy(0.5f))
                    ) {
                        if (state.isLoadingQcm) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color(0xFFCE93D8), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Quiz, null, modifier = Modifier.size(16.dp))
                        }
                        Spacer(Modifier.width(6.dp))
                        Text("QCM", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Explanation result
            AnimatedVisibility(
                visible = state.explanation.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(Color(0xFF00BCD4).copy(0.15f), CircleShape)
                                .border(1.dp, Color(0xFF00BCD4).copy(0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFF00BCD4), modifier = Modifier.size(14.dp))
                        }
                        Spacer(Modifier.width(8.dp))
                        Text("Réponse de LIA", color = Color(0xFF00BCD4), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF00BCD4).copy(0.07f), RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0xFF00BCD4).copy(0.25f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Text(state.explanation, color = CloudGray, fontSize = 14.sp, lineHeight = 21.sp)
                    }
                }
            }

            // QCM result
            AnimatedVisibility(
                visible = state.qcm != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                state.qcm?.let { qcm ->
                    LiaQcmCard(
                        qcm = qcm,
                        selectedAnswer = state.selectedAnswer,
                        onSelectAnswer = viewModel::selectAnswer
                    )
                }
            }

            // QCM error message
            AnimatedVisibility(
                visible = !state.isLoadingQcm && state.qcm == null && state.selectedCountry.isNotEmpty() && state.explanation.isEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CoralRed.copy(0.08f), RoundedCornerShape(14.dp))
                        .border(1.dp, CoralRed.copy(0.3f), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, null, tint = CoralRed, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("QCM indisponible", color = CoralRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Réessaye dans quelques secondes ou vérifie ta clé OpenRouter", color = CloudGray, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun LiaChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    accentColor: Color = Color(0xFF00BCD4)
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) accentColor.copy(0.15f) else GlassCard.copy(0.5f))
            .border(1.dp, if (isSelected) accentColor.copy(0.7f) else MistGray.copy(0.2f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = if (isSelected) SnowWhite else CloudGray,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun LiaQcmCard(
    qcm: AiGeneratedQuestion,
    selectedAnswer: Int?,
    onSelectAnswer: (Int) -> Unit
) {
    val answered = selectedAnswer != null
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Color(0xFF9C27B0).copy(0.15f), CircleShape)
                    .border(1.dp, Color(0xFF9C27B0).copy(0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Quiz, null, tint = Color(0xFFCE93D8), modifier = Modifier.size(14.dp))
            }
            Spacer(Modifier.width(8.dp))
            Text("QCM généré par LIA", color = Color(0xFFCE93D8), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF9C27B0).copy(0.07f), RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFF9C27B0).copy(0.25f), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                qcm.question,
                color = SnowWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 22.sp
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            qcm.options.forEachIndexed { index, option ->
                val isCorrect = index == qcm.correctIndex
                val isSelected = index == selectedAnswer
                val bgColor = when {
                    !answered -> GlassCard.copy(0.7f)
                    isCorrect -> Emerald.copy(0.18f)
                    isSelected -> CoralRed.copy(0.15f)
                    else -> GlassCard.copy(0.4f)
                }
                val borderColor = when {
                    !answered -> MistGray.copy(0.25f)
                    isCorrect -> Emerald
                    isSelected -> CoralRed
                    else -> MistGray.copy(0.15f)
                }
                val textColor = when {
                    !answered -> SnowWhite
                    isCorrect -> Emerald
                    isSelected -> CoralRed
                    else -> MistGray
                }

                Surface(
                    onClick = { onSelectAnswer(index) },
                    enabled = !answered,
                    color = bgColor,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            !answered -> MistGray.copy(0.15f)
                                            isCorrect -> Emerald.copy(0.2f)
                                            isSelected -> CoralRed.copy(0.2f)
                                            else -> MistGray.copy(0.08f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    listOf("A", "B", "C", "D")[index],
                                    color = textColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Text(option, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                        if (answered) {
                            Icon(
                                if (isCorrect) Icons.Default.CheckCircle else if (isSelected) Icons.Default.Cancel else Icons.Default.RadioButtonUnchecked,
                                null,
                                tint = when {
                                    isCorrect -> Emerald
                                    isSelected -> CoralRed
                                    else -> MistGray.copy(0.3f)
                                },
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Explanation after answering
        AnimatedVisibility(
            visible = answered && qcm.explanation.isNotEmpty(),
            enter = fadeIn() + expandVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (selectedAnswer == qcm.correctIndex) Emerald.copy(0.08f) else CoralRed.copy(0.08f),
                        RoundedCornerShape(14.dp)
                    )
                    .border(
                        1.dp,
                        if (selectedAnswer == qcm.correctIndex) Emerald.copy(0.3f) else CoralRed.copy(0.3f),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            if (selectedAnswer == qcm.correctIndex) "✅ Bravo !" else "❌ Pas tout à fait…",
                            color = if (selectedAnswer == qcm.correctIndex) Emerald else CoralRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Text(
                        qcm.explanation,
                        color = CloudGray,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }
            }
        }
    }
}
