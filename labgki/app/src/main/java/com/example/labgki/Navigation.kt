package com.example.labgki

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.labgki.model.UserModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var currentUser by remember { mutableStateOf<UserModel?>(null) }

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                onLoginSuccess = { _, user ->
                    currentUser = user // Lưu lại user
                    navController.navigate("home") { popUpTo("login") { inclusive = true } }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(onNavigateToLogin = { navController.popBackStack() })
        }

        composable("home") {
            HomeScreen(
                currentUser = currentUser,
                onLogout = {
                    currentUser = null
                    navController.navigate("login") { popUpTo(0) }
                },
                onNavigateToAddUser = { navController.navigate("add_user") },
                onNavigateToEditProfile = { navController.navigate("edit_profile") }
            )
        }

        composable("add_user") {
            AddUserScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("edit_profile") {
            EditProfileScreen(
                currentUser = currentUser,
                onBack = { navController.popBackStack() },
                onProfileUpdated = { updatedUser ->
                    currentUser = updatedUser // Cập nhật lại state sau khi sửa
                }
            )
        }
    }
}

@Composable
fun HomeScreen(
    currentUser: UserModel?,
    onLogout: () -> Unit,
    onNavigateToAddUser: () -> Unit,
    onNavigateToEditProfile: () -> Unit
) {
    val isAdmin = currentUser?.role?.lowercase() == "admin"

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (isAdmin) "ADMIN" else "USER", style = MaterialTheme.typography.titleLarge)
            Button(onClick = onLogout) { Text("Đăng xuất") }
        }

        HorizontalDivider()

        if (isAdmin) {
            Button(
                onClick = onNavigateToAddUser,
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                Text("+ thêm người dùng mới")
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            UserList(
                currentUser = currentUser,
                onEditClick = { onNavigateToEditProfile() }
            )
        }
    }
}