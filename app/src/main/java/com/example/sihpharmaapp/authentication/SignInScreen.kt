package com.example.sihpharmaapp.authentication

import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sihpharmaapp.Screens
import com.example.sihpharmaapp.ui.theme.buttonBackgroundColor
import com.example.sihpharmaapp.ui.theme.fadedWhite
import com.example.sihpharmaapp.ui.theme.homeBackground
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setNavigationBarColor(Color.Red)
    systemUiController.setSystemBarsColor(Color.Red)
    systemUiController.setStatusBarColor(fadedWhite)
    var emailTextState by remember {
        mutableStateOf("")
    }
    var passwordTextState by remember {
        mutableStateOf("")
    }
    var progressBarState by remember {
        mutableStateOf(false)
    }
    var dialogBoxState by remember {
        mutableStateOf(false)
    }
    var dialogTextState by remember {
        mutableStateOf("")
    }
    var dialogBoxButtonState by remember {
        mutableStateOf(false)
    }
    dialogBoxButtonState = dialogTextState.isEmailValid()

    if(dialogBoxState){
        ResetDialogBox(
            dialogTextState = dialogTextState,
            enabled = dialogBoxButtonState,
            onValueChange = {
                dialogTextState = it
            },
            onCancelClicked = {
                dialogBoxState = false
            },
            onSubmitClicked = {
                viewModel.sendPasswordResetMail(dialogTextState)
                dialogBoxState = false
            }
        )
    }

    val context = LocalContext.current

    when(viewModel.resetPasswordState.value){
        LoginState.Success -> {
            progressBarState = false
        }
        LoginState.Loading -> {
            progressBarState = true
        }

        LoginState.Error -> {
            Toast.makeText(context, "Password reset failed!",Toast.LENGTH_LONG).show()
            progressBarState = false
        }
    }
    val scope = rememberCoroutineScope()
    val loginState = remember { mutableStateOf<LoginState>(LoginState.Idle) }
    when (loginState.value) {
        LoginState.Success -> {
            progressBarState = false
        }
        LoginState.Loading -> {
            progressBarState = true
        }
        LoginState.Error -> {
            progressBarState = false
        }

        LoginState.Idle -> {
            progressBarState = false
        }
    }

    if (loginState.value == LoginState.Success) {
        navController.navigate(Screens.HomeScreen.route) {
            popUpTo(Screens.SignInScreen.route) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color.Red
                    )
                )
            )
            .padding(8.dp)
            .padding(top = 40.dp)
    ) {
        GreetingSection(
            modifier = Modifier
                .weight(.2f)
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp
                )
        )
        TextFieldsSection(
            modifier = Modifier
                .weight(.2f)
                .fillMaxWidth(),
            emailChange = { currEmail ->
                emailTextState = currEmail
            },
            passwordChange = { currPassword ->
                passwordTextState = currPassword
            }
        )
        Column(
            modifier = Modifier
                .weight(.6f)
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 40.dp,
                        start = 55.dp,
                        end = 55.dp
                    ),
                text = "Sign In",
                backgroundColor = buttonBackgroundColor,
                disabledBackgroundColor = Color.Transparent,
                enabled = emailTextState.isEmailValid() && passwordTextState.isNotEmpty()
            ) {
                // Perform Sign In
                val email = emailTextState
                val password = passwordTextState
                scope.launch {
                    viewModel.signInUser(email, password).collect {
                        loginState.value = it
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (progressBarState) CircularProgressIndicator(
                color = buttonBackgroundColor
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 10.dp,
                        bottom = 20.dp
                    ),
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .clickable {
                            // Handle forgot Password
                            dialogBoxState = true
                        }
                        .alpha(.6f),
                    text = "Forgot password?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.BottomCenter)
                        .clickable {
                            // Handle sign up
                            navController.navigate(Screens.SignUpScreen.route) {
                                popUpTo(Screens.SignInScreen.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                        .alpha(.6f),
                    text = "New here? Sign up instead...",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun ResetDialogBox(
    dialogTextState: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
    onCancelClicked: () -> Unit,
    onSubmitClicked: () -> Unit
) {
    AlertDialog(
        backgroundColor = homeBackground,
        text = {
            CustomTextField(
                label = "E-mail",
                placeholder = "Enter your E-mail Id",
                value = dialogTextState,
                keyboardType = KeyboardType.Email,
                textVisible = true,
                onValueChange = {
                    onValueChange(it)
                }
            )
        },
        onDismissRequest = {},
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onSubmitClicked()
                    },
                    enabled = enabled
                ) {
                    Text(text = "Continue")
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        onCancelClicked()
                    }
                ) {
                    Text(text = "Cancel")
                }
            }
        },
        title = {
            Text(text = "Enter your email address.")
        }
    )
}

@Composable
fun GreetingSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Hello there,",
            fontWeight = FontWeight.SemiBold,
            fontSize = 30.sp,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Welcome Back",
            fontWeight = FontWeight.SemiBold,
            fontSize = 30.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
fun TextFieldsSection(
    modifier: Modifier = Modifier,
    emailChange: (String) -> Unit,
    passwordChange: (String) -> Unit,
) {
    var emailTextState by remember {
        mutableStateOf("")
    }
    var passwordTextState by remember {
        mutableStateOf("")
    }
    Column(
        modifier = modifier.padding(
            start = 20.dp,
            end = 20.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "E-mail",
            placeholder = "Enter your E-mail Id",
            value = emailTextState,
            keyboardType = KeyboardType.Email,
            textVisible = true
        ) { currValue ->
            emailTextState = currValue
            emailChange(currValue)
        }

        Spacer(modifier = Modifier.height(20.dp))

        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "Password",
            placeholder = "Enter your Password",
            value = passwordTextState,
            keyboardType = KeyboardType.Password,
            textVisible = false
        ) { currValue ->
            passwordTextState = currValue
            passwordChange(currValue)
        }
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String,
    value: String,
    keyboardType: KeyboardType,
    textVisible: Boolean,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { currentValue ->
            onValueChange(currentValue)
        },
        placeholder = {
            Text(text = placeholder)
        },
        label = {
            Text(text = label)
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (textVisible) VisualTransformation.None else PasswordVisualTransformation(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.DarkGray,
            focusedBorderColor = Color.DarkGray,
            unfocusedBorderColor = Color.DarkGray,
            placeholderColor = Color.DarkGray,
            focusedLabelColor = Color.DarkGray,
            unfocusedLabelColor = Color.DarkGray
        ),
        modifier = modifier,
        singleLine = true
    )
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}