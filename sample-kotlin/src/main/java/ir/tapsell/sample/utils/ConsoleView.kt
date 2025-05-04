package ir.tapsell.sample.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import androidx.core.view.setPadding
import com.google.android.material.textview.MaterialTextView

@SuppressLint("SetTextI18n")
class ConsoleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle,
) : MaterialTextView(context, attrs, defStyleAttr) {
    init {
        setBackgroundColor(0xFFDDDDDD.toInt())
        setPadding((Resources.getSystem().displayMetrics.density * 8).toInt())

        movementMethod = ScrollingMovementMethod()
        isVerticalScrollBarEnabled = true

        if (isInEditMode)
            text = "This is a sample log"
    }
}