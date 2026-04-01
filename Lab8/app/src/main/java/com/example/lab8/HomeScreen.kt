package com.example.lab8

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth

// Bảng màu cho HomeScreen
val BrandYellow = Color(0xFFEBB233)
val BrandRed = Color(0xFFC1121F)
val HomeTextDark = Color(0xFF1E1E1E)

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandYellow),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "P I Z Z E R I A",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = BrandRed,
            letterSpacing = 4.sp,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.8f),
                    offset = Offset(4f, 4f),
                    blurRadius = 2f
                )
            )
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Delivering\nDeliciousness right\nto your door!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = HomeTextDark,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(16.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.6f))
            )
            Box(
                modifier = Modifier
                    .width(16.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black)
            )
            Box(
                modifier = Modifier
                    .width(16.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.6f))
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 4. Nút START ORDER
        Button(
            onClick = { },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandRed),
            shape = RoundedCornerShape(25.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 2.dp
            )
        ) {
            Text(
                text = "START ORDER",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 5. Nút SignOut
        Button(
            onClick = {
                val firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth.signOut()

                navController.navigate(Screen.Signin.rout) {
                    popUpTo(0)
                }
            },
            modifier = Modifier
                .width(140.dp)
                .height(45.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandRed),
            shape = RoundedCornerShape(22.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 2.dp
            )
        ) {
            Text(
                text = "SignOut",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AsyncImage(
            model = "https://images.unsplash.com/photo-1599813955621-e88df2cc0357?q=80&w=800&auto=format&fit=crop",
            contentDescription = "Delivery Guy",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        )
    }
}