package i.kuster.ui.episodes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import i.kuster.R
import i.kuster.data.model.EpisodeGetResponse
import i.kuster.ui.episodes.comments.CommentsActivity
import kotlinx.android.synthetic.main.activity_episode_details.*

class EpisodeDetailsActivity : AppCompatActivity() {
    lateinit var viewModel: EpisodeDetailsViewModel

    companion object {
        const val EPISODE_ID = "ID"
        fun newInstance(context: Context, id: String): Intent {
            val intent = Intent(context, EpisodeDetailsActivity::class.java)
            intent.putExtra(EPISODE_ID, id)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode_details)
        viewModel = ViewModelProviders.of(this).get(EpisodeDetailsViewModel::class.java)
        val episodeId = intent.getStringExtra(EPISODE_ID)
        viewModel.getEpisode(episodeId)
        val toolbar: Toolbar = findViewById(R.id.toolbarEpisodeDetails)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        viewModel.liveData.observe(this, Observer { episode ->
            if (episode != null) {
                if (episode.isSuccessful == true) {
                    Picasso.get().load("https://api.infinum.academy${episode.episode?.episodeImage}")
                        .error(android.R.drawable.stat_notify_error)
                        .fit()
                        .into(episodeDetailsImage, object : Callback {
                            override fun onSuccess() {
                                manageProgressBar()
                                gradientImageDetails.visibility = View.VISIBLE
                                loadData(episode)
                            }

                            override fun onError(e: Exception?) {
                                manageProgressBar()
                                loadData(episode)
                                Toast.makeText(applicationContext, "There is no picture on server", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                        )
                } else if (episode.isSuccessful == null) {
                    loadingScreenDetails.visibility = View.INVISIBLE
                    errorEpDetailsLayout.visibility=View.VISIBLE
                    Toast.makeText(this, episode.message, Toast.LENGTH_LONG)
                        .show()
                }

            }
        })
        showComments.setOnClickListener {
            startActivity(CommentsActivity.newInstance(this, episodeId))
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        errorEpDetailsLayout.setOnClickListener {
            viewModel.getEpisode(episodeId)
            loadingScreenDetails.visibility = View.VISIBLE
            errorEpDetailsLayout.visibility=View.GONE
        }
    }

    private fun loadData(episode: EpisodeGetResponse) {
        episodeDetailTitle.text = episode.episode?.episodeTitle
        episodeDetailsDescription.text = episode.episode?.episodeDescription
        episodeDetailsEpNumber.text = "Ep ${episode.episode?.episodeNumber}"
        episodeDetailsSeNumber.text = "S ${episode.episode?.episodeSeason}"
    }

    private fun manageProgressBar() {
        loadingScreenDetails.visibility = View.INVISIBLE
        showComments.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        viewModel.restartLiveData()
        super.onBackPressed()
    }
}