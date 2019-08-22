package i.kuster.ui.shows

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import i.kuster.R
import i.kuster.data.model.Show
import i.kuster.ui.episodes.EpisodesActivity
import i.kuster.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_shows.*

class ShowsActivity : AppCompatActivity() {
    private lateinit var viewModelShows: ShowViewModel
    private lateinit var adapter: ShowsAdapter

    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ShowsActivity::class.java)
            return intent
        }
    }

    fun showClicked(show: Show) {
        startActivity(EpisodesActivity.newInstance(this, show.showId))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows)
        val gridLayout = GridLayoutManager(this, 2)
        setUpAdapterGrid(gridLayout)
        viewModelShows = ViewModelProviders.of(this).get(ShowViewModel::class.java)
        runObserverShows()
        gridRecycler.setOnClickListener {
            setUpAdapterGrid(gridLayout)
            runObserverShows()
            gridRecycler.hide()
            listRecycler.show()
        }
        listRecycler.setOnClickListener {
            adapter = ShowsAdapter(R.layout.item_list_shows, { show: Show -> showClicked(show) })
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
            runObserverShows()
            listRecycler.hide()
            gridRecycler.show()
        }
        logOutIcon.setOnClickListener {
            showDialogLogOut()
        }
        errorShowsLayout.setOnClickListener {
            viewModelShows.getShowData()
            errorShowsLayout.visibility = View.GONE
        }
    }

    private fun setUpAdapterGrid(gridLayout: GridLayoutManager) {
        adapter = ShowsAdapter(R.layout.item_grid_shows, { show: Show -> showClicked(show) })
        recyclerView.layoutManager = gridLayout
        recyclerView.adapter = adapter
    }

    private fun runObserverShows() {
        viewModelShows.getShowData()
        viewModelShows.liveData.observe(this, Observer { shows ->
            if (shows != null) {
                if (shows.isSuccessful == true) {
                    if (listRecycler.visibility == View.VISIBLE) {
                        gridRecycler.show()
                    } else if (gridRecycler.visibility == View.VISIBLE) {
                        listRecycler.show()
                    } else {
                        gridRecycler.show()
                        listRecycler.show()
                    }
                    recyclerView.visibility = View.VISIBLE
                    adapter.setData(shows)
                } else if (shows.isSuccessful == null) {
                    errorShowsLayout.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    gridRecycler.hide()
                    listRecycler.hide()
                    Toast.makeText(this, shows.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    fun showDialogLogOut() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert").setMessage(
            "Are you sure you want to logout?"
        )
            .setNeutralButton("Cancel") { _, _ -> }
            .setPositiveButton("Yes") { _, _ ->
                finish()
                viewModelShows.logOut()
                startActivity(LoginActivity.newInstance(this))
            }.show()
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
    }
}