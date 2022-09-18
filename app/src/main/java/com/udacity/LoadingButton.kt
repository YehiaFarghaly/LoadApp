package com.udacity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.lang.reflect.Type
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0f
    private var heightSize = 0f
    private var progessCircle = 0f
    private var progressRectangle = 0f
    private var text: String = "Download"
    private val backgroundColor:Int
    private val loadingColor = ResourcesCompat.getColor(resources, R.color.colorAccent, null)
    private val textColor:Int
    private var valueAnimator = ValueAnimator()

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                setText("Downloading...")
                valueAnimator = ValueAnimator.ofFloat(0f, widthSize).apply {
                    addUpdateListener {
                        progessCircle = animatedValue as Float
                        progressRectangle = (widthSize / 400) * progessCircle
                        invalidate()
                    }
                    duration = 3000
                    start()
                }
                this.isEnabled = false
            }
            ButtonState.Completed -> {
                setText("Download")
                valueAnimator.cancel()
                progessCircle = 0f
                progressRectangle = 0f
                this.isEnabled = true
                invalidate()
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun setText(text: String) {
        this.text = text
        invalidate()
    }


    init {
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.LoadingButton,
                0, 0).apply {

                try {
                    textColor = getColor(R.styleable.LoadingButton_textColor,Color.BLACK)
                    backgroundColor = getColor(R.styleable.LoadingButton_backgroundColor,Color.YELLOW)
                } finally {
                    recycle()
                }
            }

    }

    private val paintButton = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = backgroundColor
    }
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = textColor
        typeface = Typeface.create("Arial", Typeface.BOLD)
        textSize = resources.getDimension(R.dimen.default_text_size)
    }
    private val paintLoading = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = loadingColor
    }
    private val paintCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ResourcesCompat.getColor(resources, R.color.purple, null)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawButton(canvas)
        drawProgress(canvas)
        drawText(canvas)
    }

    private fun drawText(canvas: Canvas?) {
        val textWidth = paintText.measureText(text)
        val bound: Rect = Rect()
        paintText.getTextBounds(text, 0, text.length, bound)
        val textHeight = bound.height()
        canvas?.drawText(
            text,
            widthSize / 2 - textWidth / 2,
            heightSize / 2 + (textHeight).toFloat() / 2,
            paintText
        )
    }

    private fun drawButton(canvas: Canvas?) {
        canvas?.drawRect(0f, 0f, widthSize, heightSize, paintButton)
    }

    private fun drawProgress(canvas: Canvas?) {
        val s: String = "Downloading..."
        canvas?.drawRect(0f, 0f, progressRectangle, heightSize, paintLoading)
        val textWidth = paintText.measureText(s)
        val bound: Rect = Rect()
        paintText.getTextBounds(s, 0, s.length, bound)
        val textHeight = bound.height()
        val left = widthSize / 2 + textWidth / 1.8f
        val top = heightSize.toFloat() / 2 - textHeight

        val circle = RectF(
            left, top,
            width.toFloat() - left / 4, height.toFloat() - textHeight / 2
        )
        canvas?.drawArc(circle, 0f, progessCircle, true, paintCircle)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w.toFloat()
        heightSize = h.toFloat()
        setMeasuredDimension(w, h)
    }

}