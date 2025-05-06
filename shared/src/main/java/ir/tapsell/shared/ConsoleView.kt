package ir.tapsell.shared

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import android.widget.TextView

@SuppressLint("SetTextI18n")
class ConsoleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle,
) : TextView(context, attrs, defStyleAttr) {
    init {
        setBackgroundColor(0xFFDDDDDD.toInt())
        val padding = (Resources.getSystem().displayMetrics.density * 8).toInt()
        setPadding(padding, padding, padding, padding)

        movementMethod = ScrollingMovementMethod()
        isVerticalScrollBarEnabled = true

        if (isInEditMode)
            text = "This is a sample log"
    }

    // TODO use a dedicated method for logging instead of setText/appendText
}