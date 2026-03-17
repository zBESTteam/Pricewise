package com.example.dz_3


import android.os.Bundle
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.asPaddingValues
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.content.Context
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.saveable.rememberSaveable


val OrangeGradientStart = Color(0xFFFD9700)
val OrangeGradientEnd = Color(0xFFFF5722)
val MainOrange = Color(0xFFFF6F00) // Цвет кнопки
val LightGrayBg = Color(0xFFF5F5F5) // Цвет фона полей
val GrayText = Color(0xFF757575)

private const val PREFS_NAME = "profile_prefs"
private const val KEY_CITY = "selected_city"

private fun loadCity(context: Context): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getString(KEY_CITY, "Москва") ?: "Москва"
}

private fun saveCity(context: Context, city: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putString(KEY_CITY, city).apply()
}

@Composable
fun CityPickerDialog(
    currentCity: String,
    cities: List<String>,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите город") },
        text = {
            LazyColumn {
                items(cities) { city ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(city) }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = city == currentCity,
                            onClick = { onSelect(city) }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(city)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Закрыть") }
        }
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Scaffod нужен для нижней панели, которая будет везде (кроме редактирования)
    Scaffold(
        bottomBar = {
            // Показываем нижнюю панель только на главных экранах
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        // NavHost управляет тем, какой экран сейчас виден
        NavHost(
            navController = navController,
            startDestination = "profile",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("profile") { ProfileScreen(navController) }
            composable("edit_profile") { EditProfileScreen(navController) }
            composable("history") { HistoryScreen() }
        }
    }
}

// --- ЭКРАН 1: ПРОФИЛЬ (КАК PLAN 1) ---
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current

    val cities = listOf(
        "Москва",
        "Санкт-Петербург",
        "Казань",
        "Новосибирск",
        "Екатеринбург",
        "Нижний Новгород",
        "Ростов-на-Дону"
    )

    var selectedCity by rememberSaveable { mutableStateOf(loadCity(context)) }
    var cityDialogOpen by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Высота шапки + высота статус-бара (чтобы фон реально доходил до самого верха)
        val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        val headerHeight = 200.dp + statusBarTop

        Box(modifier = Modifier.fillMaxWidth().height(headerHeight)) {

            // База цвета — ровно как у кнопки "Сохранить"
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    .background(MainOrange)
            )

            // Лёгкий градиент-оверлей (цвет остаётся "тем же", но есть градиентность)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.10f),
                                Color.Black.copy(alpha = 0.10f)
                            )
                        )
                    )
            )

            // Аватарка, которая "наезжает" на фон
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = 40.dp)
                    .background(Color.White, CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp).align(Alignment.Center),
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp)) // Место под аватарку

        // Имя пользователя
        Text(
            text = "Иванов Иван",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Меню (кнопки)
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            MenuButton(text = "Редактировать профиль", icon = Icons.Outlined.Edit) {
                navController.navigate("edit_profile")
            }
            MenuButton(
                text = "Регион",
                value = selectedCity,
                icon = Icons.Outlined.LocationOn
            ) { cityDialogOpen = true }
            MenuButton(text = "Тема", icon = Icons.Outlined.WbSunny) {}
            MenuButton(text = "Обратиться в поддержку", icon = Icons.Outlined.Chat) {}
        }
        if (cityDialogOpen) {
            CityPickerDialog(
                currentCity = selectedCity,
                cities = cities,
                onSelect = { city ->
                    selectedCity = city
                    saveCity(context, city)
                    cityDialogOpen = false
                },
                onDismiss = { cityDialogOpen = false }
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Text("Выйти из аккаунта?", color = GrayText, modifier = Modifier.padding(bottom = 16.dp))
    }
}

// Компонент кнопки меню для профиля
@Composable
fun MenuButton(text: String, value: String? = null, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = LightGrayBg),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).height(56.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Icon(icon, contentDescription = null, tint = GrayText)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text, color = GrayText, modifier = Modifier.weight(1f))
            if (value != null) {
                Text(value, color = GrayText)
            }
        }
    }
}

// --- ЭКРАН 2: РЕДАКТИРОВАНИЕ (КАК PLAN 2) ---
@Composable
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Шапка с кнопкой назад
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            IconButton(
                onClick = { navController.popBackStack() }, // Вернуться назад
                modifier = Modifier.border(1.dp, Color.Black, RoundedCornerShape(12.dp)).size(40.dp).align(Alignment.CenterStart)
            ) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(16.dp))
            }
            Text(
                "Редактировать профиль",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Аватарка с камерой
        Box(modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 20.dp)) {
            Box(
                modifier = Modifier.size(120.dp).clip(CircleShape).background(Color.LightGray)
                    .border(3.dp, MainOrange, CircleShape)
            ) {
                Icon(Icons.Default.Person, null, modifier = Modifier.size(80.dp).align(Alignment.Center), tint = Color.White)
            }
            // Иконка камеры
            Box(
                modifier = Modifier.align(Alignment.BottomEnd).background(Color.White, CircleShape).padding(4.dp)
            ) {
                Icon(Icons.Outlined.CameraAlt, contentDescription = null, tint = MainOrange)
            }
        }

        // Поля ввода
        Column(modifier = Modifier.padding(horizontal = 16.dp).weight(1f)) {
            SimpleTextField(label = "Фамилия", initialValue = "Иванов")
            Spacer(modifier = Modifier.height(12.dp))
            SimpleTextField(label = "Имя", initialValue = "Иван")

            Spacer(modifier = Modifier.height(24.dp))
            Text("Пароль", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            PasswordTextField(placeholder = "Старый пароль")
            Spacer(modifier = Modifier.height(12.dp))
            PasswordTextField(placeholder = "Новый пароль")
            Spacer(modifier = Modifier.height(12.dp))
            PasswordTextField(placeholder = "Подтвердите новый пароль")
        }

        // Кнопка Сохранить
        Button(
            onClick = {
                Toast.makeText(
                    context,
                    "Изменения успешно сохранены",
                   Toast.LENGTH_SHORT

                ).show()
                navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = MainOrange),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp)
        ) {
            Text("Сохранить", fontSize = 18.sp)
        }
    }
}

// Компонент обычного поля ввода
@Composable
fun SimpleTextField(label: String, initialValue: String) {
    Column {
        Text(label, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = initialValue,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = LightGrayBg,
                focusedContainerColor = LightGrayBg,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = MainOrange
            )
        )
    }
}

// Компонент поля для пароля
@Composable
fun PasswordTextField(placeholder: String) {
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text(placeholder, color = GrayText) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = GrayText) },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null, tint = GrayText)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = LightGrayBg,
            focusedContainerColor = LightGrayBg,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = MainOrange
        )
    )
}

// --- ЭКРАН 3: ИСТОРИЯ ТОВАРОВ
@Composable
fun HistoryScreen() {
    // 1. Создаем  данные для списка
    val historyItems = List(20) { index ->
        HistoryItemData(
            title = "Заказ №100$index",
            date = "25 дек 2023, 14:30",
            price = "${(index + 1) * 1500} ₽",
            status = if (index % 2 == 0) "Доставлен" else "В пути"
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Text(
            "История заказов",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // 2. Сложный список (LazyColumn)
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(historyItems) { item ->
                HistoryItemCard(item)
            }
        }
    }
}

// Модель данных для элемента списка
data class HistoryItemData(val title: String, val date: String, val price: String, val status: String)

// Как выглядит одна карточка в списке
@Composable
fun HistoryItemCard(item: HistoryItemData) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            // Картинка товара (квадратик)
            Box(
                modifier = Modifier.size(60.dp).background(LightGrayBg, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.ShoppingBag, null, tint = MainOrange)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Информация
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, fontWeight = FontWeight.Bold)
                Text(item.date, fontSize = 12.sp, color = GrayText)
                Spacer(modifier = Modifier.height(4.dp))

                // Статус с цветом
                val statusColor = if (item.status == "Доставлен") Color(0xFF4CAF50) else Color(0xFFFF9800)
                Text(item.status, fontSize = 12.sp, color = statusColor, fontWeight = FontWeight.Bold)
            }

            // Цена
            Text(item.price, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

// --- НИЖНЯЯ ПАНЕЛЬ ---
@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Скрываем панель на экране редактирования (как обычно бывает в приложениях)
    if (currentRoute != "edit_profile") {
        NavigationBar(containerColor = Color.White) {
            NavigationBarItem(
                icon = { Icon(Icons.Outlined.Search, null) },
                selected = false,
                onClick = { /* Пока ничего */ }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Outlined.FavoriteBorder, null) },
                label = { Text("История") },
                selected = currentRoute == "history",
                onClick = { navController.navigate("history") }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Outlined.Person, null) },
                label = { Text("Профиль") },
                selected = currentRoute == "profile",
                onClick = { navController.navigate("profile") }
            )
        }
    }
}


@Composable
fun NavController.currentBackStackEntryAsState(): State<NavBackStackEntry?> {
    val currentBackStackEntry = remember { mutableStateOf(currentBackStackEntry) }
    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { controller, _, _ ->
            currentBackStackEntry.value = controller.currentBackStackEntry
        }
        addOnDestinationChangedListener(listener)
        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }
    return currentBackStackEntry
}