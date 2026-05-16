package com.example.quiz_app_wvssim

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quiz_app_wvssim.domain.model.QuizCategory
import com.example.quiz_app_wvssim.ui.navigation.QuizRoutes
import com.example.quiz_app_wvssim.ui.screen.DuelScreen
import com.example.quiz_app_wvssim.ui.screen.HomeScreen
import com.example.quiz_app_wvssim.ui.screen.LeaderboardScreen
import com.example.quiz_app_wvssim.ui.screen.LiaScreen
import com.example.quiz_app_wvssim.ui.screen.QuizScreen
import com.example.quiz_app_wvssim.ui.screen.ResultScreen
import com.example.quiz_app_wvssim.ui.theme.QuizAppTheme
import com.example.quiz_app_wvssim.ui.viewmodel.AppViewModelFactory
import com.example.quiz_app_wvssim.ui.viewmodel.LiaViewModel
import com.example.quiz_app_wvssim.ui.viewmodel.DuelViewModel
import com.example.quiz_app_wvssim.ui.viewmodel.GameMode
import com.example.quiz_app_wvssim.ui.viewmodel.HomeViewModel
import com.example.quiz_app_wvssim.ui.viewmodel.LeaderboardViewModel
import com.example.quiz_app_wvssim.ui.viewmodel.QuizViewModel

@Composable
fun QuizApp() {
    QuizAppTheme {
        val navController = rememberNavController()
        val context = LocalContext.current.applicationContext as QuizApplication
        val factory = remember {
            AppViewModelFactory(
                context.container.quizRepository,
                context.container.userPreferencesRepository,
                context.container.openRouter
            )
        }

        val homeViewModel: HomeViewModel = viewModel(factory = factory)
        val userPrefs by homeViewModel.userPreferences.collectAsStateWithLifecycle()

        NavHost(navController = navController, startDestination = QuizRoutes.HOME) {
            composable(QuizRoutes.HOME) {
                HomeScreen(
                    viewModel = homeViewModel,
                    onCategorySelected = { category, mode ->
                        if (mode == GameMode.DUEL) {
                            navController.navigate(QuizRoutes.duel(category))
                        } else {
                            navController.navigate(QuizRoutes.quiz(category) + "?mode=${mode.name}")
                        }
                    },
                    onLiaClick = { navController.navigate(QuizRoutes.LIA) }
                )
            }

            composable(QuizRoutes.LIA) {
                val liaViewModel: LiaViewModel = viewModel(factory = factory)
                LiaScreen(
                    viewModel = liaViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = QuizRoutes.QUIZ + "?mode={mode}",
                arguments = listOf(
                    navArgument("category") { type = NavType.StringType },
                    navArgument("mode") {
                        type = NavType.StringType
                        defaultValue = GameMode.QUIZ.name
                    }
                )
            ) { backStackEntry ->
                val category = QuizCategory.fromRaw(backStackEntry.arguments?.getString("category").orEmpty())
                val modeName = backStackEntry.arguments?.getString("mode") ?: GameMode.QUIZ.name
                val mode = try { GameMode.valueOf(modeName) } catch (e: Exception) { GameMode.QUIZ }

                val quizViewModel: QuizViewModel = viewModel(factory = factory)

                QuizScreen(
                    category = category,
                    mode = mode,
                    viewModel = quizViewModel,
                    onBackPressed = {
                        navController.popBackStack(QuizRoutes.HOME, false)
                    },
                    onFinished = { score, correct, total, crowns ->
                        navController.navigate(QuizRoutes.result(category, score, correct, total, crowns)) {
                            popUpTo(QuizRoutes.HOME)
                        }
                    }
                )
            }

            composable(
                route = QuizRoutes.RESULT,
                arguments = listOf(
                    navArgument("category") { type = NavType.StringType },
                    navArgument("score") { type = NavType.IntType },
                    navArgument("correct") { type = NavType.IntType },
                    navArgument("total") { type = NavType.IntType },
                    navArgument("crowns") { type = NavType.IntType; defaultValue = 0 }
                )
            ) { backStackEntry ->
                val category = QuizCategory.fromRaw(backStackEntry.arguments?.getString("category").orEmpty())
                val score = backStackEntry.arguments?.getInt("score") ?: 0
                val correct = backStackEntry.arguments?.getInt("correct") ?: 0
                val total = backStackEntry.arguments?.getInt("total") ?: 0
                val crowns = backStackEntry.arguments?.getInt("crowns") ?: 0
                val leaderboardViewModel: LeaderboardViewModel = viewModel(factory = factory)

                ResultScreen(
                    category = category,
                    score = score,
                    correct = correct,
                    total = total,
                    earnedCrowns = crowns,
                    userName = userPrefs.userName,
                    leaderboardViewModel = leaderboardViewModel,
                    onReplay = { navController.navigate(QuizRoutes.quiz(category)) },
                    onLeaderboard = { navController.navigate(QuizRoutes.leaderboard(category)) }
                )
            }

            composable(
                route = QuizRoutes.LEADERBOARD,
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) { backStackEntry ->
                val category = QuizCategory.fromRaw(backStackEntry.arguments?.getString("category").orEmpty())
                val leaderboardViewModel: LeaderboardViewModel = viewModel(factory = factory)
                LeaderboardScreen(
                    category = category,
                    viewModel = leaderboardViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = QuizRoutes.DUEL,
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) { backStackEntry ->
                val category = QuizCategory.fromRaw(backStackEntry.arguments?.getString("category").orEmpty())
                val duelViewModel: DuelViewModel = viewModel(factory = factory)
                DuelScreen(
                    category = category,
                    userName = userPrefs.userName,
                    viewModel = duelViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
