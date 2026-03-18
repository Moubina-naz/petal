package com.example.petal.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petal.PetalIcon
import com.example.petal.components.ErrorSnackbar

private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
private const val MAX_USERNAME = 30
private const val MAX_EMAIL    = 254
private const val MIN_PASSWORD = 8
private const val MAX_PASSWORD = 128

val bg = Color(0xFFF9F7F2)
val black = Color(0xFF2d2d2d)
val terracotta = Color(0xFFd36b54)
val green = Color(0xFF1E3A2F)
@Composable
fun SignupScreen(
    viewModel: AuthViewModel,
    onSignupSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state) {
        if (state is AuthUiState.Success) onSignupSuccess()
    }

    var username        by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible        by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // only show errors after user has interacted with field
    var emailTouched    by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }

    val isEmailValid     = EMAIL_REGEX.matches(email)
    val emailError       = emailTouched && email.isNotBlank() && !isEmailValid
    val passwordTooShort = passwordTouched && password.isNotBlank() && password.length < MIN_PASSWORD
    val passwordMismatch = confirmPassword.isNotEmpty() && password != confirmPassword

    val isLoading = state is AuthUiState.Loading
    val isButtonEnabled = username.isNotBlank() && email.isNotBlank() && isEmailValid
            && password.isNotBlank() && password.length >= MIN_PASSWORD
            && confirmPassword.isNotBlank() && !passwordMismatch && !isLoading

    val emailFocusRequester           = remember { FocusRequester() }
    val passwordFocusRequester        = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager       = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 28.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(72.dp))
        PetalIcon()
        Spacer(Modifier.height(15.dp))

        Text(
            text          = "Petal",
            color         = black,
            fontSize      = 52.sp,
            fontWeight    = FontWeight.Normal,
            fontFamily    = FontFamily.Serif,
            letterSpacing = 0.sp
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
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("USERNAME", style = fieldLabelStyle())
                Text(
                    "${username.length}/$MAX_USERNAME",
                    style = TextStyle(fontSize = 10.sp, color = Color.Gray, fontFamily = FontFamily.SansSerif)
                )
            }
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = username,
                onValueChange = { if (it.length <= MAX_USERNAME) username = it },
                placeholder   = { Text("yourname", color = Color.Gray, fontSize = 14.sp) },
                modifier      = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(4.dp)),
                shape           = RoundedCornerShape(4.dp),
                singleLine      = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { emailFocusRequester.requestFocus() }),
                colors          = fieldColors()
            )
        }

        Spacer(Modifier.height(16.dp))

        // ── Email ──
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("EMAIL ADDRESS", style = fieldLabelStyle())
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = email,
                onValueChange = {
                    if (it.length <= MAX_EMAIL) {
                        email = it
                        emailTouched = true
                    }
                },
                placeholder   = { Text("hello@petal.app", color = Color.Gray, fontSize = 14.sp) },
                modifier      = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(4.dp))
                    .focusRequester(emailFocusRequester),
                shape           = RoundedCornerShape(4.dp),
                singleLine      = true,
                isError         = emailError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),
                colors          = fieldColors()
            )
            if (emailError) {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Please enter a valid email address",
                    style = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 11.sp, color = Color(0xFFB94040))
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Password ──
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("PASSWORD", style = fieldLabelStyle())
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = password,
                onValueChange = {
                    if (it.length <= MAX_PASSWORD) {
                        password = it
                        passwordTouched = true
                    }
                },
                placeholder          = { Text("Min 8 characters", color = Color.Gray, fontSize = 14.sp) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError              = passwordTooShort,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector        = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide" else "Show",
                            tint               = green.copy(alpha = 0.5f),
                            modifier           = Modifier.size(20.dp)
                        )
                    }
                },
                modifier      = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(4.dp))
                    .focusRequester(passwordFocusRequester),
                shape           = RoundedCornerShape(4.dp),
                singleLine      = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { confirmPasswordFocusRequester.requestFocus() }),
                colors          = fieldColors()
            )
            if (passwordTooShort) {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Password must be at least $MIN_PASSWORD characters",
                    style = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 11.sp, color = Color(0xFFB94040))
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Confirm Password ──
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("CONFIRM PASSWORD", style = fieldLabelStyle())
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value         = confirmPassword,
                onValueChange = { if (it.length <= MAX_PASSWORD) confirmPassword = it },
                placeholder   = { Text("••••••••", color = Color.Gray, fontSize = 14.sp) },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = passwordMismatch,
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector        = if (confirmPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = if (confirmPasswordVisible) "Hide" else "Show",
                            tint               = green.copy(alpha = 0.5f),
                            modifier           = Modifier.size(20.dp)
                        )
                    }
                },
                modifier      = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(4.dp))
                    .focusRequester(confirmPasswordFocusRequester),
                shape           = RoundedCornerShape(4.dp),
                singleLine      = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        if (isButtonEnabled) viewModel.register(username, password, email)
                    }
                ),
                colors = fieldColors()
            )
            if (passwordMismatch) {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Passwords don't match",
                    style = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 11.sp, color = Color(0xFFB94040))
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick  = { viewModel.register(username, password, email) },
            enabled  = isButtonEnabled,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape    = RoundedCornerShape(4.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor         = green,
                disabledContainerColor = green.copy(alpha = 0.35f)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = bg, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
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

        if (state is AuthUiState.Error) {
            ErrorSnackbar(
                message   = (state as AuthUiState.Error).message,
                onDismiss = { viewModel.resetState() }
            )
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