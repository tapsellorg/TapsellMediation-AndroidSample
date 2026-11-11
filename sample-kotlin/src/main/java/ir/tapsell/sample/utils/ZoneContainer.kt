package ir.tapsell.sample.utils

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import ir.tapsell.sample.R

class ZoneContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ScrollView(context, attrs, defStyleAttr) {

    private var onCheckedChange: ((index: Int) -> Unit) = {}
    private val chipGroup: ChipGroup = ChipGroup(context).apply {
        chipSpacingHorizontal = (8 * resources.displayMetrics.density).toInt()
        chipSpacingVertical = (8 * resources.displayMetrics.density).toInt()
        isSelectionRequired = true
        isSingleSelection = true
        setOnCheckedStateChangeListener { group, ints ->
            onCheckedChange.invoke(group.indexOfChild(group.findViewById(ints[0])))
        }
    }

    init {
        addView(
            chipGroup,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )
    }

    fun setCheckedListener(onCheckedChange: (index: Int) -> Unit) {
        this.onCheckedChange = onCheckedChange
    }

    fun addChip(
        name: String,
        checked: Boolean = false,
    ) {
        chipGroup.addView(
            Chip(context).apply {
                isClickable = true
                isCheckable = true
                setTextColor(resources.getColor(R.color.purple_500))
                setChipStrokeColorResource(R.color.purple_500)
                chipStrokeWidth = 0.5F
                text = name
                isChecked = checked
                setEnsureMinTouchTargetSize(false)
            }
        )
    }
}
