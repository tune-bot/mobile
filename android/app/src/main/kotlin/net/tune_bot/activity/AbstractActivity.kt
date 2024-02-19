package net.tune_bot.activity

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.tune_bot.build.PromotionLevel
import net.tune_bot.R
import net.tune_bot.controller.Api
import net.tune_bot.model.AbstractModel
import net.tune_bot.model.User
import net.tune_bot.view.Theme
import java.io.File

abstract class AbstractActivity: AppCompatActivity() {
    lateinit var api: Api

    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        api = Api(PromotionLevel.valueOf(getString(R.string.promotion_level)))

        setContent {
            Theme(isDark = true) { Content() }
        }
    }

    @Composable
    abstract fun Content()

    fun userFile() = File(filesDir, getString(R.string.user_file))

    fun loadUser(file: File = userFile()): User? {
        user = runBlocking {
            user ?: if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.extras?.getSerializable("user", User::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.extras?.getSerializable("user") as User?
            } ?: run {
                val accountManager = AccountManager.get(this@AbstractActivity)
                accountManager.getAccountsByType(getString(R.string.account_type)).firstOrNull()?.let { account ->
                    accountManager.getPassword(account)?.let { password ->
                        withContext(Dispatchers.IO) {
                            User.login(api, account.name, password)
                        }
                    } // TODO find out why password is always null
                }
            } ?: try {
                withContext(Dispatchers.IO) {
                    AbstractModel.load(file)
                }
            } catch (e: Throwable) {
                e.printStackTrace()  // todo do I want stack trace?
                null
            }
        }

        return user
    }

    fun addAccount(user: User) =
        AccountManager.get(this).addAccountExplicitly(
            Account(user.username, getString(R.string.account_type)),
            user.password,
            null
        )
}