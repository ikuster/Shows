package i.kuster.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.lifecycle.ViewModelProviders
import i.kuster.R
import i.kuster.ui.login.LoginActivity
import i.kuster.ui.register.DELAY_EXIT
import i.kuster.ui.shows.ShowsActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    lateinit var viewModel: SplashViewModel
    private val handler = Handler()
    var rememberMeVal: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        splashAnim()
    }

    private fun chooseActivity() {
        rememberMeVal = viewModel.getRememberMe()
        if (rememberMeVal) {
            startActivity(ShowsActivity.newInstance(this))
        } else {
            startActivity(LoginActivity.newInstance(this))
        }
    }

    private fun splashAnim() {
        logo.doOnLayout {
            logo.animate()
                .translationY(0f)
                .setDuration(1000)
                .setInterpolator(BounceInterpolator())
                .withEndAction {
                    shows.visibility = View.VISIBLE
                    shows.animate()
                        .scaleXBy(20f)
                        .scaleYBy(20f)
                        .withEndAction {
                            shows.animate()
                                .scaleXBy(-10f)
                                .scaleYBy(-10f)
                        }
                    handler.postDelayed(
                        { run { finish();chooseActivity() } },
                        DELAY_EXIT
                    )
                }
                .start()
        }
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }
}