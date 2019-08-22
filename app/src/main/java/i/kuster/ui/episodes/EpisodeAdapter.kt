package i.kuster.ui.episodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import i.kuster.R
import i.kuster.data.model.Episodes
import i.kuster.data.model.EpisodesResponse
import i.kuster.ui.episodes.add.SEASON_EPISODE_WITH_ZERO
import kotlinx.android.synthetic.main.item_episode.view.*

class EpisodeAdapter(val clickListener: (Episodes) -> Unit) : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {
    private var listOfEpisodes: List<Episodes> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_episode,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOfEpisodes.size
    }

    fun setData(episodes: EpisodesResponse?) {
        this.listOfEpisodes = episodes?.episodesList ?: listOf()
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = listOfEpisodes[position]
        holder.updateEpisode(episode,clickListener)
    }

    class EpisodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var episodeName: TextView? = itemView.episodeName
        private var showSeason: String = ""
        private var showEpisode: String = ""
        private var seasonAndEpisode: TextView? = itemView.seasonEpisodeNumber
        fun updateEpisode(episode: Episodes,clickListener: (Episodes) -> Unit) {
            showSeason = episode.episodeSeason
            showEpisode = episode.episodeNumber
            episodeName?.text = episode.episodeTitle
            try {
                if (episode.episodeSeason.toInt() < SEASON_EPISODE_WITH_ZERO) showSeason = "0${showSeason}"
                if (episode.episodeNumber.toInt() < SEASON_EPISODE_WITH_ZERO) showEpisode = "0${showEpisode}"
            } catch (ex: Exception) {

            }
            seasonAndEpisode?.text = "S ${showSeason}  E ${showEpisode}"
            itemView.setOnClickListener{clickListener(episode)}
        }

    }
}