package com.example.labgki

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.labgki.model.UserModel
import com.example.labgki.ui.theme.BackgroundPastel
import com.example.labgki.ui.theme.LightBorder
import com.example.labgki.ui.theme.PrimaryPastel
import com.example.labgki.ui.theme.SurfaceWhite
import com.example.labgki.ui.theme.TextDark
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException


//Giao diện thêm người dùng
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(onBack: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("user") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) } //trạng thái chờ
    var message by remember { mutableStateOf("") } //thông báo lỗi
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    //Chọn ảnh từ thư viện ảnh của thiết bị (API)
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()) {
            uri: Uri? -> imageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thêm người dùng mới", fontWeight = FontWeight.Bold, color = TextDark) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundPastel),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(id = android.R.drawable.ic_menu_revert),
                            contentDescription = "Back", tint = TextDark)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(BackgroundPastel).padding(padding),
            contentAlignment = Alignment.TopCenter) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPastel,
                            unfocusedBorderColor = LightBorder,
                            focusedTextColor = TextDark,
                            unfocusedTextColor = TextDark
                        )
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPastel,
                            unfocusedBorderColor = LightBorder,
                            focusedTextColor = TextDark,
                            unfocusedTextColor = TextDark
                        )
                    )
                    OutlinedTextField(
                        value = role,
                        onValueChange = { role = it },
                        label = { Text("Role (admin/user)") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPastel,
                            unfocusedBorderColor = LightBorder,
                            focusedTextColor = TextDark,
                            unfocusedTextColor = TextDark
                        )
                    )

                    //Logic hiển thị và chọn ảnh đại diện
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(bottom = 24.dp)) {
                        if (imageUri != null) {
                            AsyncImage( //thư viện Coil để tải và hiển thị ảnh từ đường dẫn ImageUri
                                model = imageUri,
                                contentDescription = null,
                                modifier = Modifier.size(110.dp).clip(CircleShape).border(3.dp, PrimaryPastel, CircleShape)
                            )
                        } else {
                            Box(modifier = Modifier.size(110.dp).clip(CircleShape).background(BackgroundPastel).border(3.dp, LightBorder, CircleShape),
                                contentAlignment = Alignment.Center) { Text("Ảnh", color = TextDark)
                            }
                        }
                        IconButton(onClick = { launcher.launch("image/*") }, modifier = Modifier.size(36.dp).align(Alignment.BottomEnd), colors = IconButtonDefaults.iconButtonColors(containerColor = PrimaryPastel)) {
                            Icon(painterResource(id = android.R.drawable.ic_menu_camera), contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }

                    Button(
                        onClick = {
                            if (username.isBlank() || password.isBlank() || role.isBlank()) {
                                message = "Vui lòng nhập đầy đủ thông tin!";
                                return@Button
                            }
                            isLoading = true //trạng thái chờ

                            //Logic xử lý ảnh
                            if (imageUri != null) {
                                // Upload ảnh lên Cloudinary
                                uploadToCloudinary(
                                    context = context,
                                    imageUri = imageUri!!,
                                    cloudName = "dp7tq1zns",
                                    uploadPreset = "dp7tq1zns",
                                    //thành công thì cloudinary sẽ trả ve đường link, lưu link ảnh trên firebase
                                    onSuccess = {
                                            uploadedUrl ->
                                        db.collection("users").document(username).set(UserModel(username, password, role, uploadedUrl))
                                            .addOnSuccessListener {
                                                isLoading = false;
                                                onBack() //quay lại màn hình trước đó
                                            }
                                    },
                                    onError = { error ->
                                        isLoading = false; message = error
                                    }
                                )
                            } else {
                                // Không có ảnh thì lưu rỗng
                                db.collection("users").document(username).set(UserModel(username, password, role, ""))
                                    .addOnSuccessListener { isLoading = false; onBack() }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPastel),
                        enabled = !isLoading
                    ) {
                        if (isLoading)
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        else Text("LƯU NGƯỜI DÙNG", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    if (message.isNotEmpty())
                        Text(message,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                }
            }
        }
    }
}

//Giao diện để người dùng chỉnh sửa thông tin cá nhân
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(currentUser: UserModel?, onBack: () -> Unit, onProfileUpdated: (UserModel) -> Unit) {
    if (currentUser == null)
        return
    var password by remember { mutableStateOf(currentUser.password) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? -> imageUri = uri }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sửa Hồ Sơ Cá Nhân", fontWeight = FontWeight.Bold, color = TextDark) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundPastel),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(id = android.R.drawable.ic_menu_revert),
                            contentDescription = "Back", tint = TextDark)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(BackgroundPastel).padding(padding),
            contentAlignment = Alignment.TopCenter)
        {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            )
            {
                Column(modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Box(contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(bottom = 24.dp))
                    {
                        if (imageUri != null) {
                            AsyncImage(model = imageUri,
                                contentDescription = null,
                                modifier = Modifier.size(110.dp).clip(CircleShape).border(3.dp,
                                    PrimaryPastel, CircleShape))
                        } else if (currentUser.file.isNotEmpty()) {
                            AsyncImage(model = currentUser.file,
                                contentDescription = null,
                                modifier = Modifier.size(110.dp).clip(CircleShape).border(3.dp,
                                    PrimaryPastel, CircleShape))
                        } else {
                            Box(modifier = Modifier.size(110.dp).clip(CircleShape).background(BackgroundPastel).border(3.dp,
                                LightBorder, CircleShape),
                                contentAlignment = Alignment.Center) {
                                Text("Ảnh", color = TextDark)
                            }
                        }
                        IconButton(onClick = {
                            launcher.launch("image/*") },
                            modifier = Modifier.size(36.dp).align(Alignment.BottomEnd),
                            colors = IconButtonDefaults.iconButtonColors(containerColor = PrimaryPastel)) {
                            Icon(painterResource(id = android.R.drawable.ic_menu_camera),
                                contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }

                    OutlinedTextField(
                        value = currentUser.username,
                        onValueChange = { },
                        label = { Text("Username") },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Gray,
                            disabledBorderColor = LightBorder,
                            disabledLabelColor = Color.Gray
                        )
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password Mới") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPastel,
                            unfocusedBorderColor = LightBorder,
                            focusedTextColor = TextDark,
                            unfocusedTextColor = TextDark
                        )
                    )

                    Button(
                        onClick = {
                            if (password.isBlank()) {
                                message = "Mật khẩu không được để trống!";
                                return@Button }
                            isLoading = true

                            if (imageUri != null) {
                                // Upload ảnh mới lên Cloudinary
                                uploadToCloudinary(
                                    context = context,
                                    imageUri = imageUri!!,
                                    cloudName = "dp7tq1zns",
                                    uploadPreset = "dp7tq1zns",
                                    onSuccess = { uploadedUrl ->
                                        val updatedUser = UserModel(currentUser.username, password, currentUser.role, uploadedUrl)
                                        db.collection("users").document(currentUser.username).set(updatedUser).addOnSuccessListener {
                                            isLoading = false
                                            onProfileUpdated(updatedUser)
                                            onBack()
                                        }
                                    },
                                    onError = { error ->
                                        isLoading = false; message = error
                                    }
                                )
                            } else {
                                // Giữ nguyên ảnh cũ
                                val updatedUser = UserModel(currentUser.username, password, currentUser.role, currentUser.file)
                                db.collection("users").document(currentUser.username).set(updatedUser).addOnSuccessListener {
                                    isLoading = false
                                    onProfileUpdated(updatedUser)
                                    onBack()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPastel),
                        enabled = !isLoading
                    ) {
                        if (isLoading)
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        else
                            Text("CẬP NHẬT", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    if (message.isNotEmpty())
                        Text(message, color = Color.Red, modifier = Modifier.padding(top = 12.dp))
                }
            }
        }
    }
}

//đẩy file ảnh từ điện thoại lên Cloudinary
fun uploadToCloudinary(
    context: android.content.Context,
    imageUri: android.net.Uri,
    cloudName: String,
    uploadPreset: String,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    // Đọc thẳng toàn bộ dữ liệu gốc của ảnh thành mảng byte
    val bytes = context.contentResolver.openInputStream(imageUri)?.use { it.readBytes() }

    if (bytes == null) {
        onError("Không thể đọc ảnh")
        return
    }

    //Chuẩn bị dữ lieu gửi đi
    val client = OkHttpClient() //tạo kết nối mạng
    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM) //định dạng chuẩn để gửi dữ lieu dạng form
        .addFormDataPart( //đưa dữ liệu ảnh vào "file"
            "file",
            "avatar.jpg",
            bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
        )
        .addFormDataPart("upload_preset", uploadPreset)
        .build()

    //Gửi request
    val request = Request.Builder()
        //Tạo POST request gửi đến URL API của Cloudinary
        .url("https://api.cloudinary.com/v1_1/$cloudName/image/upload")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onError("Lỗi mạng: ${e.message}")
        }

        //Xu lý kết quả trả ve
        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseData = response.body?.string()
                if (responseData != null) { //thành công hàm nhan về 1 chuỗi JSON
                    try {
                        val json = JSONObject(responseData)
                        val url = json.getString("secure_url") //đường link ảnh bắt đầu bằng https:// cần để lưu vào database
                        onSuccess(url)
                    } catch (e: Exception) {
                        onError("Lỗi đọc dữ liệu Cloudinary")
                    }
                }
            } else {
                onError("Lỗi Cloudinary: Vui lòng kiểm tra lại Cloud Name và Upload Preset!")
            }
        }
    })
}

// Giao diện để Admin chỉnh sửa thông tin người dùng
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditUserScreen(userToEdit: UserModel?, onBack: () -> Unit) {
    if (userToEdit == null)
        return

    var password by remember { mutableStateOf(userToEdit.password) }
    var role by remember { mutableStateOf(userToEdit.role) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? -> imageUri = uri }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sửa Người Dùng", fontWeight = FontWeight.Bold, color = TextDark) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundPastel),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(id = android.R.drawable.ic_menu_revert),
                            contentDescription = "Back", tint = TextDark)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(BackgroundPastel).padding(padding),
            contentAlignment = Alignment.TopCenter)
        {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            )
            {
                Column(modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Box(contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(bottom = 24.dp))
                    {
                        if (imageUri != null) {
                            AsyncImage(model = imageUri,
                                contentDescription = null,
                                modifier = Modifier.size(110.dp).clip(CircleShape).border(3.dp,
                                    PrimaryPastel, CircleShape))
                        } else if (userToEdit.file.isNotEmpty()) {
                            AsyncImage(model = userToEdit.file,
                                contentDescription = null,
                                modifier = Modifier.size(110.dp).clip(CircleShape).border(3.dp,
                                    PrimaryPastel, CircleShape))
                        } else {
                            Box(modifier = Modifier.size(110.dp).clip(CircleShape).background(BackgroundPastel).border(3.dp,
                                LightBorder, CircleShape),
                                contentAlignment = Alignment.Center) {
                                Text("Ảnh", color = TextDark)
                            }
                        }
                        IconButton(onClick = {
                            launcher.launch("image/*") },
                            modifier = Modifier.size(36.dp).align(Alignment.BottomEnd),
                            colors = IconButtonDefaults.iconButtonColors(containerColor = PrimaryPastel)) {
                            Icon(painterResource(id = android.R.drawable.ic_menu_camera),
                                contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }

                    OutlinedTextField(
                        value = userToEdit.username,
                        onValueChange = { },
                        label = { Text("Username") },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Gray,
                            disabledBorderColor = LightBorder,
                            disabledLabelColor = Color.Gray
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPastel,
                            unfocusedBorderColor = LightBorder,
                            focusedTextColor = TextDark,
                            unfocusedTextColor = TextDark
                        )
                    )

                    OutlinedTextField(
                        value = role,
                        onValueChange = { role = it },
                        label = { Text("Role (admin/user)") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPastel,
                            unfocusedBorderColor = LightBorder,
                            focusedTextColor = TextDark,
                            unfocusedTextColor = TextDark
                        )
                    )

                    Button(
                        onClick = {
                            if (password.isBlank() || role.isBlank()) {
                                message = "Thông tin không được để trống!"
                                return@Button
                            }
                            isLoading = true

                            if (imageUri != null) {
                                uploadToCloudinary(
                                    context = context,
                                    imageUri = imageUri!!,
                                    cloudName = "dp7tq1zns",
                                    uploadPreset = "dp7tq1zns",
                                    onSuccess = { uploadedUrl ->
                                        val updatedUser = UserModel(userToEdit.username, password, role, uploadedUrl)
                                        db.collection("users").document(userToEdit.username).set(updatedUser).addOnSuccessListener {
                                            isLoading = false
                                            onBack()
                                        }
                                    },
                                    onError = { error ->
                                        isLoading = false; message = error
                                    }
                                )
                            } else {
                                val updatedUser = UserModel(userToEdit.username, password, role, userToEdit.file)
                                db.collection("users").document(userToEdit.username).set(updatedUser).addOnSuccessListener {
                                    isLoading = false
                                    onBack()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPastel),
                        enabled = !isLoading
                    ) {
                        if (isLoading)
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        else
                            Text("CẬP NHẬT NGƯỜI DÙNG", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    if (message.isNotEmpty())
                        Text(message, color = Color.Red, modifier = Modifier.padding(top = 12.dp))
                }
            }
        }
    }
}