package com.example.fakeshopping

import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fakeshopping.data.userdatabase.UserOrders
import com.example.fakeshopping.ui.presentation.*
import com.example.fakeshopping.ui.presentation.homscreen.HomeScreen
import com.example.fakeshopping.ui.presentation.login.LoginScreenNavigation
import com.example.fakeshopping.ui.presentation.myorders.UserOrdersScreen
import com.example.fakeshopping.ui.presentation.myprofile.MyProfileScreen
import com.example.fakeshopping.ui.presentation.order_checkout.ProductCheckoutScreen
import com.example.fakeshopping.utils.Routes

@Composable
fun Navigation(window: Window, onLoggedStateChanged:(userId:String?)->Unit, getCurrentLoggedUser:()->String?, onContinueToPayment:(paymentOptionRoute:String, amountToPaid:Float, itemsToBuy:List<Int>, itemsToBuyQuantity:List<Int>)->Unit ) {

    val navController = rememberNavController()
    var currentUser = getCurrentLoggedUser()

    MainActivity.MAINACTIVITY_navController = navController

    NavHost(
        navController = navController,
        startDestination = if(currentUser.isNullOrEmpty()) Routes.loginSignupScreen else "${Routes.homeScreen}?category={category}"
    ) {

        composable(
            route = Routes.loginSignupScreen,
        ){
            window.statusBarColor = Color(0xFF350099).toArgb()
            LoginScreenNavigation(rootnavController = navController, onLoggedStateChanged= onLoggedStateChanged)
        }

        composable(
            route = Routes.homeScreen+"?category={category}",
            arguments = listOf(
                navArgument("category") {
                    type = NavType.StringType
                    defaultValue = "All"
                }

            )
        ) { backStackEntry ->

            window.statusBarColor = Color(0xFFF8F8F8).toArgb()
            currentUser = getCurrentLoggedUser()
            HomeScreen(
                getCurrentUser = { getCurrentLoggedUser()!! },
                rootNavController = navController,
                category = backStackEntry.arguments?.getString("category")!!,
                window = window
            )
        }

        composable(
            route = Routes.productDetailScreen + "/{productId}",
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            window.statusBarColor = Color.White.toArgb()
            ProductDetailScreen(navController, backStackEntry.arguments?.getInt("productId")!!, currentUser = currentUser!!)

        }

        composable(
            route = Routes.shoppingCartScreen,
        ){
            window.statusBarColor = Color.White.toArgb()
            ShopingCartScreen(navController = navController, currentUserId = currentUser!!)
        }

        composable(
            route = Routes.favouritesScreen,
        ){
            window.statusBarColor = Color.White.toArgb()
            FavouritesScreen(navController = navController, currentUser = currentUser!!)
        }

        composable(
            route = "${Routes.checkOutOverviewScreen}/{itemsToBuy}/{itemsToBuyQuantity}",
            arguments = listOf(
                navArgument("itemsToBuy"){
                    type = NavType.StringType
                },

                navArgument("itemsToBuyQuantity"){
                    type = NavType.StringType
                }
            )
        ) {
            window.statusBarColor = Color.White.toArgb()
            ProductCheckoutScreen(
                navController = navController,
                currentUser = currentUser!!,
                selectedProductIds = it.arguments!!.getString("itemsToBuy")!!,
                selectedProductQuantity = it.arguments!!.getString("itemsToBuyQuantity")!!,
                onContinueToPayment = { paymentRoute, amountToBePaid, itemsTobuy->
                    onContinueToPayment(
                        paymentRoute, amountToBePaid,
                        itemsTobuy.keys.toList(),
                        itemsTobuy.values.toList()  )
                })
        }

        composable(
            route = Routes.myProfileScreen,
        ){
            window.statusBarColor = Color.White.toArgb()
            MyProfileScreen(
                rootNavController = navController,
                currentUser = currentUser!!,
                onLoggedStateChanged= onLoggedStateChanged
            )
        }

        composable(
            route = Routes.userOrders,
        ){
            window.statusBarColor = Color.White.toArgb()
            UserOrdersScreen(currentUserId = currentUser!!)
        }

    }

}