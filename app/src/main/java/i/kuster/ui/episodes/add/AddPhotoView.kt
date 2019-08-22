package i.kuster.ui.episodes.add

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import i.kuster.R

class AddPhotoView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attributes, defStyleAttr) {
    init {
        val image = ImageView(context)
        val text = TextView(context)
        image.setImageResource(R.drawable.ic_camera)
        text.text = "Upload photo"
        text.setTextColor(Color.parseColor("#ff758c"))
        text.setTypeface(Typeface.DEFAULT_BOLD)
        orientation = VERTICAL
        addView(image)
        addView(text)
    }
}

