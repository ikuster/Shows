package i.kuster.ui.episodes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import i.kuster.R
import i.kuster.data.model.Episodes
import i.kuster.ui.episodes.add.AddEpisodeActivity
import i.kuster.ui.login.LoginActivity
import i.kuster.ui.shows.ShowDetailsViewModel
import i.kuster.ui.shows.ShowLikeViewModel
import kotlinx.android.synthetic.main.activity_episodes.*

const val LIKE_NULL = 0
const val LIKE = 1
const val DISLIKE = 2

class EpisodesActivity : AppCompatActivity() {
    private lateinit var adapter: EpisodeAdapter
    private lateinit var viewModelShowDetails: ShowDetailsViewModel
    private lateinit var viewModelEpisodes: EpisodesViewModel
    private lateinit var viewModelLikes: ShowLikeViewModel

    companion object {
        const val SHOW_ID = "ID"
        fun newInstance(context: Context, id: String): Intent {
            val intent = Intent(context, EpisodesActivity::class.java)
            intent.putExtra(SHOW_ID, id)
            return intent
        }
    }

    fun episodeClicked(episode: Episodes) {
        startActivity(EpisodeDetailsActivity.newInstance(this, episode.episodeId))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episodes)
        val toolbar: Toolbar = findViewById(R.id.toolbarEpisodes)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        viewModelShowDetails = ViewModelProviders.of(this).get(ShowDetailsViewModel::class.java)
        viewModelEpisodes = ViewModelProviders.of(this).get(EpisodesViewModel::class.java)
        viewModelLikes = ViewModelProviders.of(this).get(ShowLikeViewModel::class.java)
        val showId = intent.getStringExtra(SHOW_ID)
        markLikeOrDislike(showId)
        adapter = EpisodeAdapter({ episode: Episodes -> episodeClicked(episode) })
        recyclerViewEpisodes.layoutManager = LinearLayoutManager(this)
        recyclerViewEpisodes.adapter = adapter
        viewModelShowDetails.getShowDetails(showId)
        viewModelShowDetails.liveData.observe(this, Observer { show ->
            if (show != null) {
                if (show.isSuccessful == true) {
                    showDetails.visibility=View.VISIBLE
                    likesView.visibility=View.VISIBLE
                    toolbar.title = show.showDetail?.showTitle
                    showDescription.text = show.showDetail?.showDescription
                    likesCount.text = show.showDetail?.showLikes.toString()
                } else if (show.isSuccessful == null) {
                    Toast.makeText(this, show.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModelEpisodes.getEpisodes(showId)
        viewModelEpisodes.liveData.observe(this, Observer { episodes ->
            if (episodes != null) {
                if (episodes.isSuccessful == true) {
                    if (!episodes.episodesList.isNullOrEmpty()) {
                        loadingScreenEpisodes.visibility = View.GONE
                        recyclerViewEpisodes.visibility = View.VISIBLE
                        addEpisodeBtn.show()
                        adapter.setData(episodes)
                    } else {
                        loadingScreenEpisodes.visibility = View.GONE
                        addEpisodeBtn.show()
                        emptyEpisodesLayout.visibility = View.VISIBLE
                    }
                } else if (episodes.isSuccessful == null) {
                    loadingScreenEpisodes.visibility = View.GONE
                    errorEpisodeLayout.visibility=View.VISIBLE
                    Toast.makeText(this, episodes.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
        errorEpisodeLayout.setOnClickListener {
            viewModelShowDetails.getShowDetails(showId)
            viewModelEpisodes.getEpisodes(showId)
            loadingScreenEpisodes.visibility = View.VISIBLE
            errorEpisodeLayout.visibility=View.GONE
        }

        addEpisodeBtn.setOnClickListener {
            startActivity(
                AddEpisodeActivity.newInstance(this, showId)
            )
        }
        addEpisodeLabel.setOnClickListener {
            startActivity(
                AddEpisodeActivity.newInstance(this, showId)
            )
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        viewModelLikes.liveData.observe(this, Observer { like ->
            if (like != null) {
                when {
                    like.isSuccessful == true && like.type == true -> markThumbsUp()
                    like.isSuccessful == true && like.type == false -> markThumbsDown()
                    like.isSuccessful == false -> showDialogOnError(like.message.toString())
                    like.isSuccessful == null -> Toast.makeText(this, like.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        likeEpisode.setOnClickListener {
            if (viewModelLikes.getLikeStatus(showId) == LIKE_NULL || viewModelLikes.getLikeStatus(showId) == DISLIKE) {
                viewModelLikes.likeShow(showId)
            } else {
                Toast.makeText(this, "You have already voted!", Toast.LENGTH_SHORT).show()
            }
        }
        dislikeEpisode.setOnClickListener {
            if (viewModelLikes.getLikeStatus(showId) == LIKE_NULL || viewModelLikes.getLikeStatus(showId) == LIKE) {
                viewModelLikes.dislikeShow(showId)
            } else {
                Toast.makeText(this, "You have already voted!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun markLikeOrDislike(showId: String) {
        if (viewModelLikes.getLikeStatus(showId) == LIKE) {
            markThumbsUp()
        } else if (viewModelLikes.getLikeStatus(showId) == DISLIKE) {
            markThumbsDown()
        }
    }

    private fun markThumbsUp() {
        dislikeEpisode.isSelected = false
        dislikeEpisodeImage.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorLikeNotClicked)
        likeEpisode.isSelected = true
        likeEpisodeImage.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorLikeClicked)
    }

    private fun markThumbsDown() {
        likeEpisode.isSelected = false
        likeEpisodeImage.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorLikeNotClicked)
        dislikeEpisode.isSelected = true
        dislikeEpisodeImage.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorLikeClicked)
    }

    fun showDialogOnError(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert").setMessage(message)
            .setNegativeButton("Cancel") { _, _ ->
            }
            .setPositiveButton("Ok") { _, _ ->
                finish()
                viewModelLikes.logOut()
                startActivity(LoginActivity.newInstance(this))
            }.show()
    }

    override fun onBackPressed() {
        viewModelLikes.restartLiveData()
        viewModelEpisodes.restartEpisodes()
        viewModelShowDetails.restartShowDetails()
        super.onBackPressed()
    }
}