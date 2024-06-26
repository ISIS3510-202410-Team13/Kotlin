package com.example.unibites.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


object MainDestinations {
    const val SIGNIN_ROUTE = "signin"
    const val SIGNUP_ROUTE = "signup"
    const val HOME_ROUTE = "home"
    const val SNACK_DETAIL_ROUTE = "snack"
    const val SNACK_ID_KEY = "snackId"
    const val MAP_ROUTE = "map"
    const val PREFERENCES_ROUTE = "preferences"
    const val REVIEW_DETAIL_ROUTE = "review"
}


@Composable
fun rememberUniBitesNavController(
    navController: NavHostController = rememberNavController()
): UniBitesNavController = remember(navController) {
    UniBitesNavController(navController)
}

/**
 * Responsible for holding UI Navigation logic.
 */
@Stable
class UniBitesNavController(
    val navController: NavHostController,
) {

    // ----------------------------------------------------------
    // Navigation state source of truth
    // ----------------------------------------------------------

    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                // Pop up backstack to the first destination and save state. This makes going back
                // to the start destination when pressing back in any other bottom tab.
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    fun navigateToSnackDetail(snackId: String, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.SNACK_DETAIL_ROUTE}/$snackId")
        }
    }

    fun navigateToReviewDetail(restaurantName: String, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.REVIEW_DETAIL_ROUTE}/$restaurantName")
        }
    }

    fun navigateToMapScreen( from: NavBackStackEntry, latitud: Double, longitud: Double) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.MAP_ROUTE}/${latitud.toString()}/${longitud.toString()}")
        }
    }

    fun navigateToHome(from: NavBackStackEntry) {
        navController.navigate(MainDestinations.HOME_ROUTE)
        /**if (from.lifecycleIsResumed()) {

        }**/
    }

    fun navigateToSignUp(from: NavBackStackEntry) {
        navController.navigate(MainDestinations.SIGNUP_ROUTE)
    }

    fun signOut() {
        navController.navigate(MainDestinations.SIGNIN_ROUTE)
    }

    fun navigateToSignIn(from: NavBackStackEntry) {
        navController.navigate(MainDestinations.SIGNIN_ROUTE)
    }

    fun navigateToPreferences(from: NavBackStackEntry) {
        navController.navigate(MainDestinations.PREFERENCES_ROUTE)
    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

/**
 * Copied from similar function in NavigationUI.kt
 *
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:navigation/navigation-ui/src/main/java/androidx/navigation/ui/NavigationUI.kt
 */
private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}

