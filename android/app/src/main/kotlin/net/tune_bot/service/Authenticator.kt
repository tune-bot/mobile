package net.tune_bot.service

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle

import android.os.IBinder
import net.tune_bot.activity.LoginActivity


class Authenticator : Service() {
    private lateinit var authenticator: AccountAuthenticator
    
    override fun onCreate() {
        super.onCreate()

        authenticator = AccountAuthenticator(this)
    }

    override fun onBind(intent: Intent?): IBinder = authenticator.iBinder

    class AccountAuthenticator(private val context: Context): AbstractAccountAuthenticator(context) {
        override fun addAccount(
            response: AccountAuthenticatorResponse?,
            accountType: String?,
            authTokenType: String?,
            requiredFeatures: Array<out String>?,
            options: Bundle?
        ) = Bundle().apply {
            putParcelable(
                AccountManager.KEY_INTENT,
                Intent(context, LoginActivity::class.java).apply {
                    putExtra(context.getString(net.tune_bot.R.string.account_type), android.R.attr.accountType)
                    putExtra("full_access", authTokenType)
                    putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
                }
            )
        }

        override fun editProperties(
            response: AccountAuthenticatorResponse?,
            accountType: String?
        ): Bundle {
            TODO("Not yet implemented")
        }

        override fun confirmCredentials(
            response: AccountAuthenticatorResponse?,
            account: Account?,
            options: Bundle?
        ): Bundle {
            TODO("Not yet implemented")
        }

        override fun getAuthToken(
            response: AccountAuthenticatorResponse?,
            account: Account?,
            authTokenType: String?,
            options: Bundle?
        ): Bundle {
            TODO("Not yet implemented")
        }

        override fun getAuthTokenLabel(authTokenType: String?): String {
            TODO("Not yet implemented")
        }

        override fun updateCredentials(
            response: AccountAuthenticatorResponse?,
            account: Account?,
            authTokenType: String?,
            options: Bundle?
        ): Bundle {
            TODO("Not yet implemented")
        }

        override fun hasFeatures(
            response: AccountAuthenticatorResponse?,
            account: Account?,
            features: Array<out String>?
        ): Bundle {
            TODO("Not yet implemented")
        }
    }
}