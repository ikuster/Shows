package i.kuster.ui.shows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import i.kuster.R
import i.kuster.data.model.Show
import i.kuster.data.model.ShowResponse
import kotlinx.android.synthetic.main.item_list_shows.view.*

class ShowsAdapter(val itemDisplay: Int, val clickListener: (Show) -> Unit) :
    RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>() {


    private var listOfShows: List<Show> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        return ShowsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                itemDisplay,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOfShows.size
    }

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        val show = listOfShows[position]
        holder.updateShow(show, clickListener)
    }

    fun setData(shows: ShowResponse?) {
        this.listOfShows = shows?.showList ?: listOf()
        notifyDataSetChanged()
    }

    class ShowsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var showImage = itemView.seriesImage
        private var showName = itemView.seriesName
        private var showLikes= itemView.numLike

        fun updateShow(show: Show, clickListener: (Show) -> Unit) {
            if (show.showImage.isEmpty()) {
                Picasso.get().load(R.drawable.placeholder)
                    .error(android.R.drawable.stat_notify_error)
                    .into(showImage)
            } else {
                Picasso.get().load("https://api.infinum.academy/${show.showImage}")
                    .error(android.R.drawable.stat_notify_error)
                    .placeholder(R.drawable.progress_anim)
                    .fit().noFade()
                    .into(showImage)
            }
            showName?.text = show.showTitle
            showLikes?.text=show.showLikes.toString()
            itemView.setOnClickListener { clickListener(show) }
        }
    }
}