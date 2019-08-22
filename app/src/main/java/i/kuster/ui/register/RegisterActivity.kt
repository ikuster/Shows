package i.kuster.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import i.kuster.R
import i.kuster.data.model.UserPost
import i.kuster.ui.login.LoginViewModel
import i.kuster.ui.shared.Validations
import i.kuster.ui.shared.onTextChanged
import kotlinx.android.synthetic.main.activity_registration.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModelReg: RegisterViewModel
    private lateinit var viewModelLog: LoginViewModel
    private fun setStateOfRegisterBtn(): ArrayList<String> {
        val messageList = arrayListOf<String>()
        val (allowedUsername, messageUsername) = Validations.isUsernameInputValid(emailRegInput.text.toString())
        messageList.add(messageUsername)
        val (allowedPassword, messagePassword) = Validations.isPasswordInputValid(passwordRegInput.text.toString())
        messageList.add(messagePassword)
        val (allowedConfirmPassword, messageConfirmPassword) = Validations.confirmPasswordCheck(
            passwordRegInput.text.toString(),
            passwordConfirmRegInput.text.toString()
        )
        messageList.add(messageConfirmPassword)

        btnRegister.isEnabled = allowedUsername && allowedPassword && allowedConfirmPassword
        return messageList
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }

    lateinit var userCreate: UserPost
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        val toolbar: Toolbar = findViewById(R.id.toolbarRegistration)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.title = "Register"
        viewModelReg = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        viewModelLog = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        emailRegInput.onTextChanged {
            errorRegister.visibility= View.GONE
            setStateOfRegisterBtn()
            if(emailRegInput.hasFocus()){
                emailRegLayout.error = setStateOfRegisterBtn()[0]
            }
        }
        passwordRegInput.onTextChanged {
            setStateOfRegisterBtn()
            errorRegister.visibility= View.GONE
            if(passwordRegInput.hasFocus()){
                passwordRegLayout.error = setStateOfRegisterBtn()[1]
            }
        }
        passwordConfirmRegInput.onTextChanged {
            setStateOfRegisterBtn()
            if(passwordConfirmRegInput.hasFocus()){
                passwordConfirmRegLayout.error = setStateOfRegisterBtn()[2]
            }
        }
        viewModelReg.liveData.observe(this, Observer { register ->
            if (register != null) {
                if(register.isSuccessful){
                    viewModelLog.loginUser(userCreate,rememberMe = false)
                }
                else if(!register.isSuccessful){
                    loadingScreenRegister.visibility=View.GONE
                    errorRegister.visibility= View.VISIBLE
                    errorRegister.text=register.message.toString()
                }
            }
        })
        viewModelLog.liveData.observe(this, Observer { login ->
            if (login != null) {
                if (login.isSuccessful==true) {
                    loadingScreenRegister.visibility=View.GONE
                    startActivity(WelcomeActivity.newInstance(this, emailRegInput.text.toString()))
                }
            }
        })
        btnRegister.setOnClickListener {
            loadingScreenRegister.visibility=View.VISIBLE
            userCreate = UserPost(emailRegInput.text.toString(), passwordRegInput.text.toString())
            viewModelReg.createUser(userCreate)
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}