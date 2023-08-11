package net.tune_bot.activity

import android.content.Intent
import androidx.compose.runtime.Composable
import net.tune_bot.model.User
import net.tune_bot.view.LoginView

class LoginActivity: AbstractActivity() {
    fun success(user: User) {
        if (addAccount(user)) {
            println("account added")
        } else {
            println("account NOT added")
        }

        startActivity(
            Intent(this, MainActivity::class.java).apply {
                putExtra("user", user.copy(password = ""))
            }
        )
        finish()
    }

    @Composable
    override fun Content() = LoginView(this).Frame()
}