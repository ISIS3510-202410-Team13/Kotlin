package com.example.unibites.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.unibites.Signup.repository.SignUpViewModel
import com.example.unibites.Signup.ui.SignupDetail
import com.example.unibites.maps.ui.MyUniMap
import com.example.unibites.ui.home.HomeSections
import com.example.unibites.ui.home.addHomeGraph
import com.example.unibites.ui.navigation.MainDestinations
import com.example.unibites.ui.navigation.rememberUniBitesNavController
import com.example.unibites.ui.theme.UniBitesTheme

@Composable
fun UniBitesApp() {
    UniBitesTheme {
        val unibitesNavController = rememberUniBitesNavController()
        NavHost(
            navController = unibitesNavController.navController,
            startDestination = MainDestinations.SIGUNP_ROUTE
        ) {
            unibitesNavGraph(
                onSnackSelected = unibitesNavController::navigateToSnackDetail,
                upPress = unibitesNavController::upPress,
                onNavigateToRoute = unibitesNavController::navigateToBottomBarRoute,
                onNavigateMap = unibitesNavController::navigateToMapScreen,
                onNavigateHome = unibitesNavController::navigateToHome
            )
        }
    }
}

private fun NavGraphBuilder.unibitesNavGraph(
    onSnackSelected: (String, NavBackStackEntry) -> Unit,
    upPress: () -> Unit,
    onNavigateToRoute: (String) -> Unit,
    onNavigateMap: (NavBackStackEntry, Double, Double) -> Unit,
    onNavigateHome: (NavBackStackEntry) -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.FEED.route
    ) {
        addHomeGraph(onSnackSelected, onNavigateToRoute, upPress= upPress, onNavigateMap = onNavigateMap)
    }

    composable(route= "${MainDestinations.MAP_ROUTE}/{latitud}/{longitud}", arguments = listOf(  navArgument("latitud") { type = NavType.StringType }, navArgument("longitud") { type = NavType.StringType })){

        val latitud = it.arguments?.getString("latitud")?.toDouble() ?: 0.0
        val longitud = it.arguments?.getString("longitud")?.toDouble() ?: 0.0
        MyUniMap(latitud, longitud)
    }

    composable(route= MainDestinations.SIGUNP_ROUTE){navBackStackEntry ->
        val viewModel = viewModel<SignUpViewModel>()
        SignupDetail(navBackStackEntry,viewModel, onNavigateHome) {
            viewModel.signIn("fancy@yopmail.com", "juniorcampeon", onSuccessSignIn = {onNavigateHome(navBackStackEntry)}, onErrorSignIn = {})
        }
    }
}

