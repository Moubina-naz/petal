package com.example.petal.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petal.PetalIcon


val bg = Color(0xFFFf9f7f2)
val black = Color(0xFF2d2d2d)
val teracotta = Color(0xFFd36b54)
val green = Color(0xFF1E3A2F)

@Composable
fun SignupScreen(
    viewModel: AuthViewModel,
    onSignupSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val state           by viewModel.uiState.collectAsState()
    var username        by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible        by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val passwordMismatch = confirmPassword.isNotEmpty() && password != confirmPassword

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
            "Petal",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize   = 48.sp,
                color      = black
            )
        )

        Spacer(Modifier.height(4.dp))

        Text(
            "CREATE YOUR ACCOUNT",
            style = TextStyle(
                fontFamily    = FontFamily.SansSerif,
                fontWeight    = FontWeight.Light,
                fontSize      = 10.sp,
                letterSpacing = 3.sp,
                color         = green.copy(alpha = 0.6f)
            )
        )

        Spacer(Modifier.height(40.dp))

        // ── Username ──
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("USERNAME", style = fieldLabelStyle())
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = username, onValueChange = { username = it },
                placeholder = { Text("yourname", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(4.dp)),
                shape = RoundedCornerShape(4.dp), singleLine = true,
                colors = fieldColors()
            )
        }

        Spacer(Modifier.height(16.dp))

        // ── Email ──
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("EMAIL ADDRESS", style = fieldLabelStyle())
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                placeholder = { Text("hello@petal.app", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(4.dp)),
                shape = RoundedCornerShape(4.dp), singleLine = true,
                colors = fieldColors()
            )
        }

        Spacer(Modifier.height(16.dp))

        // ── Password ──
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("PASSWORD", style = fieldLabelStyle())
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                placeholder = { Text("••••••••", color = Color.Gray, fontSize = 14.sp) },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = green.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(4.dp)),
                shape = RoundedCornerShape(4.dp), singleLine = true,
                colors = fieldColors()
            )
        }

        Spacer(Modifier.height(16.dp))

        // ── Confirm Password ──
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("CONFIRM PASSWORD", style = fieldLabelStyle())
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = confirmPassword, onValueChange = { confirmPassword = it },
                placeholder = { Text("••••••••", color = Color.Gray, fontSize = 14.sp) },
                visualTransformation = if (confirmPasswordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible)
                                Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                            tint = green.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(4.dp)),
                shape = RoundedCornerShape(4.dp), singleLine = true,
                colors = fieldColors()
            )
            if (passwordMismatch) {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Passwords don't match",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize   = 11.sp,
                        color      = Color(0xFFB94040)
                    )
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick  = { if (!passwordMismatch) viewModel.register(username, password, email) },
            enabled  = username.isNotBlank() && email.isNotBlank()
                    && password.isNotBlank() && !passwordMismatch,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape    = RoundedCornerShape(4.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor         = green,
                disabledContainerColor = green.copy(alpha = 0.35f)
            )
        ) {
            Text(
                "SIGN UP",
                style = TextStyle(
                    fontFamily    = FontFamily.SansSerif,
                    fontWeight    = FontWeight.SemiBold,
                    fontSize      = 13.sp,
                    letterSpacing = 2.sp,
                    color         = bg
                )
            )
        }

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = onBackClick) {
            Text(
                "BACK TO LOGIN",
                style = TextStyle(
                    fontFamily    = FontFamily.SansSerif,
                    fontWeight    = FontWeight.Normal,
                    fontSize      = 11.sp,
                    letterSpacing = 1.5.sp,
                    color         = green.copy(alpha = 0.6f)
                )
            )
        }

        when (state) {
            is AuthUiState.Loading -> CircularProgressIndicator(color = green)
            is AuthUiState.Error   ->
                Text((state as AuthUiState.Error).message, color = Color.Red, fontSize = 13.sp)
            is AuthUiState.Success ->
                LaunchedEffect(Unit) { onSignupSuccess() }
            else -> {}
        }
    }
}

@Composable
fun fieldLabelStyle() = TextStyle(
    fontFamily    = FontFamily.SansSerif,
    fontWeight    = FontWeight.SemiBold,
    fontSize      = 10.sp,
    letterSpacing = 1.5.sp,
    color         = green
)

@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor    = green.copy(alpha = 0.35f),
    focusedBorderColor      = green,
    unfocusedContainerColor = Color.White,
    focusedContainerColor   = Color.White
)
