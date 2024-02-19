package net.tune_bot.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.tune_bot.R
import net.tune_bot.activity.LoginActivity
import net.tune_bot.model.User

class LoginView {
    @Composable
    fun Frame(context: LoginActivity) {
        var needsToRegister by remember { mutableStateOf(false) }

        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirm by remember { mutableStateOf("") }

        fun validate() =
            username.isNotBlank() && username.isNotEmpty() && username.length in USERNAME_LENGTH_RANGE &&
                    password.isNotBlank() && password.isNotEmpty() && password.length in PASSWORD_LENGTH_RANGE &&
                    if(needsToRegister) confirm == password else true

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(R.drawable.logo_text_dark),
                stringResource(R.string.app_name),
                modifier = Modifier.width(180.dp)
            )

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            if (needsToRegister) {
                TextField(
                    value = confirm,
                    onValueChange = { confirm = it },
                    label = { Text("Confirm") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (!validate()) {
                            println("validate failed") //todo present error to user
                        } else if (needsToRegister) {
                            User.register(context.api, username, password)
                        } else {
                            User.login(context.api, username, password)
                        }?.let {
                            it.save(context.userFile())
                            (context).success(it)
                        }
                    }
                }
            ) {
                Text(if (needsToRegister) "Register" else "Login")
            }

            Row {
                Text(
                    if (needsToRegister) "Already have an account?" else "Need to create an account?",
                    color = Color.White
                )

                Text(
                    "Tap here.",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { needsToRegister = !needsToRegister },
                    color = Color.Blue
                )
            }
        }
    }

    companion object {
        // TODO move this to activity
        const val MIN_USERNAME_LENGTH = 1
        const val MAX_USERNAME_LENGTH = 32
        const val MIN_PASSWORD_LENGTH = 8
        const val MAX_PASSWORD_LENGTH = 72

        val USERNAME_LENGTH_RANGE = MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH
        val PASSWORD_LENGTH_RANGE = MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH

        // TODO validate characters in user and pass. letter and num in user, letter num and limited chars in pass
    }
}