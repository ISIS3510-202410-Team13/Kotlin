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
import com.example.unibites.SignIn.repository.SignInViewModel
import com.example.unibites.SignIn.ui.SignInDetail
import com.example.unibites.maps.ui.MyUniMap
import com.example.unibites.signup.repository.SignUpViewModel
import com.example.unibites.signup.ui.SignUpDetail

import com.example.unibites.ui.home.HomeSections
import com.example.unibites.ui.home.addHomeGraph
import com.example.unibites.ui.navigation.MainDestinations
import com.example.unibites.ui.navigation.rememberUniBitesNavController
import com.example.unibites.ui.theme.UniBitesTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun UniBitesApp(auth: FirebaseAuth) {
    UniBitesTheme {
        val unibitesNavController = rememberUniBitesNavController()
        val currentUser = auth.currentUser
        NavHost(
            navController = unibitesNavController.navController,
            startDestination = if(currentUser != null) MainDestinations.HOME_ROUTE else MainDestinations.SIGNUP_ROUTE
        ) {
            unibitesNavGraph(
                onSnackSelected = unibitesNavController::navigateToSnackDetail,
                upPress = unibitesNavController::upPress,
                onNavigateToRoute = unibitesNavController::navigateToBottomBarRoute,
                onNavigateMap = unibitesNavController::navigateToMapScreen,
                onNavigateHome = unibitesNavController::navigateToHome,
                onSignOut = unibitesNavController::signOut
            )
        }
    }
}

private fun NavGraphBuilder.unibitesNavGraph(
    onSnackSelected: (String, NavBackStackEntry) -> Unit,
    upPress: () -> Unit,
    onNavigateToRoute: (String) -> Unit,
    onNavigateMap: (NavBackStackEntry, Double, Double) -> Unit,
    onNavigateHome: (NavBackStackEntry) -> Unit,
    onSignOut: () -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.FEED.route
    ) {
        addHomeGraph(onSnackSelected, onNavigateToRoute, upPress= upPress, onNavigateMap = onNavigateMap, onSignOut = {
            onSignOut()
        })
    }

    composable(route= "${MainDestinations.MAP_ROUTE}/{latitud}/{longitud}", arguments = listOf(  navArgument("latitud") { type = NavType.StringType }, navArgument("longitud") { type = NavType.StringType })){

        val latitud = it.arguments?.getString("latitud")?.toDouble() ?: 0.0
        val longitud = it.arguments?.getString("longitud")?.toDouble() ?: 0.0
        MyUniMap(latitud, longitud)
    }

    composable(route= MainDestinations.SIGNIN_ROUTE){ navBackStackEntry ->
        val viewModel = viewModel<SignInViewModel>()
        SignInDetail(navBackStackEntry,viewModel, onNavigateHome) {
            viewModel.signIn("fancy@yopmail.com", "juniorcampeon", onSuccessSignIn = {onNavigateHome(navBackStackEntry)}, onErrorSignIn = {})
        }
    }
    composable(route = MainDestinations.SIGNUP_ROUTE) { navBackStackEntry ->
        val viewModel = viewModel<SignUpViewModel>()
        SignUpDetail(navBackStackEntry, viewModel, onNavigateHome)
    }

}

