package net.tune_bot.activity

import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.Composable

class LauncherActivity: AbstractActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadUser()?.let {
            startActivity(
                Intent(this@LauncherActivity, MainActivity::class.java).apply {
                    putExtra("user", it)
                }
            )
        } ?: startActivity(Intent(this@LauncherActivity, LoginActivity::class.java))

        finish()
    }

    @Composable
    override fun Content() = Unit
}
  