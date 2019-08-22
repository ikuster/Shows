package i.kuster.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import i.kuster.R
import i.kuster.ui.shows.ShowsActivity
import kotlinx.android.synthetic.main.activity_welcome.*

const val DELAY_EXIT: Long = 2000
lateinit var viewModel: WelcomeActivityViewModel

class WelcomeActivity : AppCompatActivity() {

    companion object {
        const val EMAIL = "EMAIL"

        fun newInstance(context: Context, email: String): Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra(EMAIL, email)
            return intent
        }
    }

    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        viewModel = ViewModelProviders.of(this).get(WelcomeActivityViewModel::class.java)
        val email = intent.getStringExtra(EMAIL)
        welcomeUsername.text = "Welcome ${viewModel.getPrefix(email)}"
        handler.postDelayed(
            { run { finish();startActivity(ShowsActivity.newInstance(this)) } },
            DELAY_EXIT
        )
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }
}