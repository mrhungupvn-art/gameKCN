package com.foodkcn.game.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.foodkcn.game.ui.screens.*
import com.foodkcn.game.viewmodel.GameViewModel

// Mỗi Screen tương ứng 1 module trong sơ đồ kiến trúc game.foodkcn.com
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "KCN", Icons.Filled.Factory)
    object Character : Screen("character", "Nhân vật", Icons.Filled.Person)
    object Career : Screen("career", "Nghề nghiệp", Icons.Filled.Work)
    object Quest : Screen("quest", "Nhiệm vụ", Icons.Filled.Checklist)
    object Skill : Screen("skill", "Kỹ năng", Icons.Filled.Star)
    object House : Screen("house", "Nhà", Icons.Filled.Home)
    object Vehicle : Screen("vehicle", "Xe", Icons.Filled.DirectionsBike)
    object Shop : Screen("shop", "Mua sắm", Icons.Filled.ShoppingCart)
    object Seller : Screen("seller", "Cửa hàng", Icons.Filled.Storefront)
    object Romance : Screen("romance", "Kết duyên", Icons.Filled.Favorite)
    object Reward : Screen("reward", "Phần thưởng", Icons.Filled.CardGiftcard)
}

// Các tab hiển thị dưới thanh điều hướng (Bottom Navigation).
// "Nhân vật" và các phần khác vẫn truy cập được qua các Screen ở trên,
// đây chỉ là những tab chính hay dùng nhất để không làm rối UI.
private val bottomTabs = listOf(
    Screen.Home, Screen.Career, Screen.Quest, Screen.Shop, Screen.Reward
)

@Composable
fun KCNNavGraph(viewModel: GameViewModel, isLoggedIn: Boolean, onLoggedIn: () -> Unit) {
    val navController = rememberNavController()

    if (!isLoggedIn) {
        LoginScreen(viewModel = viewModel, onLoggedIn = onLoggedIn)
        return
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                bottomTabs.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) { HomeScreen(viewModel) }
            composable(Screen.Character.route) { CharacterScreen(viewModel) }
            composable(Screen.Career.route) { CareerScreen(viewModel) }
            composable(Screen.Quest.route) { QuestScreen(viewModel) }
            composable(Screen.Skill.route) { SkillScreen(viewModel) }
            composable(Screen.House.route) { HouseScreen(viewModel) }
            composable(Screen.Vehicle.route) { VehicleScreen(viewModel) }
            composable(Screen.Shop.route) { ShopScreen(viewModel) }
            composable(Screen.Seller.route) { SellerScreen(viewModel) }
            composable(Screen.Romance.route) { RomanceScreen(viewModel) }
            composable(Screen.Reward.route) { RewardScreen(viewModel) }
        }
    }
}
