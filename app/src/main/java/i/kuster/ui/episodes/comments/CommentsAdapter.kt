package i.kuster.ui.episodes.comments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import i.kuster.R
import i.kuster.data.model.CommentGet
import i.kuster.data.model.CommentGetResponse
import i.kuster.data.repository.USER
import i.kuster.data.repository.sharedPreferences
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentsAdapter(val clickListener: (CommentGet) -> Unit) :
    RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {
    private var listOfComments: List<CommentGet> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_comment,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOfComments.size
    }

    fun setData(comments: CommentGetResponse?) {
        this.listOfComments = comments?.commentsList ?: listOf()
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val comment = listOfComments[position]
        holder.updateComment(comment)
    }

    inner class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var commentAuthor: TextView? = itemView.usernameComment
        private var commentContent: TextView? = itemView.commentContent
        fun updateComment(comment: CommentGet) {
            val user = sharedPreferences.getString(USER, "")
            if (comment.autorEmail == user) {
                itemView.deleteComment.visibility = View.VISIBLE
                itemView.deleteComment.setOnClickListener {
                    showDialogOnCommentDelete(itemView.context,comment)
                }
            }
            else{
                itemView.deleteComment.visibility = View.GONE
            }
            commentAuthor?.text = comment.autorEmail
            commentContent?.text = comment.commentContent
        }

    }

    fun showDialogOnCommentDelete(context: Context,comment: CommentGet) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Alert").setMessage("Are you sure you want to delete the comment?")
            .setNegativeButton("No") { _, _ ->
            }
            .setPositiveButton("Yes") { _, _ ->
                clickListener(comment)
            }.show()
    }
}