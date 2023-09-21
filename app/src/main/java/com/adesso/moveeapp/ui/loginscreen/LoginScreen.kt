package com.adesso.moveeapp.ui.loginscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.adesso.moveeapp.R
import com.adesso.moveeapp.data.model.authmodel.AuthUserModel
import com.adesso.moveeapp.navigation.graphs.AuthScreen
import com.adesso.moveeapp.navigation.graphs.Graph
import com.adesso.moveeapp.ui.components.IndicatorLine
import com.adesso.moveeapp.ui.components.StatusToast
import com.adesso.moveeapp.ui.theme.SystemUi
import com.adesso.moveeapp.util.Constants
import com.adesso.moveeapp.util.state.DataState
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun LoginScreen(
    navHostController: NavHostController,
) {
    val isLoading = remember { mutableStateOf(false) }
    LoginResult(navHostController = navHostController, isLoading = isLoading)

    SystemUi.Color(color = Color.Transparent)
    LaunchedEffect(Unit) {
        SystemUi.fileSystemWindows.value = false
    }

    LoginBackground(
        modifier = Modifier
            .fillMaxSize()
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoginIcon(
            modifier = Modifier
                .size(width = 108.dp, height = 149.dp)
        )
        LoginForm(
            modifier = Modifier
                .padding(start = 32.dp, end = 32.dp, top = 48.dp),
            navHostController,
            isLoading = isLoading
        )

        RegisterNow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            navHostController
        )
    }
}

@Composable
private fun LoginBackground(modifier: Modifier = Modifier) {
    val loginBgResource = painterResource(id = R.drawable.background_login)
    Image(
        modifier = modifier,
        painter = loginBgResource,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun LoginIcon(modifier: Modifier = Modifier) {
    val iconResource = ImageVector.vectorResource(id = R.drawable.ic_movee)
    Icon(
        modifier = modifier,
        imageVector = iconResource,
        tint = Color.White,
        contentDescription = null
    )
}

@Composable
private fun LoginForm(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    viewModel: LoginScreenViewModel = hiltViewModel(),
    isLoading: MutableState<Boolean>
) {
    var usernameState by remember { mutableStateOf(TextFieldValue()) }
    var passwordState by remember { mutableStateOf(TextFieldValue()) }
    var passwordVisibility by remember { mutableStateOf(false) }
    val showToast = remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = stringResource(id = R.string.login_email),
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )

            BasicTextField(
                value = usernameState,
                onValueChange = { usernameState = it },
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium
            )
            IndicatorLine(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = stringResource(id = R.string.login_password),
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                BasicTextField(
                    modifier = Modifier
                        .padding(end = 32.dp),
                    value = passwordState,
                    onValueChange = { passwordState = it },
                    textStyle = MaterialTheme.typography.displayMedium,
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                )

                IconButton(
                    modifier = Modifier
                        .size(15.dp)
                        .align(BottomEnd),
                    onClick = { passwordVisibility = !passwordVisibility }) {
                    val iconEyeSource = painterResource(id = R.drawable.ic_eye)
                    Icon(
                        painter = iconEyeSource,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
            }
            IndicatorLine(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            navHostController.navigate("${AuthScreen.WebView.route}/${Constants.FORGOT_PASSWORD_URL}")
                        }
                    )
                },
            textAlign = TextAlign.End,
            text = stringResource(id = R.string.login_forgot_password),
            style = MaterialTheme.typography.labelSmall
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            onClick = {
                if (usernameState.text.isBlank() || passwordState.text.isBlank()) {
                    showToast.value = true
                } else {
                    val newUser = AuthUserModel(usernameState.text, passwordState.text)
                    viewModel.login(newUser)
                }

            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                disabledContainerColor = Color.White
            ),
            enabled = !isLoading.value
        ) {
            if (isLoading.value) {
                LoadingLottie(modifier = Modifier.size(20.dp))
            }

            Text(
                modifier = Modifier
                    .padding(start = if (isLoading.value) 5.dp else 0.dp),
                text = stringResource(id = R.string.login_button_title),
                color = Color(0xff003dff),
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
            )

            if (showToast.value) {
                val usernamePassErrorSource =
                    stringResource(id = R.string.login_username_password_error)
                StatusToast(textMessage = usernamePassErrorSource)
            }

        }
    }
}

@Composable
private fun RegisterNow(modifier: Modifier = Modifier, navHostController: NavHostController) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.login_no_account),
            color = Color(0xffabb4bd),
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            modifier = Modifier
                .padding(start = 3.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            navHostController.navigate("${AuthScreen.WebView.route}/${Constants.REGISTER_URL}")
                        }
                    )
                },
            text = stringResource(id = R.string.login_register_now),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun LoadingLottie(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_loading))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { progress }
    )
}

@Composable
private fun LoginResult(
    navHostController: NavController,
    isLoading: MutableState<Boolean>,
    viewModel: LoginScreenViewModel = hiltViewModel()
) {

    val loginResult = viewModel.loginResult.collectAsState()

    when (loginResult.value) {
        is DataState.Initial -> {
            isLoading.value = false
        }

        is DataState.Loading -> {
            isLoading.value = true
        }

        is DataState.Success -> {
            val sessionId = (loginResult.value as DataState.Success<String>).data
            viewModel.registerItem(Constants.SESSION_ID, sessionId)
            viewModel.registerAccountId(sessionId)
            isLoading.value = false
            navHostController.popBackStack()
            navHostController.navigate(Graph.HOME)
        }

        is DataState.Error -> {
            StatusToast(
                textMessage = (loginResult.value as DataState.Error).exception.message.toString(),
            )
            isLoading.value = false
        }
    }
}


@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}