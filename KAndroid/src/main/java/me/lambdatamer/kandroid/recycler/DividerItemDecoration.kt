package me.lambdatamer.kandroid.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import me.lambdatamer.kandroid.R
import me.lambdatamer.kandroid.extensions.dpToInt
import me.lambdatamer.kandroid.extensions.getColorCompat
import kotlin.math.roundToInt

@Suppress("unused")
class DividerItemDecoration(
    context: Context,
    private val paddingLeft: Int = DEFAULT_PADDING_DP.dpToInt(context),
    private val paddingRight: Int = DEFAULT_PADDING_DP.dpToInt(context),
    private val thickness: Int = DEFAULT_THICKNESS_DP.dpToInt(context),
    @ColorInt color: Int = context.getColorCompat(R.color.black_10)
) : RecyclerView.ItemDecoration() {

    companion object {
        private const val DEFAULT_PADDING_DP = 16
        private const val DEFAULT_THICKNESS_DP = 1
    }

    private val bounds = Rect()

    private val paint = Paint().apply {
        setColor(color)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) = outRect.set(0, 0, 0, thickness)

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left: Int
        val right: Int
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.clipToPadding) {
            left = parent.paddingLeft + paddingLeft
            right = parent.width - parent.paddingRight - paddingRight
            canvas.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
        } else {
            left = 0 + paddingLeft
            right = parent.width - paddingRight
        }

        val childCount = parent.childCount

        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val bottom = bounds.bottom + child.translationY.roundToInt()
            val top = bottom - thickness
            canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }
    }
}