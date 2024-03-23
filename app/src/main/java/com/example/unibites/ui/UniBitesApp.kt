package com.example.unibites.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.unibites.ui.home.HomeSections
import com.example.unibites.ui.home.addHomeGraph
import com.example.unibites.ui.navigation.MainDestinations
import com.example.unibites.ui.navigation.rememberUniBitesNavController
import com.example.unibites.ui.snackdetail.SnackDetail
import com.example.unibites.ui.theme.UniBitesTheme

@Composable
fun UniBitesApp() {
    UniBitesTheme {
        val unibitesNavController = rememberUniBitesNavController()
        NavHost(
            navController = unibitesNavController.navController,
            startDestination = MainDestinations.HOME_ROUTE
        ) {
            unibitesNavGraph(
                onSnackSelected = unibitesNavController::navigateToSnackDetail,
                upPress = unibitesNavController::upPress,
                onNavigateToRoute = unibitesNavController::navigateToBottomBarRoute
            )
        }
    }
}

private fun NavGraphBuilder.unibitesNavGraph(
    onSnackSelected: (Long, NavBackStackEntry) -> Unit,
    upPress: () -> Unit,
    onNavigateToRoute: (String) -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.FEED.route
    ) {
        addHomeGraph(onSnackSelected, onNavigateToRoute)
    }
    composable(
        "${MainDestinations.SNACK_DETAIL_ROUTE}/{${MainDestinations.SNACK_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.SNACK_ID_KEY) { type = NavType.LongType })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val snackId = arguments.getLong(MainDestinations.SNACK_ID_KEY)
        SnackDetail(snackId, upPress)
    }
}

