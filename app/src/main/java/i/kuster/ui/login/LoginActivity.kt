package i.kuster.ui.login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import i.kuster.R
import i.kuster.data.model.UserPost
import i.kuster.data.repository.PREF_NAME
import i.kuster.data.repository.sharedPreferences
import i.kuster.ui.register.RegisterActivity
import i.kuster.ui.shared.Validations
import i.kuster.ui.shared.onTextChanged
import i.kuster.ui.shows.ShowsActivity
import kotlinx.android.synthetic.main.activity_login.*

const val USERNAME_INPUT = "username"
const val PASSWORD_INPUT = "password"

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    fun setStateOfLoginBtn(): Pair<String, String> {
        val (allowedUsername, messageUsername) = Validations.isUsernameInputValid(usernameInput.text.toString())
        val (allowedPassword, messagePassword) = Validations.isPasswordInputValid(passwordInput.text.toString())
        btnLogin.isEnabled = allowedUsername && allowedPassword
        return Pair(messageUsername, messagePassword)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.liveData.observe(this, Observer { login ->
            if (login != null) {
                if (login.isSuccessful == true) {
                    finish()
                    startActivity(ShowsActivity.newInstance(this))
                } else if (login.isSuccessful == false) {
                    errorLogin.visibility = View.VISIBLE
                    errorLogin.text = login.message.toString()
                    appLogo.clearAnimation()
                }
            }
        })
        setContentView(R.layout.activity_login)
        usernameInput.onTextChanged {
            errorLogin.visibility = View.GONE
            setStateOfLoginBtn()
            if (usernameInput.hasFocus()) {
                usernameLayout.error = setStateOfLoginBtn().first
            }
        }
        passwordInput.onTextChanged {
            errorLogin.visibility = View.GONE
            setStateOfLoginBtn()
            if (passwordInput.hasFocus()) {
                passwordLayout.error = setStateOfLoginBtn().second
            }
        }
        btnLogin.setOnClickListener {
            errorLogin.visibility = View.GONE
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            loginAnim()
            viewModel.loginUser(UserPost(username, password), rememberMe.isChecked)
        }
        registerClick.setOnClickListener {
            startActivity(RegisterActivity.newInstance(this))

        }
    }

    private fun loginAnim() {
        val rotate = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.setDuration(2500);
        rotate.setRepeatCount(Animation.INFINITE)
        appLogo.startAnimation(rotate)
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
    }
}

