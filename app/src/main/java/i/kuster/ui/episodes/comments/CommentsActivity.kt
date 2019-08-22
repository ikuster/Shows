package i.kuster.ui.episodes.comments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import i.kuster.R
import i.kuster.data.model.CommentGet
import i.kuster.data.model.CommentPost
import i.kuster.ui.login.LoginActivity
import i.kuster.ui.shared.Validations
import kotlinx.android.synthetic.main.activity_comments.*

class CommentsActivity : AppCompatActivity() {
    private lateinit var adapter: CommentsAdapter
    private lateinit var viewModelGet: CommentsGetViewModel
    private lateinit var viewModelPost: CommentsPostViewModel
    private lateinit var viewModelDelete: CommentDeleteViewModel

    companion object {
        const val EPISODE_ID = "ID"
        fun newInstance(context: Context, id: String): Intent {
            val intent = Intent(context, CommentsActivity::class.java)
            intent.putExtra(EPISODE_ID, id)
            return intent
        }
    }

    fun deleteCommentClick(comment: CommentGet) {
        viewModelDelete.deleteComment(comment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        val toolbar: Toolbar = findViewById(R.id.toolbarComments)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.title = "Comments"
        viewModelGet = ViewModelProviders.of(this).get(CommentsGetViewModel::class.java)
        viewModelPost = ViewModelProviders.of(this).get(CommentsPostViewModel::class.java)
        viewModelDelete = ViewModelProviders.of(this).get(CommentDeleteViewModel::class.java)
        val episodeId = intent.getStringExtra(EPISODE_ID)
        adapter = CommentsAdapter { comment: CommentGet -> deleteCommentClick(comment) }
        recyclerViewComments.layoutManager = LinearLayoutManager(this)
        recyclerViewComments.adapter = adapter
        viewModelGet.getComments(episodeId)
        viewModelGet.liveData.observe(this, Observer { comments ->
            if (comments != null) {
                if (comments.isSuccessful == null) {
                    Toast.makeText(this, comments.message, Toast.LENGTH_LONG).show()
                    errorCommentsLayout.visibility=View.VISIBLE
                    loadingScreenComments.visibility = View.GONE
                }
                if (comments.isSuccessful == true) {
                    if (!comments.commentsList.isNullOrEmpty()) {
                        hideProgressBar()
                        emptyCommentsLayout.visibility = View.GONE
                        recyclerViewComments.visibility = View.VISIBLE
                        adapter.setData(comments)
                    } else {
                        hideProgressBar()
                        recyclerViewComments.visibility = View.GONE
                        emptyCommentsLayout.visibility = View.VISIBLE
                    }
                }

            }
        })
        postComment.setOnClickListener {
            val (isValid,message)=Validations.isCommentValid(commentInput.text.toString())
            if(isValid){
                val comment = CommentPost(commentInput.text.toString(), episodeId)
                postCommentMessage.visibility = View.VISIBLE
                viewModelPost.postComment(comment)
            }
           else{
                Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
            }
        }
        viewModelPost.liveData.observe(this, Observer { comment ->
            if (comment != null) {
                if (comment.isSuccessful == null) {
                    postCommentMessage.visibility = View.GONE
                    errorCommentsLayout.visibility=View.VISIBLE
                    Toast.makeText(this, comment.message, Toast.LENGTH_LONG).show()
                } else if (comment.isSuccessful == false) {
                    postCommentMessage.visibility = View.GONE
                    errorCommentsLayout.visibility=View.VISIBLE
                    showDialogOnError(comment.message.toString())
                } else if (comment.isSuccessful == true) {
                    postCommentMessage.visibility = View.GONE
                    commentInput.text.clear()
                }
            }
        })

        viewModelDelete.liveData.observe(this, Observer { delete ->
            if (delete != null) {
                if (delete.isSuccessful == false) {
                    showDialogOnError(delete.message.toString())
                }
            }
        })
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        errorCommentsLayout.setOnClickListener {
            errorCommentsLayout.visibility=View.GONE
            loadingScreenComments.visibility=View.VISIBLE
            viewModelGet.getComments(episodeId)
        }
    }

    fun showDialogOnError(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert").setMessage(message)
            .setNegativeButton("Cancel") { _, _ ->
            }
            .setPositiveButton("Yes") { _, _ ->
                finish()
                viewModelPost.logOut()
                startActivity(LoginActivity.newInstance(this))
            }.show()
    }

    private fun hideProgressBar() {
        loadingScreenComments.visibility = View.GONE
        postCommentLayout.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        viewModelGet.restartComments()
        viewModelPost.restartTokenCondition()
        super.onBackPressed()
    }
}