package com.example.petal.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petal.NavigationEvent

@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel,
    onBack: () -> Unit,
    onChangePassword: () -> Unit
) {

    val state by viewModel.uiState.collectAsState()

    val accent = Color(0xFF8B5E3C)
    val background = Color(0xFFF7F3EE)
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)

    ) {

        Spacer(Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBackIosNew, contentDescription = null)
            }

            Spacer(Modifier.width(8.dp))

            Text(
                "Edit Profile",
                fontSize = 22.sp,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.weight(1f))
        }

        Spacer(Modifier.height(10.dp))

        Text(
            "Manage your private account details ",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(28.dp))

        FieldLabel("USERNAME")
        ProfileTextField(
            label = "",
            value = state.username,
            onValueChange = viewModel::onUsernameChange
        )

        Spacer(Modifier.height(20.dp))

        FieldLabel("EMAIL ADDRESS")
        ProfileTextField(
            label = "",
            value = state.email,
            onValueChange = viewModel::onEmailChange
        )

        Spacer(Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Column(modifier = Modifier.weight(1f)) {

                FieldLabel("FIRST NAME")

                ProfileTextField(
                    label = "",
                    value = state.firstName,
                    onValueChange = viewModel::onFirstNameChange
                )
            }

            Column(modifier = Modifier.weight(1f)) {

                FieldLabel("LAST NAME")

                ProfileTextField(
                    label = "",
                    value = state.lastName,
                    onValueChange = viewModel::onLastNameChange
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(0.dp),
            onClick = { onChangePassword() }
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "Change Password",
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    modifier = Modifier.rotate(180f)
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center

        ){
            Button(
                onClick = viewModel::saveProfile,
                enabled = !state.isSavingProfile,
                modifier = Modifier
                    .wrapContentSize()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E3A2F),
                    contentColor = Color.White
                )
            ) {

                Text(
                    if (state.isSavingProfile) "Saving..." else "Save Changes",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        TextButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Cancel", color = Color.Gray)
        }

        Spacer(Modifier.height(40.dp))
    }
}
@Composable
private fun FieldLabel(text: String) {

    Text(
        text = text,
        fontSize = 11.sp,
        letterSpacing = 1.sp,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}
@Composable
private fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    onTogglePassword: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { onTogglePassword?.invoke() }) {
                    Icon(
                        if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            }
        } else null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color(0xFF2d2d2d),
            unfocusedBorderColor = Color(0xFFE3DED7)
        )
    )
}

@Composable
fun ChangePasswordScreen(
    viewModel: EditProfileViewModel,
    onBack: () -> Unit
) {

    val state by viewModel.uiState.collectAsState()

    var showOld by remember { mutableStateOf(false) }
    var showNew by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F3EE))
            .statusBarsPadding()
            .padding(20.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }

            Text(
                "Change Password",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(30.dp))

        ProfileTextField(
            label = "Current Password",
            value = state.oldPassword,
            onValueChange = viewModel::onOldPasswordChange,
            isPassword = true,
            showPassword = showOld,
            onTogglePassword = { showOld = !showOld }
        )

        Spacer(Modifier.height(16.dp))

        ProfileTextField(
            label = "New Password",
            value = state.newPassword,
            onValueChange = viewModel::onNewPasswordChange,
            isPassword = true,
            showPassword = showNew,
            onTogglePassword = { showNew = !showNew }
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = viewModel::saveProfile,
            enabled = !state.isSavingProfile,
            modifier = Modifier
                .wrapContentSize()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E3A2F),
                contentColor = Color.White
            )
        )  {
            Text(
                if (state.isSavingPassword) "Changing..." else "Update Password",
                color = Color.White
            )
        }

        state.passwordError?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        state.passwordSuccess?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = Color(0xFF4CAF50))
        }
    }
}