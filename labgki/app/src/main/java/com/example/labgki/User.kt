package com.example.labgki

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.labgki.model.UserModel
import com.example.labgki.ui.theme.AccentOrange
import com.example.labgki.ui.theme.BackgroundPastel
import com.example.labgki.ui.theme.DangerRed
import com.example.labgki.ui.theme.LightBorder
import com.example.labgki.ui.theme.PrimaryPastel
import com.example.labgki.ui.theme.SurfaceWhite
import com.example.labgki.ui.theme.TextDark
import com.example.labgki.ui.theme.TextGray
import com.google.firebase.firestore.FirebaseFirestore


//Hiển thị danh sách tất cả người dùng
@Composable
fun UserList(currentUser: UserModel?, onEditClick: () -> Unit) {
    var userList by remember { mutableStateOf(listOf<UserModel>()) }
    var searchQuery by remember { mutableStateOf("") } // Biến lưu từ khóa tìm kiếm
    val db = FirebaseFirestore.getInstance()

    //tu dong cap nhat lai database
    LaunchedEffect(Unit) {
        db.collection("users").addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                userList = snapshot.documents.mapNotNull {
                    it.toObject(UserModel::class.java)
                }
            }
        }
    }

    // BỘ LỌC TÌM KIẾM
    val filteredList = userList.filter {
        it.username.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundPastel)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Tìm kiếm người dùng", color = TextGray) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryPastel,
                unfocusedBorderColor = LightBorder,
                focusedContainerColor = SurfaceWhite,
                unfocusedContainerColor = SurfaceWhite
            ),
            leadingIcon = { Icon(painterResource(id = android.R.drawable.ic_menu_search), contentDescription = null, tint = PrimaryPastel) }
        )

        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            items(filteredList.size) { index ->
                val user = filteredList[index]
                val isAdmin = currentUser?.role?.lowercase() == "admin"
                val isMe = currentUser?.username == user.username

                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (user.file.isNotEmpty()) {
                            AsyncImage(
                                model = user.file, contentDescription = "Avatar",
                                modifier = Modifier.size(60.dp).clip(CircleShape).border(1.5.dp, PrimaryPastel, CircleShape)
                            )
                        } else {
                            Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(PrimaryPastel.copy(alpha=0.1f)).border(1.5.dp, PrimaryPastel, CircleShape), contentAlignment = Alignment.Center) {
                                Text(user.username.take(1).uppercase(), color = PrimaryPastel, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(user.username, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextDark)
                            Card(
                                shape = RoundedCornerShape(6.dp), colors = CardDefaults.cardColors(containerColor = if (user.role.lowercase() == "admin") AccentOrange.copy(alpha = 0.2f) else PrimaryPastel.copy(alpha = 0.1f)), modifier = Modifier.padding(top = 6.dp)
                            ) {
                                Text(user.role.uppercase(), style = MaterialTheme.typography.bodySmall, color = if (user.role.lowercase() == "admin") AccentOrange else PrimaryPastel, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.Bold)
                            }
                        }

                        Row {
                            if (isMe) {
                                IconButton(
                                    onClick = onEditClick,
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = PrimaryPastel.copy(alpha = 0.1f)),
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Icon(painterResource(id = android.R.drawable.ic_menu_edit), contentDescription = "Sửa", tint = PrimaryPastel, modifier = Modifier.size(20.dp))
                                }
                            }

                            if (isAdmin && !isMe) {
                                IconButton(
                                    onClick = { db.collection("users").document(user.username).delete() },
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = DangerRed.copy(alpha = 0.1f))
                                ) {
                                    Icon(painterResource(id = android.R.drawable.ic_menu_delete), contentDescription = "Xóa", tint = DangerRed, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}