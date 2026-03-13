package com.example.petal.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petal.DarkGreen
import com.example.petal.PetalIcon

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onSignupClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val bg        = Color(0xFFF9F7F2)
    val black     = Color(0xFF2d2d2d)
    val terracotta = Color(0xFFd36b54)
    val green     = Color(0xFF1E3A2F)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(72.dp))

        PetalIcon()

        Spacer(Modifier.height(15.dp))

        Text(
            text = "Petal",
            color = black,
            fontSize = 52.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.Serif,
            letterSpacing = 0.sp
        )

        Spacer(Modifier.height(52.dp))

        // ── Username ──
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "USERNAME",
                style = TextStyle(
                    fontFamily    = FontFamily.SansSerif,
                    fontWeight    = FontWeight.SemiBold,
                    fontSize      = 10.sp,
                    letterSpacing = 1.5.sp,
                    color         = green
                )
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = email,
                onValueChange = { email = it },
                placeholder   = { Text("John Doe", color = Color.Gray, fontSize = 14.sp) },
                modifier      = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(4.dp)),
                shape      = RoundedCornerShape(4.dp),
                singleLine = true,
                colors     = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor    = green.copy(alpha = 0.35f),
                    focusedBorderColor      = green,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor   = Color.White
                )
            )
        }

        Spacer(Modifier.height(20.dp))

        // ── Password with eye toggle ──
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "PASSWORD",
                style = TextStyle(
                    fontFamily    = FontFamily.SansSerif,
                    fontWeight    = FontWeight.SemiBold,
                    fontSize      = 10.sp,
                    letterSpacing = 1.5.sp,
                    color         = green
                )
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = password,
                onValueChange = { password = it },
                placeholder   = { Text("••••••••", color = Color.Gray, fontSize = 14.sp) },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Outlined.Visibility
                            else
                                Icons.Outlined.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint     = green.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                modifier   = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(4.dp)),
                shape      = RoundedCornerShape(4.dp),
                singleLine = true,
                colors     = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor    = green.copy(alpha = 0.35f),
                    focusedBorderColor      = green,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor   = Color.White
                )
            )
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick  = { viewModel.login(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape  = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = green)
        ) {
            Text(
                "OPEN JOURNAL",
                style = TextStyle(
                    fontFamily    = FontFamily.SansSerif,
                    fontWeight    = FontWeight.SemiBold,
                    fontSize      = 13.sp,
                    letterSpacing = 2.sp,
                    color         = bg
                )
            )
        }

        Spacer(Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment     = Alignment.CenterVertically,
            modifier              = Modifier.padding(bottom = 32.dp)
        ) {
            TextButton(onClick = onSignupClick) {
                Text(
                    "SIGN UP",
                    style = TextStyle(
                        fontFamily     = FontFamily.SansSerif,
                        fontWeight     = FontWeight.SemiBold,
                        fontSize       = 11.sp,
                        letterSpacing  = 1.5.sp,
                        color          = green,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            repeat(3) { i ->
                Box(
                    Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(green.copy(alpha = if (i == 0) 0.6f else 0.2f))
                )
            }
        }

        when (state) {
            is AuthUiState.Loading -> CircularProgressIndicator(color = green)
            is AuthUiState.Error   ->
                Text((state as AuthUiState.Error).message, color = Color.Red, fontSize = 13.sp)
            is AuthUiState.Success ->
                LaunchedEffect(Unit) { onLoginSuccess() }
            else -> {}
        }
    }
}
